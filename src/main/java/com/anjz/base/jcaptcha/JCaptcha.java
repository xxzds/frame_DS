package com.anjz.base.jcaptcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供相应的 API 来验证当前请求输入的验证码是否正确
 * @author ding.shuai
 * @date 2016年8月23日上午9:53:42
 */
public class JCaptcha {
    public static final MyManageableImageCaptchaService captchaService
            = new MyManageableImageCaptchaService(new FastHashMapCaptchaStore(), new GMailEngine(), 180, 100000, 75000);


    /**
     * 验证当前请求输入的验证码是否正确;并从 CaptchaService 中删除已经生成的验证码;
     * @param request
     * @param userCaptchaResponse
     * @return
     */
    public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse) {
        if (request.getSession(false) == null) return false;

        boolean validated = false;
        try {
            String id = request.getSession().getId();
            validated = captchaService.validateResponseForID(id, userCaptchaResponse).booleanValue();
        } catch (CaptchaServiceException e) {
            e.printStackTrace();
        }
        return validated;
    }

    /**
     * 验证当前请求输入的验证码是否正确;但不从 CaptchaService 中删除已经生成的验证码(比如 Ajax 验证时可以使用,防止多次生成验证码);
     * @param request
     * @param userCaptchaResponse
     * @return
     */
    public static boolean hasCaptcha(HttpServletRequest request, String userCaptchaResponse) {
        if (request.getSession(false) == null) return false;
        boolean validated = false;
        try {
            String id = request.getSession().getId();
            validated = captchaService.hasCapcha(id, userCaptchaResponse);
        } catch (CaptchaServiceException e) {
            e.printStackTrace();
        }
        return validated;
    }

}
