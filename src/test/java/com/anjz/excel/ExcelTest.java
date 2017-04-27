package com.anjz.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.anjz.util.excel.ExcelUtil;
import com.anjz.util.excel.ExportSetInfo;
import com.google.common.collect.Lists;

/**
 * excel导出测试
 * @author shuai.ding
 *
 * @date 2017年4月27日下午3:11:17
 */
public class ExcelTest{

	@Test
	public void excelTest() throws IllegalArgumentException, IllegalAccessException, IOException {
		ExportSetInfo setInfo = new ExportSetInfo();

		setInfo.setTitles(new String[] { "用户列表1","用户列表2"});

		List<String[]> headNames = new ArrayList<String[]>();
		headNames.add(new String[] { "用户ID", "用户名", "年龄" });
		headNames.add(new String[] { "用户ID", "用户名", "年龄" });
		setInfo.setHeadNames(headNames);

		List<String[]> fieldNames = new ArrayList<String[]>();
		fieldNames.add(new String[] { "userId", "userName", "userAge" });
		fieldNames.add(new String[] { "userId", "userName", "userAge" });
		setInfo.setFieldNames(fieldNames);

		
		//准备数据
		User user = new User();
		user.setUserId("1");
		user.setUserName("你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好");
		user.setUserAge(18);
		
		List<User> users = Lists.newArrayList();
		users.add(user);
		users.add(user);
		users.add(user);
		users.add(user);
		users.add(user);
		users.add(user);
		
		
		java.util.LinkedHashMap<String, List<?>> objsMap = new java.util.LinkedHashMap<String, List<?>>();
		objsMap.put("用户列表1", users);
		objsMap.put("用户列表2", users);
		setInfo.setObjsMap(objsMap);

		FileOutputStream out = new FileOutputStream("C:/Users/Administrator/Desktop/a.xls");
		setInfo.setOut(out);
		ExcelUtil.export2Excel(setInfo);
	}

}
