package com.anjz.core.service.impl.maintain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.model.MaintainTemplate;
import com.anjz.core.service.intf.maintain.MaintainTemplateService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年9月13日下午4:46:13
 */
@Service
public class MaintainTemplateServiceImpl extends BaseServiceImpl<MaintainTemplate, String>
		implements MaintainTemplateService {
		
	/**
	 * 批量更新
	 * @param templates
	 * @return
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult batchUpdateTemplate(List<MaintainTemplate> templates){
		if(templates==null){
			throw new BusinessException("参数异常");
		}
		
		for(MaintainTemplate template:templates){
			BaseResult result= this.updateSelectiveById(template);
			if(!result.isSuccess()){
				throw new BusinessException("批量更新失败");
			}
		}
		return new BaseResult();
	}
}
