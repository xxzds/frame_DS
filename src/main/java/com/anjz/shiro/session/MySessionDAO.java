package com.anjz.shiro.session;

import java.io.Serializable;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ding.shuai
 * @date 2016年9月12日上午11:51:39
 */
public class MySessionDAO extends CachingSessionDAO{
	
	private static final Logger logger=LoggerFactory.getLogger(MySessionDAO.class); 

	
	/**
	 * 获取session的缓存对象
	 * @return
	 */
	private Cache<Object,Object> getSessionCache(){
		return this.getCacheManager().getCache(this.getActiveSessionsCacheName());
	}
	
	@Override
	protected void doUpdate(Session session) {
		logger.info("doUpdate session【"+session.getId()+"】");	
		
		if(session!=null){
			this.getSessionCache().put(session.getId(), session);
		}		
	}

	@Override
	protected void doDelete(Session session) {
		Serializable sessionId=session.getId();
		logger.info("doDelete session【"+session.getId()+"】");	
		
		if(sessionId!=null){
			 this.getSessionCache().remove(session.getId());
		}		
	}

	@Override
	protected Serializable doCreate(Session session) {
		logger.info("doCreate session【"+session.getId()+"】");		
		Serializable sessionId = generateSessionId(session);
        ((MySession) session).setId(sessionId);
        return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		logger.info("doReadSession session【"+sessionId+"】");	
		if(sessionId!=null){
			Object value= this.getSessionCache().get(sessionId);
			if(value!=null || value instanceof Session){
				return (Session)value;
			}
		}			
		return null;
	}

}
