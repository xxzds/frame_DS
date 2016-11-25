package com.anjz.util.pdf;

import java.io.UnsupportedEncodingException;

/**
 * 中文转化工具
 * @author ding.shuai
 * @date 2016年7月31日下午4:30:48
 */
public class StrHelp {
	 public static String getChinese(String s) {
	      try {
	         return new String(s.getBytes("gb2312"), "iso-8859-1");
	      } catch (UnsupportedEncodingException e) {
	         return s;
	      }
	   }
}
