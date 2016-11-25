package com.anjz.core.service.intf.maintain;

import java.util.List;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.MaintainTemplate;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年9月13日下午4:44:44
 */
public interface MaintainTemplateService extends BaseService<MaintainTemplate, String>{
	
	
	/**
	 * 批量更新
	 * @param templates
	 * @return
	 */
	public BaseResult batchUpdateTemplate(List<MaintainTemplate> templates);

}
