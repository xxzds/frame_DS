package com.anjz.job;

import javax.annotation.Resource;

import com.anjz.core.enums.MessageState;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.google.common.collect.Lists;

/**
 * 消息的定时清除任务
 * @author ding.shuai
 * @date 2016年9月20日下午7:06:18
 */
public class MessageClearJob implements BaseJob {

	public static final int EXPIRE_DAYS_OF_ONE_YEAR = 366;
	public static final int EXPIRE_DAYS_OF_ONE_MONTH = 31;

	@Resource
	private PersonalMessageService personalMessageService;

	@Override
	public void run() {
		// 1、收件箱、发件箱状态修改为垃圾箱状态
		this.doClearInOrOutBox();
		// 2、垃圾箱状态改为已删除状态
		this.doClearTrashBox();
		// 3、物理删除那些已删除的（即收件人和发件人 同时都删除了的）
		this.doClearDeletedMessage();
	}

	private void doClearInOrOutBox() {
		personalMessageService.changeState(Lists.newArrayList(MessageState.in_box, MessageState.out_box),
				MessageState.trash_box, EXPIRE_DAYS_OF_ONE_YEAR);

	}

	private void doClearTrashBox() {
		personalMessageService.changeState(Lists.newArrayList(MessageState.trash_box), MessageState.delete_box,
				EXPIRE_DAYS_OF_ONE_MONTH);
	}
	
	private void doClearDeletedMessage() {
		personalMessageService.clearDeletedMessage(MessageState.delete_box);
	}

}
