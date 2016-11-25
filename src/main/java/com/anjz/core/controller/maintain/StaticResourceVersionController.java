package com.anjz.core.controller.maintain;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anjz.base.controller.BaseController;
import com.anjz.base.util.WebUtils;
import com.anjz.core.controller.maintain.utils.YuiCompressorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 静态资源版本控制 
 * 要求:所有需要版本控制的静态资源 以.jspf为后缀 放到webapp/WEB-INF/views/common下
 * 这样即可扫描这些文件实施版本控制
 * 
 * @author ding.shuai
 * @date 2016年9月22日下午11:56:39
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/maintain/staticResource")
@RequiresPermissions("maintain:staticResource:*")
public class StaticResourceVersionController extends BaseController {

	private final String versionedResourcePath = "/WEB-INF/views/common";
	
    private final Pattern scriptPattern =
            Pattern.compile("(.*<script.* src=[\'\"])(.*?)(\\??)(\\d*)([\'\"].*>.*)", Pattern.CASE_INSENSITIVE);
    private final Pattern linkPattern =
            Pattern.compile("(.*<link.* href=[\'\"])(.*?)(\\??)(\\d*)([\'\"].*>.*)", Pattern.CASE_INSENSITIVE);


    /**
     * 主页
     */
	@RequestMapping("")
	public String list(Model model) throws IOException {		
		String realPath = WebUtils.getRealPath(versionedResourcePath);
		model.addAttribute("resources", findStaticResources(realPath));
		return viewName("list");
	}
	
	/**
	 * 版本+1
	 */
	@RequestMapping(value = "incVersion", method = RequestMethod.POST)
    @ResponseBody
    public String incVersion(
            @RequestParam("fileName") String fileName,
            @RequestParam("content") String content,
            @RequestParam("newVersion") String newVersion) throws IOException {

        String realPath = WebUtils.getRealPath(versionedResourcePath);
        return versionedStaticResourceContent(realPath + fileName, content, newVersion);
    }
	
	/**
	 * 批量版本+1
	 */
	@RequestMapping(value = "batchIncVersion", method = RequestMethod.POST)
    @ResponseBody
    public String batchIncVersion(
            @RequestParam("fileNames[]") String[] fileNames,
            @RequestParam("contents[]") String[] contents,
            @RequestParam("newVersions[]") String[] newVersions) throws IOException {
        String realPath = WebUtils.getRealPath(versionedResourcePath);

        for(int i = 0, l = fileNames.length; i < l; i++) {
            versionedStaticResourceContent(realPath + fileNames[i], contents[i], newVersions[i]);
        }
        return "操作成功";
    }
	
	/**
	 * 清空版本
	 */
	@RequestMapping(value = "clearVersion", method = RequestMethod.POST)
    @ResponseBody
    public String clearVersion(
            @RequestParam("fileNames[]") String[] fileNames,
            @RequestParam("contents[]") String[] contents) throws IOException {
        String realPath = WebUtils.getRealPath(versionedResourcePath);

        for(int i = 0, l = fileNames.length; i < l; i++) {
            versionedStaticResourceContent(realPath + fileNames[i], contents[i], null);
        }
        return "操作成功";
    }
	
	/**
	 * 压缩
	 */
	@RequestMapping("compress")
    @ResponseBody
    public String compress(
            @RequestParam("fileName") String fileName,
            @RequestParam("content") String content) {
        String rootRealPath = WebUtils.getRealPath("/WEB-INF");
        String versionedResourceRealPath = WebUtils.getRealPath(versionedResourcePath);

        try {
            String minFilePath = compressStaticResource(rootRealPath, versionedResourceRealPath + fileName, content);
            return "压缩成功，压缩好的文件为：" + minFilePath;
        } catch (Exception e) {
        	e.printStackTrace();
            return "压缩失败：" + e.getMessage();
        }
    }
	
	
	/**
	 * 批量压缩js/css
	 */
	@RequestMapping("batchCompress")
    @ResponseBody
    public String batchCompress(
            @RequestParam("fileNames[]") String[] fileNames,
            @RequestParam("contents[]") String[] contents) throws IOException {
        String rootRealPath = WebUtils.getRealPath("/WEB-INF");
        String versionedResourceRealPath = WebUtils.getRealPath(versionedResourcePath);

        StringBuilder success = new StringBuilder();
        StringBuilder error = new StringBuilder();

        for (int i = 0, l = fileNames.length; i < l; i++) {
            try {
                String fileName = fileNames[i];
                String content = contents[i];
                String minFilePath = compressStaticResource(rootRealPath, versionedResourceRealPath + fileName, content);
                success.append("压缩成功，压缩好的文件为：" + minFilePath + "<br/>");
            } catch (Exception e) {
            	e.printStackTrace();
                error.append("压缩失败：" + e.getMessage() + "<br/>");
            }
        }
        return success.insert(0, "成功的压缩：<br/>").append("<br/>失败的压缩：<br/>").append(error).toString();
    }

