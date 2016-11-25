package com.anjz.core.service.impl.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.filter.SearchFilter;
import com.anjz.base.entity.search.filter.SearchFilterHelper;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.PersonalMessageDao;
import com.anjz.core.enums.MessageState;
import com.anjz.core.model.PersonalMessage;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.anjz.exception.BusinessException;
import com.anjz.message.push.PushApi;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年9月18日下午12:13:51
 */
@Service
public class PersonalMessageServiceImpl extends BaseServiceImpl<PersonalMessage, String> implements PersonalMessageService{
	
	@Resource
	private PersonalMessageDao personalMessageDao;
	
	@Resource
	private PushApi pushApi;
	
	/**
	 * 通过用户id和状态查询消息列表
	 * @param userId
	 * @param state
	 * @param pageable
	 * @return
	 */
	public Page<PersonalMessage> findUserMessage(String userId, MessageState state, Pageable pageable){
		Searchable searchable = Searchable.newSearchable();
        searchable.setPage(pageable);       
        this.addQueryFilter(searchable, userId, state);
        return this.findAll(searchable);
	}
	
	public List<PersonalMessage> findUserMessage(String userId, MessageState state){
		Searchable searchable = Searchable.newSearchable();       
		this.addQueryFilter(searchable, userId, state);		       
        return this.findAllWithNoPageNoSort(searchable);
	}
	
	private void addQueryFilter(Searchable searchable, String userId,MessageState state){
		switch (state) {
        //草稿箱、发件箱
        case draft_box:
        case out_box:
        	 searchable.addSearchFilter("sender_id", SearchOperator.eq, userId);
             searchable.addSearchFilter("sender_state", SearchOperator.eq, state);
			break;
		//收件箱
        case in_box:
        	searchable.addSearchFilter("receiver_id", SearchOperator.eq, userId);
            searchable.addSearchFilter("receiver_state", SearchOperator.eq, state);
        	break;
        	
        //收藏箱、垃圾箱
        case store_box:
        case trash_box:
        	//sender           
            SearchFilter customFilter1=SearchFilterHelper.newCondition("", SearchOperator.custom, 
            		"(sender_id ='"+userId+"' and sender_state = '"+state+"')");

            //receiver
            SearchFilter customFilter2=SearchFilterHelper.newCondition("", SearchOperator.custom, 
            		"(receiver_id ='"+userId+"' and receiver_state = '"+state+"')");

            searchable.or(customFilter1, customFilter2);
        	break;

		default:
			throw new BusinessException("消息状态["+state+"]不存在！");
		}
	}
	
	
	/**
	 * 统计用户收件箱未读消息数
	 * @param userId
	 * @return
	 */
	public long countUnread(String userId){
		Searchable searchable=new SearchRequest()
				.addSearchFilter("receiver_id", SearchOperator.eq, userId)
				.addSearchFilter("is_read",SearchOperator.eq,false);
		
		int count =this.findAllWithNoPageNoSort(searchable).size();		
		return count;
	}
	
	/**
	 * 发送消息
	 * @param message
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public void send(PersonalMessage message){
		Date now = new Date();
        message.setSendDate(now);
        message.setSenderState(MessageState.out_box);
        message.setSenderStateChangeDate(now);
        message.setReceiverState(MessageState.in_box);
        message.setReceiverStateChangeDate(now);
        
        if (StringUtils.isNotEmpty(message.getParentId())) {
        	PersonalMessage parent = this.findOne(message.getParentId()).getData();
            if (parent != null) {
                message.setParentIds(parent.makeSelfAsParentIds());
            }
        }
        
        //判读消息是新消息，还是发送草稿的消息
        if(StringUtils.isNotEmpty(message.getId())){
        	this.updateSelectiveById(message);
        }else{
        	this.saveSelective(message);
        }        
        pushApi.pushUnreadMessage(message.getReceiverId(), countUnread(message.getReceiverId()));
	}
	
	/**
	 * 标记为已读
	 * @param message
	 */
	@Override
	public BaseResult markRead(PersonalMessage message){
		if(message==null || StringUtils.isEmpty(message.getId())){
			throw new BusinessException("参数错误");
		}
		
		if(Boolean.TRUE.equals(message.getIsRead())){
			return new BaseResult();
		}
		
		message.setIsRead(true);
		return this.updateSelectiveById(message);
	}
	
