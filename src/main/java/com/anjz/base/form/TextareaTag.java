/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.anjz.base.form;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.support.BindStatus;

import com.anjz.base.form.bind.SearchBindStatus;

/**
 * 取值时
 * 1、先取parameter
 * 2、如果找不到再找attribute (page--->request--->session--->application)
 * <p>User: Zhang Kaitao
 * <p>Date: 13-3-28 下午3:11
 * <p>Version: 1.0
 */
public class TextareaTag extends org.springframework.web.servlet.tags.form.TextareaTag {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BindStatus bindStatus = null;

    @Override
    protected BindStatus getBindStatus() throws JspException {
        if (this.bindStatus == null) {
            this.bindStatus = SearchBindStatus.create(pageContext, getName(), getRequestContext(), false);
        }
        return this.bindStatus;
    }


    @Override
    protected String getPropertyPath() throws JspException {
        return getPath();
    }


    @Override
    public void doFinally() {
        super.doFinally();
        this.bindStatus = null;
    }

}
