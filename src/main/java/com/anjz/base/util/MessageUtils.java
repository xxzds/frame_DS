package com.anjz.base.util;

import org.springframework.context.MessageSource;

import com.anjz.spring.SpringUtils;

/**
 * 信息定制
 * @author ding.shuai
 * @date 2016年9月19日下午10:29:15
 */
public class MessageUtils {

    private static MessageSource messageSource;

    /**
     * 根据消息键和参数 获取消息
     * 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return
     */
    public static String message(String code, Object... args) {
        if (messageSource == null) {
            messageSource = SpringUtils.getBean(MessageSource.class);
        }
        return messageSource.getMessage(code, args, null);
    }

}
