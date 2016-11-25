package com.anjz.base.extra.taglib;

import java.util.Iterator;

import com.anjz.core.model.SysJob;
import com.anjz.core.model.SysOrganization;
import com.anjz.core.model.SysPermission;
import com.anjz.core.model.SysResource;
import com.anjz.core.model.SysRole;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysJobService;
import com.anjz.core.service.intf.system.SysOrganizationService;
import com.anjz.core.service.intf.system.SysPermissionService;
import com.anjz.core.service.intf.system.SysResourceService;
import com.anjz.core.service.intf.system.SysRoleService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.spring.SpringUtils;

/**
 * el表达式中对应的函数
 * es-functions.tld
 * @author ding.shuai
 * @date 2016年9月4日下午4:18:46
 */
public class EsFunctions {

	/**
	 * 判断obj是否在iterable中
	 * 
	 * @param iterable
	 * @param obj
	 * @return
	 */
	public static boolean in(Iterable<?> iterable, Object obj) {
		if (iterable == null) {
			return false;
		}
		Iterator<?> iterator = iterable.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().equals(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断指定id的组织机构是否存在
	 * 
	 * @param id
	 * @param onlyDisplayShow
	 *            是否仅显示可见的
	 * @return
	 */
	public static boolean existsOrganization(String id, Boolean onlyDisplayShow) {
		SysOrganization organization = SpringUtils.getBean(SysOrganizationService.class).findOne(id).getData();
		if (organization == null) {
			return false;
		}
		if (Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(organization.getIsShow())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断指定id的工作职务是否存在
	 * 
	 * @param id
	 * @param onlyDisplayShow
	 *            是否仅显示可见的
	 * @return
	 */
	public static boolean existsJob(String id, Boolean onlyDisplayShow) {
		SysJob job = SpringUtils.getBean(SysJobService.class).findOne(id).getData();
		if (job == null) {
			return false;
		}
		if (Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(job.getIsShow())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断指定id的资源是否存在
	 * 
	 * @param id
	 * @param onlyDisplayShow
	 *            是否仅显示可见的
	 * @return
	 */
	public static boolean existsResource(String id, Boolean onlyDisplayShow) {
		SysResource resource = SpringUtils.getBean(SysResourceService.class).findOne(id).getData();
		if (resource == null) {
			return false;
		}
		if (Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(resource.getIsShow())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断指定id的权限是否存在
	 * 
	 * @param id
	 * @param onlyDisplayShow
	 * @return
	 */
	public static boolean existsPermission(String id, Boolean onlyDisplayShow) {
		SysPermission permission = SpringUtils.getBean(SysPermissionService.class).findOne(id).getData();
		if (permission == null) {
			return false;
		}
		if (Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(permission.getIsShow())) {
			return false;
		}
		return true;
	}

	
	/**
	 * 判断指定id的角色是否存在
	 * @param id
	 * @param onlyDisplayShow 是否仅显示可见的
	 * @return
	 */
	public static boolean existsRole(String id, Boolean onlyDisplayShow) {
		SysRole role = SpringUtils.getBean(SysRoleService.class).findOne(id).getData();
		if (role == null) {
			return false;
		}
		if (Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(role.getIsShow())) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 通过用户Id查询用户名称
	 * @param userId
	 * @return
	 */
	public static String findUserNameById(String userId){
		SysUser user=SpringUtils.getBean(SysUserService.class).findOne(userId).getData();
		if(user==null){
			return "";
		}
		return user.getUserName();		
	}

}
