package com.anjz.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anjz.util.PropertiesUtil;
import com.anjz.util.sftp.MyProgressMonitor;
import com.anjz.util.sftp.SFTPChannel;
import com.anjz.util.sftp.SftpUtil;
import com.jcraft.jsch.ChannelSftp;

/**
 * sftp 测试
 * @author shuai.ding
 *
 * @date 2017年3月22日下午4:28:36
 */
public class SftpTest {

	private SFTPChannel sftpChannel = new SFTPChannel();
	
	private ChannelSftp channelSftp;
	
	@Before
	public void sftpConnection(){
		PropertiesUtil propertiesUtil = PropertiesUtil.getInstance("config.properties");
		String host = propertiesUtil.getValue("sftp.host");
		Integer port = Integer.parseInt(propertiesUtil.getValue("sftp.port"));
		String username = propertiesUtil.getValue("sftp.username");		
		String password = propertiesUtil.getValue("sftp.password");		
		channelSftp = sftpChannel.getChannel(host, port, username, password);		
	}	

	/**
	 * 上传测试
	 * @throws Exception 
	 */
	@Test
	public void uploadTest() throws Exception{
		String src="C:/Users/Administrator/Desktop/apache-activemq-5.14.4-bin.tar.gz";
		String dst="/usr/local/nginx-1.7.8/html/frame/upload/test/apache-activemq-5.14.4-bin.tar.gz";
		
		//1.第一种方式
//		SftpUtil.upload(src, dst, channelSftp);
		SftpUtil.upload(src, dst, channelSftp,new MyProgressMonitor());
		
		
		//2.第二种方式
//		InputStream inputStream =new FileInputStream(new File(src));
//		SftpUtil.upload(inputStream, dst, channelSftp);
		
		//3.第三种方式
//		SftpUtil.uploadByStream(src, dst, channelSftp);
	}
	
	/**
	 * 下载测试
	 */
	@Test
	public void downloadTest(){
		
	}
	
	
	@After
	public void closeConnection(){
		sftpChannel.closeChannel();
	}
}