	/**
	 * 批量标记为已读
	 * @param userId
	 * @param ids
	 */
	public BaseResult markRead(String userId, String[] ids){
		if(StringUtils.isEmpty(userId) || ArrayUtils.isEmpty(ids)){
			throw new BusinessException("参数错误");
		}		
		personalMessageDao.markRead(userId, ids);
		
		return new BaseResult();		
	}
	
	/**
     * 标识为已回复
     *
     * @param message
     */
    public BaseResult markReplied(PersonalMessage message){
    	if(message==null || StringUtils.isEmpty(message.getId())){
			throw new BusinessException("参数错误");
		}
		
		if(Boolean.TRUE.equals(message.getIsReplied())){
			return new BaseResult();
		}
		
		message.setIsReplied(true);
		return this.updateSelectiveById(message);
    }
	
	/**
	 * 查询消息的祖先 和 后代
	 * @param message
	 * @return
	 */
	public List<PersonalMessage> findAncestorsAndDescendants(PersonalMessage message){
		Searchable searchable = Searchable.newSearchable();

        searchable.addSort(Sort.Direction.ASC, "id");

        SearchFilter filter = null;
        //祖先 不为空 从祖先查起
        if (!StringUtils.isEmpty(message.getParentIds())) {
            String ancestorsId = message.getParentIds().split("/")[0];
            filter = SearchFilterHelper.or(
                    SearchFilterHelper.newCondition("parent_ids", SearchOperator.prefixLike, ancestorsId + "/"),
                    SearchFilterHelper.newCondition("id", SearchOperator.eq, ancestorsId)
            );
        } else {
            //祖先为空 查自己的后代
            String descendantsParentIds = message.makeSelfAsParentIds();
            filter = SearchFilterHelper.newCondition("parent_ids", SearchOperator.prefixLike, descendantsParentIds);
        }

        searchable.addSearchFilter(filter);
        List<PersonalMessage> result = this.findAllWithSort(searchable);
        //把自己排除
        result.remove(message);

        //删除 不可见的消息 如垃圾箱/已删除
        for (int i = result.size() - 1; i >= 0; i--) {
        	PersonalMessage m = result.get(i);

            if (m.getSenderId() == message.getSenderId() &&
                    (m.getSenderState() == MessageState.trash_box || m.getSenderState() == MessageState.delete_box)) {
                result.remove(i);
            }

            if (m.getReceiverId() == message.getSenderId() &&
                    (m.getSenderState() == MessageState.trash_box || m.getSenderState() == MessageState.delete_box)) {
                result.remove(i);
            }
        }

        return result;
	}

	 /**
     * 保存草稿
     *
     * @param message
     */
    public BaseResult saveDraft(PersonalMessage message){
    	message.setSenderState(MessageState.draft_box);
    	Date now = new Date();
        message.setSendDate(now);
        message.setSenderStateChangeDate(now);
        message.setReceiverStateChangeDate(now);
    	   	
        message.setReceiverState(null);
        
        //更改草稿箱的数据
        if(StringUtils.isNotEmpty(message.getId())){
        	return this.updateSelectiveById(message);
        }   
        return this.saveSelective(message);
    }
    
    
    /**
     * 将消息保存到收藏箱
     *
     * @param userId
     * @param messageId
     * @return
     */
    public void store(String userId, String messageId){
    	this.changeState(userId, messageId, MessageState.store_box);
    }
    
    /**
     * 批量将消息保存到收藏箱
     *
     * @param userId
     * @param messageIds
     * @return
     */
    public void store(String userId, String[] messageIds){
    	for(String messageId:messageIds){
    		this.store(userId, messageId);
    	}
    }
    
    /**
     * 将消息移动到垃圾箱
     *
     * @param userId
     * @param messageId
     * @return
     */
    public void recycle(String userId, String messageId){
    	this.changeState(userId, messageId, MessageState.trash_box);
    }

    /**
     * 批量将消息移动到垃圾箱
     *
     * @param userId
     * @param messageIds
     * @return
     */
    public void recycle(String userId, String[] messageIds){
    	for(String messageId:messageIds){
    		this.recycle(userId, messageId);
    	}
    }
    
    
    /**
     * 从垃圾箱删除消息
     *
     * @param userId
     * @param messageId
     */
    public void delete(String userId, String messageId){
    	this.changeState(userId, messageId, MessageState.delete_box);
    }

