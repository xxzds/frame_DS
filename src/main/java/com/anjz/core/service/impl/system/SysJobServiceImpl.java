package com.anjz.core.service.impl.system;

import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.anjz.base.service.BaseTreeableServiceImpl;
import com.anjz.core.model.SysJob;
import com.anjz.core.service.intf.system.SysJobService;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:19:34
 */
@Service
public class SysJobServiceImpl extends BaseTreeableServiceImpl<SysJob, String>implements SysJobService{

	@Override
	public void filterForCanShow(Set<String> jobIds, Set<String[]> organizationJobIds) {
		Iterator<String> iter1 = jobIds.iterator();

        while (iter1.hasNext()) {
            String id = iter1.next();
            SysJob o = findOne(id).getData();
            if (o == null || Boolean.FALSE.equals(o.getIsShow())) {
                iter1.remove();
            }
        }

        Iterator<String[]> iter2 = organizationJobIds.iterator();

        while (iter2.hasNext()) {
        	String id = iter2.next()[1];
            SysJob o = findOne(id).getData();
            if (o == null || Boolean.FALSE.equals(o.getIsShow())) {
                iter2.remove();
            }
        }		
	}

}
