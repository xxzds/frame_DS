package com.anjz.core.service.intf.system;

import com.anjz.base.service.BaseTreeableService;
import com.anjz.core.model.SysResource;
import com.anjz.core.model.SysUser;
import com.anjz.core.vo.Menu;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年8月24日上午9:38:33
 */
public interface SysResourceService extends BaseTreeableService<SysResource, String>{

	/**
	 * 获取用户的菜单集合
	 * @param user
	 * @return
	 */
	 public ListResult<Menu> findMenus(SysUser user);
	 
	 /**
	  * 得到真实的资源标识  即 父亲:儿子
	  * @param resource
	  * @return
	  */
	 public PlainResult<String> findActualResourceIdentity(SysResource resource);
}
