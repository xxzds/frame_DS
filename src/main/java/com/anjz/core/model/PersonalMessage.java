package com.anjz.core.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.MessageState;
import com.anjz.core.enums.MessageType;

/**
 * @author ding.shuai
 * @date 2016年9月19日下午12:36:24
 */
public class PersonalMessage extends BaseEntity {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 消息发送者Id
	 */
	private java.lang.String senderId;
	/**
	 * 消息接收者id
	 */
	private java.lang.String receiverId;
	/**
	 * 消息发送时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date sendDate;
	/**
	 * 标题
	 */
	private java.lang.String title;
	/**
	 * 内容
	 */
	private java.lang.String content;
	/**
	 * 发件人的消息状态
	 */
	private MessageState senderState;
	/**
	 * 发件人的消息状态改变时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date senderStateChangeDate;
	/**
	 * 收件人的消息状态
	 */
	private MessageState receiverState;
	/**
	 * 收件人的消息状态的改变时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date receiverStateChangeDate;
	/**
	 * 消息类型
	 */
	private MessageType type;
	/**
	 * 是否已读
	 */
	private java.lang.Boolean isRead;
	/**
	 * 是否已回复
	 */
	private java.lang.Boolean isReplied;
	/**
	 * 父编号
	 */
	private java.lang.String parentId;
	/**
	 * 父消息编号列表
	 */
	private java.lang.String parentIds;
	
	public void setSenderId(java.lang.String senderId) {
		this.senderId = senderId;
	}

	public java.lang.String getSenderId() {
		return this.senderId;
	}
	public void setReceiverId(java.lang.String receiverId) {
		this.receiverId = receiverId;
	}

	public java.lang.String getReceiverId() {
		return this.receiverId;
	}
	public void setSendDate(java.util.Date sendDate) {
		this.sendDate = sendDate;
	}

	public java.util.Date getSendDate() {
		return this.sendDate;
	}
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getTitle() {
		return this.title;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.String getContent() {
		return this.content;
	}
	public void setSenderState(MessageState senderState) {
		this.senderState = senderState;
	}

	public MessageState getSenderState() {
		return this.senderState;
	}
	public void setSenderStateChangeDate(java.util.Date senderStateChangeDate) {
		this.senderStateChangeDate = senderStateChangeDate;
	}

	public java.util.Date getSenderStateChangeDate() {
		return this.senderStateChangeDate;
	}
	public void setReceiverState(MessageState receiverState) {
		this.receiverState = receiverState;
	}

	public MessageState getReceiverState() {
		return this.receiverState;
	}
	public void setReceiverStateChangeDate(java.util.Date receiverStateChangeDate) {
		this.receiverStateChangeDate = receiverStateChangeDate;
	}

	public java.util.Date getReceiverStateChangeDate() {
		return this.receiverStateChangeDate;
	}
	public void setType(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return this.type;
	}
	public void setIsRead(java.lang.Boolean isRead) {
		this.isRead = isRead;
	}

	public java.lang.Boolean getIsRead() {
		return this.isRead;
	}
	public void setIsReplied(java.lang.Boolean isReplied) {
		this.isReplied = isReplied;
	}

	public java.lang.Boolean getIsReplied() {
		return this.isReplied;
	}
	public void setParentId(java.lang.String parentId) {
		this.parentId = parentId;
	}

	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setParentIds(java.lang.String parentIds) {
		this.parentIds = parentIds;
	}

	public java.lang.String getParentIds() {
		return this.parentIds;
	}
	
	public String makeSelfAsParentIds() {
        return (getParentIds() != null ? getParentIds() : "") + getId() + "/";
    }


}