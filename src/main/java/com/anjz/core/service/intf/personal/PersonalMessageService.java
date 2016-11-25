package com.anjz.core.service.intf.personal;

import java.util.ArrayList;
import java.util.List;

import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.service.BaseService;
import com.anjz.core.enums.MessageState;
import com.anjz.core.model.PersonalMessage;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年9月18日下午12:12:46
 */
public interface PersonalMessageService extends BaseService<PersonalMessage, String>{

	/**
	 * 通过用户id和状态查询消息列表
	 * @param userId
	 * @param state
	 * @param pageable
	 * @return
	 */
	public Page<PersonalMessage> findUserMessage(String userId, MessageState state, Pageable pageable);
	
	public List<PersonalMessage> findUserMessage(String userId, MessageState state);
	
	
	/**
	 * 统计用户收件箱未读消息数
	 * @param userId
	 * @return
	 */
	public long countUnread(String userId);
	
	
	/**
	 * 发送消息
	 * @param message
	 */
	public void send(PersonalMessage message);
	
	
	/**
	 * 标记为已读
	 * @param message
	 */
	public BaseResult markRead(PersonalMessage message);
	
	
	/**
	 * 批量标记为已读
	 * @param userId
	 * @param ids
	 */
	public BaseResult markRead(String userId, String[] ids);
	
	 /**
     * 标识为已回复
     *
     * @param message
     */
    public BaseResult markReplied(PersonalMessage message);
	
	
	/**
	 * 查询消息的祖先 和 后代
	 * @param message
	 * @return
	 */
	public List<PersonalMessage> findAncestorsAndDescendants(PersonalMessage message);
	
	/**
     * 保存草稿
     *
     * @param message
     */
    public BaseResult saveDraft(PersonalMessage message);
    
    /**
     * 将消息保存到收藏箱
     *
     * @param userId
     * @param messageId
     * @return
     */
    public void store(String userId, String messageId);
    
    /**
     * 批量将消息保存到收藏箱
     *
     * @param userId
     * @param messageIds
     * @return
     */
    public void store(String userId, String[] messageIds);
    
    
    /**
     * 将消息移动到垃圾箱
     *
     * @param userId
     * @param messageId
     * @return
     */
    public void recycle(String userId, String messageId);

    /**
     * 批量将消息移动到垃圾箱
     *
     * @param userId
     * @param messageIds
     * @return
     */
    public void recycle(String userId, String[] messageIds);
    
    /**
     * 从垃圾箱删除消息
     *
     * @param userId
     * @param messageId
     */
    public void delete(String userId, String messageId);

    /**
     * 批量从垃圾箱删除消息
     *
     * @param userId
     * @param messageIds
     */
    public void delete(String userId, String[] messageIds);
    
    /**
     * 清空指定状态的消息
     *
     * @param userId
     * @param state
     */
    public void clearBox(String userId, MessageState state);

    /**
     * 清空草稿箱
     *
     * @param userId
     */
    public void clearDraftBox(String userId);

    /**
     * 清空收件箱
     *
     * @param userId
     */
    public void clearInBox(String userId);

    /**
     * 清空收件箱
     *
     * @param userId
     */
    public void clearOutBox(String userId);

    /**
     * 清空收藏箱
     *
     * @param userId
     */
    public void clearStoreBox(String userId);

    /**
     * 清空垃圾箱
     *
     * @param userId
     */
    public void clearTrashBox(String userId);
    
    /**
     * 发送系统消息给多个人(未实现)
     *
     * @param receiverIds
     * @param message
     */
    public void sendSystemMessage(String[] receiverIds, PersonalMessage message);

    /**
     * 发送系统消息给所有人(未实现)
     *
     * @param message
     */
    public void sendSystemMessageToAllUser(PersonalMessage message);
    
    
    /**
    * 更改状态
    *
    * @param oldStates
    * @param newState
    * @param expireDays 当前时间-过期天数 时间之前的消息将改变状态
    */
   public Integer changeState(ArrayList<MessageState> oldStates, MessageState newState, int expireDays);
   
   /**
    * 物理删除那些已删除的（即收件人和发件人 同时都删除了的）
    *
    * @param deletedState
    */
   public Integer clearDeletedMessage(MessageState deletedState);
}