    /**
     * 批量从垃圾箱删除消息
     *
     * @param userId
     * @param messageIds
     */
    public void delete(String userId, String[] messageIds){
    	for(String messageId:messageIds){
    		this.delete(userId, messageId);
    	}
    }
    
    
    /**
     * 清空指定状态的消息
     *
     * @param userId
     * @param state
     */
    public void clearBox(String userId, MessageState state){
    	switch (state) {
        case draft_box:
            clearBox(userId, MessageState.draft_box, MessageState.trash_box);
            break;
        case in_box:
            clearBox(userId, MessageState.in_box, MessageState.trash_box);
            break;
        case out_box:
            clearBox(userId, MessageState.out_box, MessageState.trash_box);
            break;
        case store_box:
            clearBox(userId, MessageState.store_box, MessageState.trash_box);
            break;
        case trash_box:
            clearBox(userId, MessageState.trash_box, MessageState.delete_box);
            break;
        default:
            //none;
            break;
    	}
    }
    	
    	
    private void clearBox(String userId, MessageState oldState, MessageState newState){
    	List<PersonalMessage> list= this.findUserMessage(userId, oldState);
    	
    	for(PersonalMessage message:list){
    		this.changeState(userId, message, newState);
    	}   	
    }

    /**
     * 清空草稿箱
     *
     * @param userId
     */
    public void clearDraftBox(String userId){
    	this.clearBox(userId, MessageState.draft_box);
    }

    /**
     * 清空收件箱
     *
     * @param userId
     */
    public void clearInBox(String userId){
    	this.clearBox(userId, MessageState.in_box);
    }

    /**
     * 清空收件箱
     *
     * @param userId
     */
    public void clearOutBox(String userId){
    	this.clearBox(userId, MessageState.out_box);
    }

    /**
     * 清空收藏箱
     *
     * @param userId
     */
    public void clearStoreBox(String userId){
    	this.clearBox(userId, MessageState.store_box);
    }

    /**
     * 清空垃圾箱
     *
     * @param userId
     */
    public void clearTrashBox(String userId){
    	this.clearBox(userId, MessageState.trash_box);
    }
    
    
    /**
     * 发送系统消息给多个人
     *
     * @param receiverIds
     * @param message
     */
    public void sendSystemMessage(String[] receiverIds, PersonalMessage message){
    	
    }

    /**
     * 发送系统消息给所有人
     *
     * @param message
     */
    public void sendSystemMessageToAllUser(PersonalMessage message){
    	
    }
    
    
    
    
    
    private void changeSenderState(PersonalMessage message, MessageState state) {
        message.setSenderState(state);
        message.setSenderStateChangeDate(new Date());
    }

    private void changeReceiverState(PersonalMessage message, MessageState state) {
        message.setReceiverState(state);
        message.setReceiverStateChangeDate(new Date());
    }
    
    
    /**
     * 变更状态
     * 根据用户id是收件人/发件人 决定更改哪个状态
     *
     * @param userId
     * @param messageId
     * @param state
     */
    private void changeState(String userId, String messageId, MessageState state) {
    	PersonalMessage message = this.findOne(messageId).getData();
        this.changeState(userId, message, state);
    }
    
    private void changeState(String userId, PersonalMessage message, MessageState state) {
        if (message == null) {
            return;
        }
        if (userId.equals(message.getSenderId())) {
            changeSenderState(message, state);
        } else {
            changeReceiverState(message, state);
        }
        this.updateSelectiveById(message);
    }
    
    /**
     * 更改状态
     *
     * @param oldStates
     * @param newState
     * @param expireDays 当前时间-过期天数 时间之前的消息将改变状态
     */
    public Integer changeState(ArrayList<MessageState> oldStates, MessageState newState, int expireDays){
    	Date changeDate = new Date();
        Integer count = personalMessageDao.changeSenderState(oldStates, newState, changeDate, DateUtils.addDays(changeDate, -expireDays));
        count += personalMessageDao.changeReceiverState(oldStates, newState, changeDate, DateUtils.addDays(changeDate, -expireDays));
        return count;
    }
    
    /**
     * 物理删除那些已删除的（即收件人和发件人 同时都删除了的）
     *
     * @param deletedState
     */
    public Integer clearDeletedMessage(MessageState deletedState){
    	return personalMessageDao.clearDeletedMessage(deletedState);
    }
}
