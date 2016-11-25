package com.anjz.core.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseCRUDController;
import com.anjz.core.model.SysUserStatusHistory;


/**
 * 用户状态变更历史
 * @author ding.shuai
 * @date 2016年9月5日下午4:39:40
 */
@Controller
@RequestMapping("/system/user/statusHistory")
public class UserStatusHistoryController extends BaseCRUDController<SysUserStatusHistory, String>{

}
