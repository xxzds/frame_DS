package com.anjz.base;

/**
 * 接口中的属性默认是static final
 * 常量
 * 
 * @author ding.shuai
 * @date 2016年8月17日下午4:28:45
 */
public interface Constants {
    //站内信回复、转发的模板
    public static final String REPLY_PREFIX = "回复：";
    public static final String FOWRARD_PREFIX = "转发：";
    public static final String FOWRARD_TEMPLATE = "<br/><br/>-----------转发消息------------<br/>发件人:%s<br/>收件人：%s<br/>标题：%s<br/><br/>%s";

    
	/**
	 * 当前的登录用户
	 */
	public static final String CURRENT_USER = "user";
	
	/**
	 * 上下文路径，存入项目名称的键
	 */
	public static final String CONTEXT_PATH = "ctx";
	
	/**
     * 错误key
     */
    String ERROR = "error";
    
    /**
     * 操作名称
     */
    String OP_NAME = "op";
    
    /**
     * 消息key
     */
    String MESSAGE = "message";
    
    /**
     * 上个页面地址
     */
    String BACK_URL = "BackURL";

    String IGNORE_BACK_URL = "ignoreBackURL";
    
    /**
     * 编码格式
     */
    String UTF8 = "UTF-8";
	
}
