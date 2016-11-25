package com.anjz.sysGroupRelation;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.service.intf.system.SysGroupRelationService;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月26日下午12:14:56
 */
public class SysGroupRelationTest extends BaseTest {
	
	@Resource
	private SysGroupRelationService sysGroupRelationService;
	
	@Test
	public void test(){
		Set<String> organizationIds=Sets.newHashSet();
		organizationIds.add("1");
		organizationIds.add("2");	
		System.out.println(sysGroupRelationService.findGroupIds("1", organizationIds));
	}

}
