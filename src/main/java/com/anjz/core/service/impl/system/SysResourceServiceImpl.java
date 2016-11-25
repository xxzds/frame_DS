package com.anjz.core.service.impl.system;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.stereotype.Service;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.base.service.BaseTreeableServiceImpl;
import com.anjz.core.dao.SysResourceDao;
import com.anjz.core.model.SysResource;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysAuthService;
import com.anjz.core.service.intf.system.SysResourceService;
import com.anjz.core.vo.Menu;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年9月2日上午11:44:26
 */
@Service
public class SysResourceServiceImpl extends BaseTreeableServiceImpl<SysResource, String> implements SysResourceService {
	
	@Resource
	private SysResourceDao sysResourceDao;
	
	@Resource
	private SysAuthService sysAuthService;

	
	/**
	 * 获取用户的菜单集合
	 * @param user
	 * @return
	 */
	@Override
	public ListResult<Menu> findMenus(SysUser user) {
		ListResult<Menu> result = new ListResult<Menu>();
				
		Searchable searchable =Searchable.newSearchable()
                        .addSearchFilter("is_show", SearchOperator.eq, "1")
                        .addSort(new Sort(Sort.Direction.DESC, "parent_id", "weight"));		
		List<SysResource> resources = findAllWithSort(searchable);
		
		//获取当前用户的所有权限字符串
		Set<String> userPermissions = sysAuthService.findStringPermissions(user).getData();
		Iterator<SysResource> iter = resources.iterator();
        while (iter.hasNext()) {
            if (!hasPermission(iter.next(), userPermissions)) {
                iter.remove();
            }
        }       
        result.setData(convertToMenus(resources));
		return result;
	}
	
	
	private boolean hasPermission(SysResource resource, Set<String> userPermissions) {
        String actualResourceIdentity = findActualResourceIdentity(resource).getData();
        if (StringUtils.isEmpty(actualResourceIdentity)) {
            return true;
        }

        for (String permission : userPermissions) {
            if (hasPermission(permission, actualResourceIdentity)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPermission(String permission, String actualResourceIdentity) {

        //得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
        String permissionResourceIdentity = permission.substring(0, permission.lastIndexOf(":"));

        //如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
        if(permissionResourceIdentity.startsWith(actualResourceIdentity)) {
            return true;
        }

        //模式匹配
        WildcardPermission p1 = new WildcardPermission(permission);
        WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);

        return p1.implies(p2) || p2.implies(p1);
    }
    
    
    
    @SuppressWarnings("unchecked")
	public static List<Menu> convertToMenus(List<SysResource> resources) {

        if (resources.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        //获取根节点
        Menu root=null;
        for(int i=0;i<resources.size();i++){
        	SysResource sysResource=resources.get(i);
        	if("0".equals(sysResource.getParentId())){
        		 root = convertToMenu(resources.remove(i));
        		 break;
        	}
        }

        recursiveMenu(root, resources);
        List<Menu> menus = root.getChildren();
        removeNoLeafMenu(menus);

        return menus;
    }
    
    private static Menu convertToMenu(SysResource resource) {
        return new Menu(resource.getId(), resource.getName(), resource.getIcon(), resource.getUrl());
    }
    
    private static void recursiveMenu(Menu menu, List<SysResource> resources) {
        for (int i = resources.size() - 1; i >= 0; i--) {
        	SysResource resource = resources.get(i);
            if (resource.getParentId().equals(menu.getId())) {
                menu.getChildren().add(convertToMenu(resource));
                resources.remove(i);
            }
        }

        for (Menu subMenu : menu.getChildren()) {
            recursiveMenu(subMenu, resources);
        }
    }
    
    private static void removeNoLeafMenu(List<Menu> menus) {
        if (menus.size() == 0) {
            return;
        }
        for (int i = menus.size() - 1; i >= 0; i--) {
            Menu m = menus.get(i);
            removeNoLeafMenu(m.getChildren());
            
            //移除掉没有孩子节点和url的菜单
            if (!m.isHasChildren() && StringUtils.isEmpty(m.getUrl())) {
                menus.remove(i);
            }
        }
    }
    

    /**
	  * 得到真实的资源标识  即 父亲:儿子
	  * @param resource
	  * @return
	  */
	@Override
	public PlainResult<String> findActualResourceIdentity(SysResource resource) {
		PlainResult<String> result = new PlainResult<String>();
		if (resource == null) {
			return result;
		}

		StringBuilder s = new StringBuilder(resource.getIdentity()==null?"":resource.getIdentity());

		boolean hasResourceIdentity = !StringUtils.isEmpty(resource.getIdentity());

		SysResource parent = this.findOne(resource.getParentId()).getData();
		while (parent != null) {
			if (!StringUtils.isEmpty(parent.getIdentity())) {
				s.insert(0, parent.getIdentity() + ":");
				hasResourceIdentity = true;
			}
			parent = findOne(parent.getParentId()).getData();
		}

		// 如果用户没有声明 资源标识 且父也没有，那么就为空
		if (!hasResourceIdentity) {
			result.setData("");
			return result;
		}

		// 如果最后一个字符是: 因为不需要，所以删除之
		int length = s.length();
		if (length > 0 && s.lastIndexOf(":") == length - 1) {
			s.deleteCharAt(length - 1);
		}

		// 如果有儿子 最后拼一个*	
		Searchable searchable=new SearchRequest()
				.addSearchFilter("parent_id", SearchOperator.eq, resource.getId());		
		Long count= this.count(searchable);
		if(count>0){
			s.append(":*");
		}

		result.setData(s.toString());
		return result;
	}

}
