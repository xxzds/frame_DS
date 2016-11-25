package com.anjz.core.dao;

import java.util.ArrayList;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.anjz.base.dao.BaseDao;
import com.anjz.core.enums.MessageState;
import com.anjz.core.model.PersonalMessage;

public interface PersonalMessageDao extends BaseDao<PersonalMessage,String> {
	
	
	/**
	 * 批量标记为已读
	 * @param reciverId
	 * @param ids
	 * @return
	 */
	public int  markRead(@Param("reciverId")String reciverId,@Param("ids")String[] ids);
	
	
	/**
	 *  超过expireDate的时间且发送者的状态在【oldStates】的消息，更改状态为【newState】
	 * @param oldStates
	 * @param newState
	 * @param changeDate
	 * @param expireDate
	 * @return
	 */
	public int  changeSenderState(@Param("oldStates")ArrayList<MessageState> oldStates, @Param("newState")MessageState newState,
			@Param("changeDate")Date changeDate, @Param("expireDate")Date expireDate);
	
	/**
	 * 超过expireDate的时间且接收者的状态在【oldStates】的消息，更改状态为【newState】
	 * @param oldStates
	 * @param newState
	 * @param changeDate
	 * @param expireDate
	 * @return
	 */
	public int changeReceiverState(@Param("oldStates")ArrayList<MessageState> oldStates, @Param("newState")MessageState newState, 
			@Param("changeDate")Date changeDate, @Param("expireDate")Date expireDate);
	
	/**
	 * 物理删除那些已删除的（即收件人和发件人 同时都删除了的）
	 * @param deletedState
	 * @return
	 */
    public int clearDeletedMessage(MessageState deletedState);

}