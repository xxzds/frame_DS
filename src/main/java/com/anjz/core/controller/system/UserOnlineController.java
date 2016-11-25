package com.anjz.core.controller.system;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.anjz.base.controller.BaseController;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.base.entity.search.pageandsort.Sort.Direction;
import com.anjz.base.entity.search.pageandsort.Sort.Order;
import com.anjz.core.model.UserOnline;
import com.anjz.shiro.session.MySession;
import com.anjz.util.DateUtil;
import com.anjz.util.ReflectionUtils;
import com.google.common.collect.Lists;


/**
 * 用户在线列表
 * @author ding.shuai
 * @date 2016年9月12日下午4:28:03
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/system/user/online")
public class UserOnlineController extends BaseController{
	
	private static final Logger logger=LoggerFactory.getLogger(UserOnlineController.class);
	
	protected PermissionList permissionList = null;
		
	//因session由shiro管理，所以此处注入shiro的缓存管理
	@Resource
	private CacheManager shiroCacheManager;
	
	@Resource
	private SessionDAO sessionDAO;
	
	
	public UserOnlineController() {
		setResourceIdentity("system:userOnline");
		
		//新增权限 - 强制退出
		permissionList.addPermission("forcedout");
	}
	
	/**
	 * 权限前缀：如sys:user 则生成的新增权限为 sys:user:create
	 */
	public void setResourceIdentity(String resourceIdentity) {
		if (!StringUtils.isEmpty(resourceIdentity)) {
			permissionList = PermissionList.newPermissionList(resourceIdentity);
		}
	}	

	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	public String list(Searchable searchable, Model model) throws IllegalAccessException, InvocationTargetException {
		if (permissionList != null) {
			this.permissionList.assertHasViewPermission();
		}
		
		Cache<Object,Object> sessionCache= shiroCacheManager.getCache("shiro-activeSessionCache");
		Set<?> keys= sessionCache.keys();
		
		List<UserOnline> userOnlines=Lists.newArrayList();
		for(Object key:keys){
			Object value =  sessionCache.get(key);
			if(value instanceof SimpleSession){
				MySession session=(MySession)value;
				
				//查询条件
				Object userNameObj= searchable.getValue("userName|like");
				String userName = userNameObj==null?"":(String)userNameObj;
				
				if(StringUtils.isNotEmpty(session.getUserId()) && StringUtils.isNotEmpty(session.getUserName())){
					
					if(session.getUserName().contains(userName)){
						UserOnline userOnline=new UserOnline();										
						BeanUtils.copyProperties(userOnline, session);					
						userOnlines.add(userOnline);
					}	
				}
				
				
				logger.info("id:"+session.getId()+"\n"+
						"host:"+session.getHost()+"\n"+
						"timeout:"+session.getTimeout()+"\n"+
						"lastAccessTime:"+DateUtil.formatDate(session.getLastAccessTime())+"\n"+
						"startTimestamp:"+DateUtil.formatDate(session.getStartTimestamp())+"\n"+
						"stopTimestamp:"+DateUtil.formatDate(session.getStopTimestamp())+"\n"+
						"userId:"+session.getUserId()+"\n"+
						"userName:"+session.getUserName()+"\n"+
						"userAgent:"+session.getUserAgent()+"\n"+
						"systemHost:"+ session.getSystemHost()+"\n"+
						"status:"+session.getStatus().getInfo());
			}
			
		}	
		
		//分页
		int offset= searchable.getPage().getOffset();
		int pageSize=searchable.getPage().getPageSize();	
		
		List<UserOnline> showUserOnlines=Lists.newArrayList();
		for(int i=0;i<userOnlines.size();i++){
			 if(i>=offset && i<offset+pageSize+1){
				 showUserOnlines.add(userOnlines.get(i));
			 }
		}
		
		//排序
		Sort sort = searchable.getSort();
		if(sort != null){
			List<Order> orders= sort.getOrders();
			
			if(!orders.isEmpty()){
				for(Order order:orders){
					final String property = order.getProperty();
					final Direction direction = order.getDirection();
					
					Collections.sort(showUserOnlines, new Comparator<UserOnline>() {

						@Override
						public int compare(UserOnline o1, UserOnline o2) {
							
							String value1 = (String)ReflectionUtils.getFieldValue(o1, property);
							String value2 = (String)ReflectionUtils.getFieldValue(o2, property);
													
							int compareResult = value1.compareTo(value2);
							return direction == Direction.DESC ? compareResult:-compareResult;
						}					
					});	
				}
				
			}
		}		
		
		Page<UserOnline> page=new PageImpl<UserOnline>(showUserOnlines, searchable.getPage(), userOnlines.size());
		
		model.addAttribute("page", page);
		return viewName("list");
	}
	
	
	/**
     * 仅返回表格数据
     *
     * @param searchable
     * @param model
     * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
     */
    @RequestMapping(value = { "", "list" },method = RequestMethod.GET, headers = "table=true")
    public String listTable(Searchable searchable, Model model) throws IllegalAccessException, InvocationTargetException {
        list(searchable, model);
        return viewName("listTable");
    }
    
    
    @RequestMapping("/forceLogout")
    public String forceLogout(@RequestParam(value = "ids") String[] ids){
    	permissionList.assertHasPermission("forcedout");
   	
    	Cache<Object,Object> sessionCache= shiroCacheManager.getCache("shiro-activeSessionCache");
    	   	
    	for(String id:ids){
    		Object object = sessionCache.get(id);
    		if(object!=null){
    			MySession session = (MySession)object;
    			session.setStatus(MySession.OnlineStatus.force_logout);
    			//同步session
    			sessionDAO.update(session);
    		}   		
    	}
    	
    	return redirectToUrl(null);
    }
}
