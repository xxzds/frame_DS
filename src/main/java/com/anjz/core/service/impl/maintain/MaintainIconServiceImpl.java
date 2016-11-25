package com.anjz.core.service.impl.maintain;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.MaintainIconDao;
import com.anjz.core.model.MaintainIcon;
import com.anjz.core.service.intf.maintain.MaintainIconService;
import com.anjz.result.PlainResult;

/**
 * 
 * @author ding.shuai
 * @date 2016年8月29日下午4:14:05
 */
@Service
public class MaintainIconServiceImpl extends BaseServiceImpl<MaintainIcon, String> implements MaintainIconService{
	
	@Resource
	private MaintainIconDao maintainIconDao;

	@Override
	public PlainResult<MaintainIcon> findByIdentity(String identity) {
		PlainResult<MaintainIcon> result = new PlainResult<MaintainIcon>();
		MaintainIcon icon=new MaintainIcon();
		icon.setIdentity(identity);
		
		List<MaintainIcon> list= this.find(icon).getData();
		
		if(list.size()>0){
			result.setData(list.get(0));
		}
		return result;
	}

}
