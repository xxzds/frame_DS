package com.anjz.sysResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.filter.SearchFilter;
import com.anjz.base.entity.search.filter.SearchFilterHelper;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.PageRequest;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.core.model.SysResource;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysResourceService;
import com.anjz.core.vo.Menu;
import com.anjz.result.ListResult;
import com.anjz.util.UuidUtil;
import com.google.common.collect.Lists;

public class SysResourceTest extends BaseTest{
	
	@Resource
	private SysResourceService sysResourceService;

	@Test
	public void test(){
		SysUser user=new SysUser();
		user.setId("1");
		ListResult<Menu> result= sysResourceService.findMenus(user);
		
		for(Menu menu:result.getData()){
			System.out.println(menu);
		}
	}
	
	
	/**
	 * 分页，查询条件的测试
	 */
	@Test
	public void pageSortTest(){
		Map<String, Object> searchParams=new HashMap<String, Object>();
		searchParams.put("name.in", new String[]{"1","2"});	
		searchParams.put("parent_id.eq", 0);
		
		Sort sort=new Sort("name","parent_id");
		
		Pageable page=new  PageRequest(0, 10);
				
		Searchable searchable=new SearchRequest(searchParams, page, sort);
		Page<SysResource> pageResult=sysResourceService.findAll(searchable);
		
		System.out.println(pageResult.getContent());
		
		System.out.println(pageResult.getTotalElements());		
	}
	
	
	/**
	 * 添加菜单数据
	 */
	@Test
	public void addResourceTest(){
		SysResource resource=new SysResource();
		resource.setId(UuidUtil.generateUuid32());
		resource.setName("系统管理");
		resource.setIdentity("system");
		resource.setParentId("1");
		resource.setParentIds("0/1/");
		resource.setWeight(1);
		resource.setIsShow(true);
		
		sysResourceService.saveSelective(resource);
	}
	
	/**
	 * 添加孩子节点
	 */
	@Test
	public void addChilden(){
		SysResource parent=sysResourceService.findOne("69c93ba78dd640bea4804be942b5fcf3").getData();
		
		SysResource child=new SysResource();
//		child.setName("用户管理");

		
		child.setName("资源列表");
		child.setIdentity("resource");
		child.setUrl("/system/resource");
		
		child.setIsShow(true);
		
		sysResourceService.appendChild(parent, child);
		
	}
	
	
	/**
	 * 查询子子孙孙
	 */
	@Test
	public void findChildrenTest(){
		List<SysResource> parents=Lists.newArrayList();
		
		parents.add(sysResourceService.findOne("69c93ba78dd640bea4804be942b5fcf3").getData());
		parents.add(sysResourceService.findOne("9f269d56cc694ab5baad165e6c9d4233").getData());
		
		
		Searchable searchable=new SearchRequest();
		searchable.addSearchFilter("name", SearchOperator.eq, "666");
		
		SearchFilter first=SearchFilterHelper.newCondition("name", SearchOperator.eq, "666");
		SearchFilter searchFilter=SearchFilterHelper.newCondition("name", SearchOperator.eq, "666");
		searchable.and(first, searchFilter);
		
		sysResourceService.findChildren(parents, searchable);
	}
}
