package com.anjz.jms;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.anjz.BaseTest;

/**
 * jms 测试
 * http://120.25.210.34:8161/admin/queues.jsp
 * @author shuai.ding
 * @date 2017年3月21日下午4:00:13
 */
public class JmsTest extends BaseTest{
	
	@Resource
	private JmsTemplate jmsTemplate;
	
	@Resource
	private ActiveMQQueue activeMQQueue;
	
	
	/**
	 * 生产者
	 */
	@Test
	public void pruducterTest(){
		try{
			jmsTemplate.send(activeMQQueue, new MessageCreator(){

				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage("6666");
					
//					return session.createObjectMessage(object)
				}
				
			});
		}catch(Exception e){
			logger.error("exception",e);
		}
		
	}
	
	/**
	 * 消费者
	 * @throws JMSException
	 */
	@Test
	public void consumerTest() throws JMSException{
		TextMessage textMessage = (TextMessage)jmsTemplate.receive(activeMQQueue);
		logger.info(textMessage.getText());
	}
}
