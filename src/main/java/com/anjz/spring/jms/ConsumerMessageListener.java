package com.anjz.spring.jms;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息监听器
 * @author shuai.ding
 * @date 2017年3月21日下午4:59:44
 */
public class ConsumerMessageListener implements MessageListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerMessageListener.class);
	
	@Override
	public void onMessage(Message message) {
		LOGGER.info("**************************************************************************************************");
		if(message instanceof BytesMessage){
			BytesMessage bytesMessage = (BytesMessage) message;
			LOGGER.info(bytesMessage.toString());
		}else if(message instanceof MapMessage){
			MapMessage mapMessage = (MapMessage) message;
			LOGGER.info(mapMessage.toString());
		}else if(message instanceof ObjectMessage){
			ObjectMessage objectMessage = (ObjectMessage) message;
			LOGGER.info(objectMessage.toString());
		}else if(message instanceof StreamMessage){
			StreamMessage streamMessage = (StreamMessage) message;
			LOGGER.info(streamMessage.toString());
		}else if(message instanceof TextMessage){
			TextMessage textMsg = (TextMessage) message;  
			LOGGER.info(textMsg.toString());
		}else{
			LOGGER.error("message is not in[BytesMessage,MapMessage,ObjectMessage,StreamMessage,TextMessage]");
		}
		LOGGER.info("**************************************************************************************************");
	}

}
