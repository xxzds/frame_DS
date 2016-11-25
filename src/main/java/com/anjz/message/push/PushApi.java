package com.anjz.message.push;

/**
 * @author ding.shuai
 * @date 2016年9月18日下午7:40:47
 */
public interface PushApi {

	/**
     * 推送未读消息
     * @param userId
     */
    public void pushUnreadMessage(final String userId, Long unreadMessageCount);

    /**
     * 离线用户，即清空用户的DefferedResult 这样就是新用户，可以即时得到通知
     *
     * 比如刷新主页时，需要offline
     *
     * @param userId
     */
    void offline(String userId);
}
