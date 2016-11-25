package com.anjz.shiro.realm;

import org.apache.shiro.authc.DisabledAccountException;

/**
 * 账户删除异常
 * @author ding.shuai
 * @date 2016年9月5日下午6:15:35
 */
public class DeletedAccountException extends DisabledAccountException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new DeletedAccountException.
     */
    public DeletedAccountException() {
        super();
    }

    /**
     * Constructs a new DeletedAccountException.
     *
     * @param message the reason for the exception
     */
    public DeletedAccountException(String message) {
        super(message);
    }

    /**
     * Constructs a new DeletedAccountException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public DeletedAccountException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DeletedAccountException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public DeletedAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
