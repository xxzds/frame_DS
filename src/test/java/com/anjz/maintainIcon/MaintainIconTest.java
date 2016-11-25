package com.anjz.maintainIcon;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.service.intf.maintain.MaintainIconService;

public class MaintainIconTest extends BaseTest{
	
	@Resource
	private MaintainIconService maintainIconService;
	
	
	@Test
	public void batchDeleteTest(){		
		maintainIconService.batchDeleteByIds(new String[]{"a","b"});
	}
}
