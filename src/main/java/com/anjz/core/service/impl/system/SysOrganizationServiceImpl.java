package com.anjz.core.service.impl.system;

import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.anjz.base.service.BaseTreeableServiceImpl;
import com.anjz.core.model.SysOrganization;
import com.anjz.core.service.intf.system.SysOrganizationService;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:12:14
 */
@Service
public class SysOrganizationServiceImpl extends BaseTreeableServiceImpl<SysOrganization, String> implements SysOrganizationService{

	@Override
	public void filterForCanShow(Set<String> organizationIds, Set<String[]> organizationJobIds) {
		 Iterator<String> iter1 = organizationIds.iterator();

	        while (iter1.hasNext()) {
	            String id = iter1.next();
	            SysOrganization o = findOne(id).getData();
	            if (o == null || Boolean.FALSE.equals(o.getIsShow())) {
	                iter1.remove();
	            }
	        }

	        Iterator<String[]> iter2 = organizationJobIds.iterator();

	        while (iter2.hasNext()) {
	            String id = iter2.next()[0];
	            SysOrganization o = findOne(id).getData();
	            if (o == null || Boolean.FALSE.equals(o.getIsShow())) {
	                iter2.remove();
	            }
	        }		
	}

}
