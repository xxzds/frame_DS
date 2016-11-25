package com.anjz.core.service.intf.maintain;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.MaintainIcon;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年8月29日下午4:13:06
 */
public interface MaintainIconService extends BaseService<MaintainIcon, String>{
	
	/**
	 * 通过身份标识，查询结果
	 * @param identity
	 * @return
	 */
	public PlainResult<MaintainIcon> findByIdentity(String identity);

}
