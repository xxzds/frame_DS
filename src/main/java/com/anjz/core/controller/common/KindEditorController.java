package com.anjz.core.controller.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anjz.base.util.WebUtils;
import com.anjz.result.PlainResult;
import com.anjz.upload.FileUploadService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author ding.shuai
 * @date 2016年9月18日下午4:47:19
 */
@Controller
@RequestMapping(value = "/kindeditor")
public class KindEditorController {

    //图片mime类型
    private static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};
    
    //附件mime类型
    private static final String[] ATTACHMENT_EXTENSION = {
            //图片
            "bmp", "gif", "jpg", "jpeg", "png",
            //word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "html", "htm", "txt",
            //压缩文件
            "rar", "zip", "gz", "bz2",
            //pdf
            "pdf"
    };
    
    //flash mime类型
    private static final String[] FLASH_EXTENSION = {
            "swf", "flv"
    };
    
    //swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb
    private static final String[] MEDIA_EXTENSION = {
            "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg", "asf", "rm", "rmvb"
    };
    
    @Value("${local.uploadDirectory}")
    private String baseDir;
    
    @Resource
    private FileUploadService fileUploadService;


    /**
     * 返回的数据格式：<br>
     * 出错时{error : 1,message:出错时的消息}<br>
     * 正确时{error:0,url:上传后的地址,title:文件的名称}<br>
     * 
     * @param response
     * @param request
     * @param dir  表示类型  file image flash media
     * @param file  文件对象
     * @return
     */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "dir", required = false) String dir,
			@RequestParam(value = "imgFile", required = false) MultipartFile file) {
		
		response.setContentType("text/html; charset=UTF-8");

		if (file == null) {
			return errorResponse("upload.no.file");
		}
		
		String[] allowedExtension = extractAllowedExtension(dir);
		PlainResult<String> result = fileUploadService.uploadFile(file, allowedExtension);
		if (!result.isSuccess()) {
			return errorResponse(result.getMessage());
		}

		return successResponse(request, file.getOriginalFilename(), result.getData());
	}
	
	
	/**
	 * 图片空间/文件空间中展示的数据
	 * <p/>
	 * 返回的数据格式：<br/>
	 * 出错时：无提示，可以优化<br/>
	 * 正确时：<br/>
	 * {"current_url":当前地址（绝对）,<br/>
     * "current_dir_path":当前目录（相对）,<br/>
     * "moveup_dir_path":上级目录（可以根据当前目录算出来）,<br/>
     * "file_list":[//文件列表<br/>
     * 文件名                  文件大小     文件类型      是否包含文件    是否是目录   是否是照片        时间<br/>
     * {"filename":"My Pictures","filesize":0,"filetype":"","has_file":true,"is_dir":true,"is_photo":false,"datetime":"2013-03-09 11:41:17"}<br/>
     * ],<br/>
     * "total_count":文件及目录总数<br/>
     * }<br/>
	 * 
	 * 
	 * @param request
	 * @param order            排序  NAME SIZE TYPE
	 * @param dir              文件类型 file image flash media
	 * @param currentDirPath   当前访问的目录（相对路径） 【..】需要排除掉  相对于baseDir
	 * @return
	 */
    @RequestMapping(value = "filemanager", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object fileManager(
            HttpServletRequest request,
            @RequestParam(value = "order", required = false, defaultValue = "NAME") String order,
            @RequestParam(value = "dir", required = false, defaultValue = "file") String dir,
            @RequestParam(value = "path", required = false, defaultValue = "") String currentDirPath) {
        //根目录路径
        String rootPath =WebUtils.getRootRealPath() + "/" + baseDir;
        String rootUrl = request.getContextPath() + "/" + baseDir;


        //上一级目录
        String moveupDirPath = "";
        if (!"".equals(currentDirPath)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //不允许使用..移动到上一级目录
        if (currentDirPath.indexOf("..") >= 0) {
            return "访问拒绝，目录中不能出现..";
        }
        //最后一个字符不是/
        if (!"".equals(currentDirPath) && !currentDirPath.endsWith("/")) {
            return "参数不合法，目录需要以 / 结尾";
        }
        //目录不存在或不是目录
        File currentPathFile = new File(rootPath + "/" + currentDirPath);
        if (!currentPathFile.exists() || !currentPathFile.isDirectory()) {
            return "目录不存在";
        }

        //遍历目录取的文件信息
        List<Map<String, Object>> fileMetaInfoList = Lists.newArrayList();

        List<String> allowedExtension = Arrays.asList(extractAllowedExtension(dir));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {

                Map<String, Object> fileMetaInfo = Maps.newHashMap();
                String fileName = file.getName();

                if (file.isDirectory()) {
                    fileMetaInfo.put("is_dir", true);
                    fileMetaInfo.put("has_file", (file.listFiles() != null));
                    fileMetaInfo.put("filesize", 0L);
                    fileMetaInfo.put("is_photo", false);
                    fileMetaInfo.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = FilenameUtils.getExtension(fileName);
                    if (!allowedExtension.contains(FilenameUtils.getExtension(fileName))) {
                        continue;
                    }
                    fileMetaInfo.put("is_dir", false);
                    fileMetaInfo.put("has_file", false);
                    fileMetaInfo.put("filesize", file.length());
                    fileMetaInfo.put("is_photo", Arrays.<String>asList(IMAGE_EXTENSION).contains(fileExt));
                    fileMetaInfo.put("filetype", fileExt);
                }
                fileMetaInfo.put("filename", fileName);
                fileMetaInfo.put("datetime", df.format(file.lastModified()));

                fileMetaInfoList.add(fileMetaInfo);
            }
        }

        if ("size".equalsIgnoreCase(order)) {
            Collections.sort(fileMetaInfoList, new SizeComparator());
        } else if ("type".equalsIgnoreCase(order)) {
            Collections.sort(fileMetaInfoList, new TypeComparator());
        } else {
            Collections.sort(fileMetaInfoList, new NameComparator());
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("moveup_dir_path", moveupDirPath);
        result.put("current_dir_path", currentDirPath);
        result.put("current_url", rootUrl + "/" + currentDirPath);
        result.put("total_count", fileMetaInfoList.size());
        result.put("file_list", fileMetaInfoList);

        return result;
    }

    
	/**
	 * 成功信息的响应
	 * @param request
	 * @param filename
	 * @param url
	 * @return
	 */
    private String successResponse(HttpServletRequest request, String filename, String url) {
        return "{\"error\":0, \"url\":\"" + request.getContextPath() + "/" + url + "\", \"title\":\"" + filename + "    \"}";
    }
    
    /**
     * 错误信息的响应
     * @param message
     * @return
     */
    private String errorResponse(String message) {
        if (message.contains("<br/>")) {
            message = message.replace("<br/>", "\\n");
        }
        return "{\"error\":1, \"message\":\"" + message + "\"}";
    }
    
    /**
     * 提取允许上传的文件扩展名
     * @param dir
     * @return
     */
    private String[] extractAllowedExtension(String dir) {
        if ("image".equals(dir)) {
            return IMAGE_EXTENSION;
        } else if ("flash".equals(dir)) {
            return FLASH_EXTENSION;
        } else if ("media".equals(dir)) {
            return MEDIA_EXTENSION;
        } else {
            return ATTACHMENT_EXTENSION;
        }
    }
    
    public class NameComparator implements Comparator<Object> {
    	
    	@Override
        public int compare(Object a, Object b) {
            Map<?, ?> mapA = (Map<?, ?>) a;
            Map<?, ?> mapB = (Map<?, ?>) b;
            if (((Boolean) mapA.get("is_dir")) && !((Boolean) mapB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) mapA.get("is_dir")) && ((Boolean) mapB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) mapA.get("filename")).compareTo((String) mapB.get("filename"));
            }
        }
    }

    public class SizeComparator implements Comparator<Object> {
    	
    	@Override
        public int compare(Object a, Object b) {
            Map<?, ?> mapA = (Map<?, ?>) a;
            Map<?, ?> mapB = (Map<?, ?>) b;
            if (((Boolean) mapA.get("is_dir")) && !((Boolean) mapB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) mapA.get("is_dir")) && ((Boolean) mapB.get("is_dir"))) {
                return 1;
            } else {
                if (((Long) mapA.get("filesize")) > ((Long) mapB.get("filesize"))) {
                    return 1;
                } else if (((Long) mapA.get("filesize")) < ((Long) mapB.get("filesize"))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public class TypeComparator implements Comparator<Object> {
    	
    	@Override
        public int compare(Object a, Object b) {
            Map<?, ?> mapA = (Map<?, ?>) a;
            Map<?, ?> mapB = (Map<?, ?>) b;
            if (((Boolean) mapA.get("is_dir")) && !((Boolean) mapB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) mapA.get("is_dir")) && ((Boolean) mapB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) mapA.get("filetype")).compareTo((String) mapB.get("filetype"));
            }
        }
    }
    
}

