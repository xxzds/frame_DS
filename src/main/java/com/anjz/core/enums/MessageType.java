package com.anjz.core.enums;

/**
 * 消息类型
 * @author ding.shuai
 * @date 2016年9月18日下午12:04:21
 */
public enum MessageType {
    user_message("普通消息"),
    system_message("系统消息");

    private final String info;

    private MessageType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    

}
