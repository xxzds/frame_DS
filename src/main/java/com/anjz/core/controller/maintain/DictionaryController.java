package com.anjz.core.controller.maintain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseTreeableController;
import com.anjz.core.model.MaintainDictionary;

/**
 * 数据字典
 * @author ding.shuai
 * @date 2016年9月13日下午5:37:31
 */
@Controller
@RequestMapping("/maintain/dictionary")
public class DictionaryController extends BaseTreeableController<MaintainDictionary, String>{

}
