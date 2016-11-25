package com.anjz.core.controller.common;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anjz.base.Constants;
import com.anjz.base.util.WebUtils;
import com.anjz.util.DownloadUtils;

/**
 * 下载控制器
 * @author ding.shuai
 * @date 2016年9月24日上午11:22:43
 */
@Controller
public class DownloadController {
	
	@RequestMapping(value = "/download")
    public String download(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "filename") String filename) throws Exception {


        filename = filename.replace("/", "\\");

        if (StringUtils.isEmpty(filename) || filename.contains("\\.\\.")) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("您下载的文件不存在！");
            return null;
        }
        filename = URLDecoder.decode(filename, Constants.UTF8);

        String filePath = WebUtils.getRootRealPath() + "/" + filename;

        DownloadUtils.download(request, response, filePath,filename);
        return null;
    }
}
