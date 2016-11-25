package com.anjz.shiro.filter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;

import com.anjz.base.Constants;
import com.anjz.core.model.SysUser;
import com.anjz.shiro.session.MySession;

/**
 * session过滤器，向session中存入userId和userName
 * 以及强制退出的控制
 * 
 * @author ding.shuai
 * @date 2016年9月12日下午5:55:38
 */
public class OnlineSessionFilter extends AdviceFilter {
	
//	private static final Logger log = LoggerFactory.getLogger(OnlineSessionFilter.class);
	/**
     * 强制退出后重定向的地址
     */
    private String forceLogoutUrl;
    
	@Resource
	private SessionDAO sessionDao;
	
	public String getForceLogoutUrl() {
        return forceLogoutUrl;
    }

    public void setForceLogoutUrl(String forceLogoutUrl) {
        this.forceLogoutUrl = forceLogoutUrl;
    }
    
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    	Subject subject = SecurityUtils.getSubject();
		if (subject == null || subject.getSession() == null) {
			return true;
		}
		
		//说明：不能用subject.getSession()获取session，因为获取的是个代理对象，
		//解决方法，从缓存中获取
		// org.apache.shiro.subject.support.DelegatingSubject$StoppingAwareProxiedSession@3078f2d0
		// Session session=subject.getSession();
		
		Session session=null;
		try{
		    session=sessionDao.readSession(subject.getSession().getId());
		}catch(UnknownSessionException e){
			return true;
		}

		if(session instanceof MySession){
			MySession mySession=(MySession)session;
			
			//强制退出
			if(mySession.getStatus() == MySession.OnlineStatus.force_logout){
				
				//说明：在这清除登录内容，？？forceLogoutUrl重定向的参数传递有问题
//				try {
//		            subject.logout();
//		        } catch (SessionException ise) {
//		            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
//		        }		
				WebUtils.issueRedirect(request, response, getForceLogoutUrl());
				return false;
			}
			
			SysUser user=(SysUser)request.getAttribute(Constants.CURRENT_USER);
			if(user  != null && StringUtils.isEmpty(mySession.getUserId())){
				mySession.setUserId(user.getId());
				mySession.setUserName(user.getUserName());
				//同步session
				sessionDao.update(session);
			}		
		}
		return true;
    }
}
