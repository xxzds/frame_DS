package com.anjz.personal;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.dao.PersonalMessageDao;
import com.anjz.core.enums.MessageState;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.google.common.collect.Lists;

/**
 * 
 * @author ding.shuai
 * @date 2016年9月20日下午12:27:26
 */
public class MessageTest extends BaseTest {

	@Resource
	private PersonalMessageService personalMessageService;

	@Resource
	private PersonalMessageDao messageDao;

	@Test
	public void markReadTest() {
		messageDao.markRead("aaaa", new String[] { "1", "2" });
	}

	@Test
	public  void doClearInOrOutBoxTest() {
		personalMessageService.changeState(Lists.newArrayList(MessageState.in_box, MessageState.out_box),
				MessageState.trash_box, 366);

	}
	
	@Test
	public void doClearTrashBoxTest() {
		personalMessageService.changeState(Lists.newArrayList(MessageState.trash_box), MessageState.delete_box,
				31);
	}
	

	@Test
	public void doClearDeletedMessage() {
		personalMessageService.clearDeletedMessage(MessageState.delete_box);
	}
}
