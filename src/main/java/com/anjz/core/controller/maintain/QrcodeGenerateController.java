package com.anjz.core.controller.maintain;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anjz.base.controller.BaseController;
import com.anjz.base.util.WebUtils;
import com.anjz.util.QRCodeUtil;

/**
 * 二维码生成
 * @author ding.shuai
 * @date 2016年9月24日下午4:45:02
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/maintain/qrcodeGenerate")
@RequiresPermissions("maintain:qrcode:*")
public class QrcodeGenerateController extends BaseController{
	
	@RequestMapping("")
	public String main(){
		return viewName("main");
	}
	
	
	/**
	 * 生成验证码
	 * @throws IOException 
	 */
	@RequestMapping("generate")
	public void generateQrcode(
			 HttpServletResponse response,
			 @RequestParam("content") String content,
			 @RequestParam(value="logopath",required = false) String logopath) throws IOException{
		
		response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        
        if(StringUtils.isNotEmpty(logopath)){
			logopath=WebUtils.getRootRealPath()+File.separator+logopath;
		}
		
		OutputStream out=response.getOutputStream();
		try {
			QRCodeUtil.encode(content, logopath, out, true);
			out.flush();
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			out.close();
		}
	}

}
