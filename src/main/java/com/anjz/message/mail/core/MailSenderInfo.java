package com.anjz.message.mail.core;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * 发送邮件的信息模型
 * @author ding.shuai
 * @date 2016年7月29日下午2:57:14
 */
public class MailSenderInfo {
    // 发送邮件的服务器的IP和端口    
    private String   mailServerHost;
    private String   mailServerPort = "25";
    // 邮件发送者的地址    
    private String   fromAddress;
    //邮件发送者地址别名
    private String 	 fromAddressAlias;
    // 登陆邮件发送服务器的用户名和密码    
    private String   userName;
    private String   password;
    
    
    // 邮件主题    
    private String   subject;
    // 邮件的文本内容    
    private String   content;
    //邮件接收者的地址(发送）
    private String[]  toAddresses;
    //抄送者集合
    private String[] ccAddresses;
    //密送者集合
    private String[] bccAddresses;
    
    // 邮件附件的文件名(全路径)   
    private String[] attachFileNames;

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.auth", "true");
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        if(StringUtils.isEmpty(mailServerPort)) return;
        this.mailServerPort = mailServerPort;
    }


    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

	public String getFromAddressAlias() {
		return fromAddressAlias;
	}

	public void setFromAddressAlias(String fromAddressAlias) {
		this.fromAddressAlias = fromAddressAlias;
	}

	public String[] getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(String[] toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String[] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String[] ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String[] getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(String[] bccAddresses) {
		this.bccAddresses = bccAddresses;
	}
}