	/**
	 * 切换版本
	 */
	@RequestMapping("switch")
    @ResponseBody
    public Object switchStaticResource(
            @RequestParam("fileName") String fileName,
            @RequestParam("content") String content,
            @RequestParam(value = "min", required = false, defaultValue = "false") boolean isMin) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("msg", "切换成功");
        data.put("success", true);
        
        String rootRealPath = WebUtils.getRealPath("/WEB-INF");
        String versionedResourceRealPath = WebUtils.getRealPath(versionedResourcePath);

        try {
            StaticResource resource = switchStaticResourceContent(rootRealPath, versionedResourceRealPath, fileName, content, isMin);
            data.put("content", resource.getContent());
            data.put("url", resource.getUrl());
            return data;
        } catch (Exception e) {
        	e.printStackTrace();
            data.put("msg", "切换失败：" + e.getMessage());
            data.put("success", false);
            return data;
        }
    }
	
	 /**
     * 批量切换版本
     */
    @RequestMapping("batchSwitch")
    @ResponseBody
    public String batchSwitchStaticResource(
            @RequestParam("fileNames[]") String[] fileNames,
            @RequestParam("contents[]") String[] contents,
            @RequestParam(value = "min", required = false, defaultValue = "false") boolean isMin) throws IOException {
        String rootRealPath = WebUtils.getRealPath("/WEB-INF");
        String versionedResourceRealPath = WebUtils.getRealPath(versionedResourcePath);

        StringBuilder success = new StringBuilder();
        StringBuilder error = new StringBuilder();

        for (int i = 0, l = fileNames.length; i < l; i++) {
            try {
                String fileName = fileNames[i];
                String content = contents[i];
                StaticResource resource =
                        switchStaticResourceContent(rootRealPath, versionedResourceRealPath, fileName, content, isMin);
                success.append("切换成功，切换到的文件为：" + resource.getUrl() + "<br/>");
            } catch (Exception e) {
            	e.printStackTrace();
                error.append("切换失败：" + e.getMessage() + "<br/>");
            }
        }
        return success.insert(0, "成功的切换：<br/>").append("<br/>失败的切换：<br/>").append(error).toString();
    }
	
	
	private Map<String, List<StaticResource>> findStaticResources(String realPath) throws IOException {
		//文件名：[]
        final Map<String, List<StaticResource>> resources = Maps.newTreeMap();

        final int realPathLength = realPath.length();

        Collection<File> files = FileUtils.listFiles(new File(realPath), new String[]{"jspf"}, true);

        for (File file : files) {
            String fileName = file.getAbsolutePath().substring(realPathLength);
            List<String> contents = FileUtils.readLines(file);

            List<StaticResource> resourceList = resources.get(fileName);
            if (resourceList == null) {
                resourceList = Lists.newArrayList();
            }

            for (String content : contents) {
                if (!StringUtils.isEmpty(content)) {
                    StaticResource resource = extractResource(fileName, content);
                    if (resource != null) {
                        resourceList.add(resource);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(resourceList)) {
                resources.put(fileName, resourceList);
            }
        }

        return resources;
    }
	
	/**
	 * 更新版本
	 * @param fileRealPath  文件的真实路径
	 * @param content       一行内容
	 * @param newVersion    新版本
	 * @return
	 * @throws IOException
	 */
	 private String versionedStaticResourceContent(String fileRealPath, String content, String newVersion) throws IOException {
        content = StringEscapeUtils.unescapeXml(content);
        if(newVersion != null && newVersion.equals("1")) {
            newVersion = "?" + newVersion;
        }

        File file = new File(fileRealPath);

        List<String> contents = FileUtils.readLines(file);

        for(int i = 0, l = contents.size(); i < l; i++) {
            String fileContent = contents.get(i);
            if(content.equals(fileContent)) {
                Matcher matcher = scriptPattern.matcher(content);
                if(!matcher.matches()) {
                    matcher = linkPattern.matcher(content);
                }
                if(newVersion == null) { //删除版本
                    content = matcher.replaceAll("$1$2$5");
                } else {
                    content = matcher.replaceAll("$1$2$3" + newVersion + "$5");
                }
                contents.set(i, content);
                break;
            }
        }
        FileUtils.writeLines(file, contents);
        return content;
    }
	 
	/**
	 * 压缩静态资源
	 */
	private String compressStaticResource(String rootRealPath, String includeFilePath, String content) {
		StaticResource resource = extractResource(includeFilePath, content);
		String filePath = resource.getUrl();
		filePath = filePath.replace("${ctx}", rootRealPath);

		if (YuiCompressorUtils.hasCompress(filePath)) {
			throw new RuntimeException("[" + filePath + "]文件已经是压缩过的了，不需要再压缩了");
		}

		if (filePath.startsWith("http://")) {
			throw new RuntimeException("[" + filePath + "]文件是互联网上的，无法压缩");
		}
		String minFilePath = YuiCompressorUtils.compress(filePath);

		return minFilePath.replace(rootRealPath, "${ctx}");
	}
	 	
	/**
	 * 提出资源信息
	 * @param fileName  文件名称
	 * @param content   文件中的某行信息
	 * @return
	 */
	private StaticResource extractResource(String fileName, String content) {
        Matcher matcher = scriptPattern.matcher(content);
        if(!matcher.matches()) {
            matcher = linkPattern.matcher(content);
        }

        if(!matcher.matches()) {
            return null;
        }

        String url = matcher.group(2);
        String version = "";
        version = matcher.group(4);

        StaticResource resource = new StaticResource(fileName, content);
        resource.setUrl(url);
        resource.setVersion(version);

        return resource;
    }
	
	private StaticResource switchStaticResourceContent(String rootRealPath, String versionedResourceRealPath,
			String fileName, String content, boolean isMin) throws IOException {

		StaticResource resource = extractResource(fileName, content);
		String filePath = resource.getUrl();
		filePath = filePath.replace("${ctx}", rootRealPath);

		if (isMin) {
			File file = new File(YuiCompressorUtils.getCompressFileName(filePath));
			if (!file.exists()) {
				throw new RuntimeException("请先压缩文件：" + resource.getUrl());
			}
		} else {
			File file = new File(YuiCompressorUtils.getNoneCompressFileName(filePath));
			if (!file.exists()) {
				throw new RuntimeException("没有压缩文件对应的非压缩版：" + resource.getUrl());
			}
		}
		content = StringEscapeUtils.unescapeXml(content);

		File file = new File(versionedResourceRealPath + fileName);

		List<String> contents = FileUtils.readLines(file);

		for (int i = 0, l = contents.size(); i < l; i++) {
			String fileContent = contents.get(i);
			if (content.equals(fileContent)) {
				Matcher matcher = scriptPattern.matcher(content);
				if (!matcher.matches()) {
					matcher = linkPattern.matcher(content);
				}
				String newUrl = isMin ? YuiCompressorUtils.getCompressFileName(resource.getUrl())
						: YuiCompressorUtils.getNoneCompressFileName(resource.getUrl());

				content = matcher.replaceAll("$1" + Matcher.quoteReplacement(newUrl) + "$3$4$5");
				contents.set(i, content);

				resource.setContent(content);
				resource.setUrl(newUrl);

				break;
			}
		}
		FileUtils.writeLines(file, contents);

		return resource;
	}
	
	/**
	 * 内部类，静态资源的信息
	 * @author ding.shuai
	 * @date 2016年9月23日上午12:06:12
	 */
	public static class StaticResource {
		/**
		 * 文件名称
		 */
        private String fileName;
        /**
         * 文件中对应的某一行数据
         */
        private String content;
        
        /**
         * content中src或href中的信息
         */
        private String url;
        
        /**
         * 版本
         */
        private String version;

        private StaticResource(String fileName, String content) {
            this.fileName = fileName;
            this.content = content;
        }


        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "StaticResource{" +
                    "fileName='" + fileName + '\'' +
                    ", content='" + content + '\'' +
                    ", url='" + url + '\'' +
                    ", version=" + version +
                    '}';
        }
    }
}
