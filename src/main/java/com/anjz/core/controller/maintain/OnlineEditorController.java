package com.anjz.core.controller.maintain;

import static com.anjz.core.controller.maintain.utils.OnlineEditorUtils.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.controller.BaseController;
import com.anjz.base.entity.response.AjaxUploadResponse;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.base.util.LogUtils;
import com.anjz.base.util.MessageUtils;
import com.anjz.base.util.WebUtils;
import com.anjz.core.controller.maintain.utils.CompressUtils;
import com.anjz.upload.FileUploadUtils;
import com.anjz.upload.exception.FileNameLengthLimitExceededException;
import com.anjz.upload.exception.InvalidExtensionException;
import com.anjz.util.DownloadUtils;
import com.google.common.collect.Lists;
/**
 * 在线编辑
 * 
 * @author ding.shuai
 * @date 2016年9月22日上午11:29:12
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/maintain/editor")
@RequiresPermissions("maintain:onlineEditor:*")
public class OnlineEditorController extends BaseController {
	
	private final long MAX_SIZE = 20000000; //20MB
    private final String[] ALLOWED_EXTENSION = new String[] {
            "bmp","gif","jpeg", "jpg", "png",
            "pdf",
            "docx", "doc", "xlsx", "xls", "pptx", "ppt",
            "zip", "rar",
            "jsp", "jspf", "tag", "tld", "xml", "java", "html", "css", "js"
    };
    
    //允许\ 即多级目录创建
    private final String VALID_FILENAME_PATTERN = "[^\\s:\\*\\?\\\"<>\\|]?(\\x20|[^\\s:\\*\\?\\\"<>\\|])*[^\\s:\\*\\?\\\"<>\\|\\.]?$";

	@RequestMapping(value = { "", "main" }, method = RequestMethod.GET)
	public String main() {
		return viewName("main");
	}
	
	@RequestMapping(value = "tree", method = RequestMethod.GET)
    public String tree(Model model) throws UnsupportedEncodingException {
        String rootPath = WebUtils.getRootRealPath();

        long id = 0L;
        File rootDirectory = new File(rootPath);

        Map<Object, Object> root= extractFileInfoMap(rootDirectory, rootPath, id, -1);

        List<Map> trees = Lists.newArrayList();
        trees.add(root);

        for(File subFile : rootDirectory.listFiles()) {
            if(!subFile.isDirectory()) {
                continue;
            }
            id++;
            trees.add(extractFileInfoMap(subFile, rootPath, id, (Long)root.get("id")));
        }

         model.addAttribute("trees", trees);

        return viewName("tree");
    }
	
	
	@RequestMapping(value = "ajax/load", method = RequestMethod.GET)
    @ResponseBody
    public Object ajaxLoad(
            @RequestParam("id") long parentId,
            @RequestParam(value = "paths", required = false) String[] excludePaths,
            @RequestParam("path") String parentPath) throws UnsupportedEncodingException {

        parentPath = URLDecoder.decode(parentPath, Constants.UTF8);

        String rootPath = WebUtils.getRootRealPath();

        File parentPathDirectory = new File(rootPath + File.separator + parentPath);

        List<Map> trees = Lists.newArrayList();

        long id = parentId;

        for(File subFile : parentPathDirectory.listFiles()) {
            if(!subFile.isDirectory()) {
                continue;
            }
            String path = URLEncoder.encode(subFile.getAbsolutePath().replace(rootPath, ""), Constants.UTF8);
            if(isExclude(excludePaths, path)) {
                continue;
            }
            id++;
            trees.add(extractFileInfoMap(subFile, rootPath, id, parentId));
        }
        return trees;
    }
	
	private boolean isExclude(String[] excludePaths, String path) {
        if (excludePaths == null) {
            return false;
        }
        for (int i = 0; i < excludePaths.length; i++) {
            String excludePath = excludePaths[i];
            if(path.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * 右侧，展示目录的信息
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public String listFile(
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            Pageable pageable,
            Model model) throws UnsupportedEncodingException {

        path = URLDecoder.decode(path, Constants.UTF8);

        Sort sort = pageable.getSort();

        String rootPath = WebUtils.getRootRealPath();

        File currentDirectory = new File(rootPath + File.separator + path);

        Map<Object, Object> current = extractFileInfoMap(currentDirectory, rootPath);
        current.put("name", currentDirectory.getName());

        Map<Object, Object> parent = null;
        if(haveParent(currentDirectory, rootPath)) {
            File parentDirectory = currentDirectory.getParentFile();
            parent = extractFileInfoMap(parentDirectory, rootPath);
            parent.put("name", parentDirectory.getName());
        }


        List<Map<Object, Object>> files = Lists.newArrayList();
        for(File subFile : currentDirectory.listFiles()) {
            files.add(extractFileInfoMap(subFile, rootPath));
        }

        sort(files, sort);

        model.addAttribute("current", current);
        model.addAttribute("parent", parent);
        model.addAttribute("files", files);

        return viewName("list");
    }
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
    public String showEditForm(
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {

        String rootPath = WebUtils.getRootRealPath();


        path = URLDecoder.decode(path, Constants.UTF8);
        File file = new File(rootPath + File.separator + path);
        String parentPath = file.getParentFile().getAbsolutePath().replace(rootPath, "");

        boolean hasError = false;
        if(file.isDirectory()) {
            hasError = true;
            redirectAttributes.addFlashAttribute(Constants.ERROR, path + "是目录，不能编辑！");
        }

        if(!file.exists()) {
            hasError = true;
            redirectAttributes.addFlashAttribute(Constants.ERROR, path + "文件不存在，不能编辑！");
        }

        if(!file.canWrite()) {
            hasError = true;
            redirectAttributes.addFlashAttribute(Constants.ERROR, path + "文件是只读的，不能编辑，请修改文件权限！");
        }
        if(!file.canRead()) {
            hasError = true;
            redirectAttributes.addFlashAttribute(Constants.ERROR, path + "文件不能读取，不能编辑，请修改文件权限！");
        }


        if(hasError) {
            redirectAttributes.addAttribute("path", parentPath);
            return redirectToUrl(viewName("list"));
        }

        String content = FileUtils.readFileToString(file);
        model.addAttribute("content", content);
        model.addAttribute("path", URLEncoder.encode(path, Constants.UTF8));
        model.addAttribute("parentPath", URLEncoder.encode(parentPath, Constants.UTF8));

        return viewName("editForm");
    }
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(
            @RequestParam(value = "path") String path,
            @RequestParam(value = "content") String content,
            RedirectAttributes redirectAttributes) throws IOException {

        String rootPath = WebUtils.getRootRealPath();

        path = URLDecoder.decode(path, Constants.UTF8);
        File file = new File(rootPath + File.separator + path);
        String parentPath = file.getParentFile().getAbsolutePath().replace(rootPath, "");

        FileUtils.write(file, content);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "编辑成功！");
        redirectAttributes.addAttribute("path", parentPath);
        return redirectToUrl(viewName("list"));
    }
	
	/**********************************list.jsp 表格上的一排按钮*********************************************/
	/**
	 *上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String showUploadForm(
            @RequestParam("parentPath") String parentPath,
            Model model,
            RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

        String rootPath = WebUtils.getRootRealPath();
        parentPath = URLDecoder.decode(parentPath, Constants.UTF8);
        File parent = new File(rootPath + File.separator + parentPath);
        if(!parent.exists()) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, parentPath + "目录不存在！");
            redirectAttributes.addAttribute("path", "");
            return redirectToUrl(viewName("list"));
        }

        model.addAttribute("parentPath", parentPath);
        return viewName("uploadForm");
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxUploadResponse upload(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("parentPath") String parentPath,
            @RequestParam("conflict") String conflict,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) throws UnsupportedEncodingException {

        String rootPath = WebUtils.getRootRealPath();
        parentPath = URLDecoder.decode(parentPath, Constants.UTF8);
        File parent = new File(rootPath + File.separator + parentPath);


        //The file upload plugin makes use of an Iframe Transport module for browsers like Microsoft Internet Explorer and Opera, which do not yet support XMLHTTPRequest file uploads.
        response.setContentType("text/plain");

        AjaxUploadResponse ajaxUploadResponse = new AjaxUploadResponse();

        if (ArrayUtils.isEmpty(files)) {
            return ajaxUploadResponse;
        }

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            long size = file.getSize();
            try {
                File current = new File(parent, filename);
                if(current.exists() && "ignore".equals(conflict)) {
                    ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.conflict.error"));
                    continue;
                }
                String url = FileUploadUtils.upload(request, parentPath, file, ALLOWED_EXTENSION, MAX_SIZE, false);
                String deleteURL = viewName("/delete") + "?paths=" + URLEncoder.encode(url, Constants.UTF8);

                ajaxUploadResponse.add(filename, size, url, deleteURL);

                continue;
            } catch (IOException e) {
                LogUtils.logError("file upload error", e);
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.server.error"));
                continue;
            } catch (InvalidExtensionException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.not.allow.extension"));
                continue;
            } catch (FileUploadBase.FileSizeLimitExceededException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.exceed.maxSize"));
                continue;
            } catch (FileNameLengthLimitExceededException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.filename.exceed.length"));
                continue;
            }
        }
        return ajaxUploadResponse;
    }
    
    /**
     * 创建目录
     */
    @RequestMapping("/create/directory")
    public String createDirectory(
            @RequestParam(value = "parentPath") String parentPath,
            @RequestParam(value = "name") String name,
            RedirectAttributes redirectAttributes) throws IOException {

        //删除最后的/
        name = FilenameUtils.normalizeNoEndSeparator(name);

        if(isValidFileName(name)) {
            String rootPath = WebUtils.getRootRealPath();
            parentPath = URLDecoder.decode(parentPath, Constants.UTF8);

            File parent = new File(rootPath + File.separator + parentPath);
            File currentDirectory = new File(parent, name);
            boolean result = currentDirectory.mkdirs();
            if(result == false) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "名称为[" + name + "]的文件/目录已经存在");
            } else {
                redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建成功！");
            }
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "名称为[" + name + "]不是合法的文件名，请重新命名");
        }

        redirectAttributes.addAttribute("path", parentPath);
        return redirectToUrl(viewName("list"));
    }
 
    /**
     * 创建文件
     */
    @RequestMapping("/create/file")
    public String createFile(
            @RequestParam(value = "parentPath") String parentPath,
            @RequestParam(value = "name") String name,
            RedirectAttributes redirectAttributes) throws IOException {

        if(isValidFileName(name)) {
            String rootPath = WebUtils.getRootRealPath();
            parentPath = URLDecoder.decode(parentPath, Constants.UTF8);

            File parent = new File(rootPath + File.separator + parentPath);
            File currentFile = new File(parent, name);
            currentFile.getParentFile().mkdirs();
            boolean result = currentFile.createNewFile();
            if(result == false) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "名称为[" + name + "]的文件/目录已经存在");
            } else {
                redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建成功！");
            }
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "名称为[" + name + "]不是合法的文件名，请重新命名");
        }

        redirectAttributes.addAttribute("path", parentPath);
        return redirectToUrl(viewName("list"));
    }
    
    /**
     * 重命名
     */
    @RequestMapping("/rename")
    public String rename(
            @RequestParam(value = "path") String path,
            @RequestParam(value = "newName") String newName,
            RedirectAttributes redirectAttributes) throws IOException {

        String rootPath = WebUtils.getRootRealPath();
        path = URLDecoder.decode(path, Constants.UTF8);

        File current = new File(rootPath + File.separator + path);
        File parent = current.getParentFile();
        String parentPath = parent.getAbsolutePath().replace(rootPath, "");

        File renameToFile = new File(parent, newName);
        boolean result = current.renameTo(renameToFile);
        if(result == false) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "名称为[" + newName + "]的文件/目录已经存在");
        } else {
            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "重命名成功");
        }

        redirectAttributes.addAttribute("path", parentPath);
        return redirectToUrl(viewName("list"));
    }

	
    private boolean isValidFileName(String fileName) {
        return fileName.matches(VALID_FILENAME_PATTERN);
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(
            @RequestParam(value = "paths") String[] paths,
            RedirectAttributes redirectAttributes) throws IOException {

        String rootPath = WebUtils.getRootRealPath();

        for(String path : paths) {
            path = URLDecoder.decode(path, Constants.UTF8);
            File current = new File(rootPath + File.separator + path);
            FileUtils.deleteQuietly(current);
        }

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功！");

        String path = URLDecoder.decode(paths[0], Constants.UTF8);
        File file = new File(rootPath + File.separator + path);
        String parentPath = file.getParentFile().getAbsolutePath().replace(rootPath, "");
        redirectAttributes.addAttribute("path", parentPath);
        return redirectToUrl(viewName("list"));
    }
    
    @RequestMapping("select")
    public String showSelectForm(
            @RequestParam("paths") String[] excludePaths,
            Model model) throws UnsupportedEncodingException {

        String rootPath = WebUtils.getRootRealPath();

        List<Map> trees = Lists.newArrayList();

        long id = 0L;
        File rootDirectory = new File(rootPath);

        Map<Object, Object> root= extractFileInfoMap(rootDirectory, rootPath, id, -1);

        trees.add(root);

        for(File subFile : rootDirectory.listFiles()) {
            if(!subFile.isDirectory()) {
                continue;
            }
            String path = URLEncoder.encode(subFile.getAbsolutePath().replace(rootPath, ""), Constants.UTF8);
            if(isExclude(excludePaths, path)) {
                continue;
            }
            id++;
            trees.add(extractFileInfoMap(subFile, rootPath, id, (Long)root.get("id")));
        }

        model.addAttribute("trees", trees);
        model.addAttribute("excludePaths", excludePaths);

        return viewName("selectForm");
    }
    
    
    /**
     * 移动
     */
    @RequestMapping("move")
    public String move(
            @RequestParam(value = "descPath") String descPath,
            @RequestParam(value = "paths") String[] paths,
            @RequestParam(value = "conflict") String conflict,
            RedirectAttributes redirectAttributes) throws IOException {


        String rootPath = WebUtils.getRootRealPath();
        descPath = URLDecoder.decode(descPath, Constants.UTF8);

        for(int i = 0, l = paths.length; i < l; i++) {
            String path = paths[i];
            path = URLDecoder.decode(path, Constants.UTF8);
            paths[i] = (rootPath + File.separator + path).replace("\\", "/");
        }

        try {
            File descPathFile = new File(rootPath + File.separator + descPath);
            for(String path : paths) {
                File sourceFile = new File(path);
                File descFile = new File(descPathFile, sourceFile.getName());
                if(descFile.exists() && "ignore".equals(conflict)) {
                    continue;
                }

                FileUtils.deleteQuietly(descFile);

                if(sourceFile.isDirectory()) {
                    FileUtils.moveDirectoryToDirectory(sourceFile, descPathFile, true);
                } else {
                    FileUtils.moveFileToDirectory(sourceFile, descPathFile, true);
                }

            }
            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "移动成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
        }

        redirectAttributes.addAttribute("path", URLEncoder.encode(descPath, Constants.UTF8));
        return redirectToUrl(viewName("list"));
    }
    
    
    /**
     * 复制
     */
    @RequestMapping("copy")
    public String copy(
            @RequestParam(value = "descPath") String descPath,
            @RequestParam(value = "paths") String[] paths,
            @RequestParam(value = "conflict") String conflict,
            RedirectAttributes redirectAttributes) throws IOException {


        String rootPath = WebUtils.getRootRealPath();
        descPath = URLDecoder.decode(descPath, Constants.UTF8);

        for(int i = 0, l = paths.length; i < l; i++) {
            String path = paths[i];
            path = URLDecoder.decode(path, Constants.UTF8);
            paths[i] = (rootPath + File.separator + path).replace("\\", "/");
        }

        try {
            File descPathFile = new File(rootPath + File.separator + descPath);
            for(String path : paths) {
                File sourceFile = new File(path);
                File descFile = new File(descPathFile, sourceFile.getName());
                if(descFile.exists() && "ignore".equals(conflict)) {
                    continue;
                }

                FileUtils.deleteQuietly(descFile);

                if(sourceFile.isDirectory()) {
                    FileUtils.copyDirectoryToDirectory(sourceFile, descPathFile);
                } else {
                    FileUtils.copyFileToDirectory(sourceFile, descPathFile);
                }

            }
            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "复制成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
        }

        redirectAttributes.addAttribute("path", URLEncoder.encode(descPath, Constants.UTF8));
        return redirectToUrl(viewName("list"));
    }
    
    /**
     * 压缩
     */
    @RequestMapping("compress")
    public String compress(
            @RequestParam(value = "parentPath") String parentPath,
            @RequestParam(value = "paths") String[] paths,
            RedirectAttributes redirectAttributes) throws IOException {


        String rootPath = WebUtils.getRootRealPath();
        parentPath = URLDecoder.decode(parentPath, Constants.UTF8);

        Date now = new Date();
        String pattern = "yyyyMMddHHmmss";

        String compressPath = parentPath + File.separator + "[系统压缩]" + DateFormatUtils.format(now, pattern) + "-" + System.nanoTime() + ".zip";

        for(int i = 0, l = paths.length; i < l; i++) {
            String path = paths[i];
            path = URLDecoder.decode(path, Constants.UTF8);
            paths[i] = rootPath + File.separator + path;
        }

        try {
            CompressUtils.zip(rootPath + File.separator + compressPath, paths);
            String msg = "压缩成功，<a href='%s/%s?path=%s' target='_blank' class='btn btn-primary'>点击下载</a>，下载完成后，请手工删除生成的压缩包";
            redirectAttributes.addFlashAttribute(Constants.MESSAGE,
                    String.format(msg,
                    		WebUtils.getContextPath(),
                            viewName("download"),
                            URLEncoder.encode(compressPath, Constants.UTF8)));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
        }

        redirectAttributes.addAttribute("path", URLEncoder.encode(parentPath, Constants.UTF8));
        return redirectToUrl(viewName("list"));
    }
    
    /**
     * 下载压缩文件
     */
    @RequestMapping("/download")
    public void download(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("path") String path) throws Exception {

        String rootPath = WebUtils.getRootRealPath();
        String filePath = rootPath + File.separator + URLDecoder.decode(path, Constants.UTF8);
        filePath = filePath.replace("\\", "/");

        DownloadUtils.download(request, response, filePath);
        return;
    }
    
    /**
     * 解压缩
     */
    @RequestMapping("uncompress")
    public String uncompress(
            @RequestParam(value = "descPath") String descPath,
            @RequestParam(value = "paths") String[] paths,
            @RequestParam(value = "conflict") String conflict,
            RedirectAttributes redirectAttributes) throws IOException {


        String rootPath = WebUtils.getRootRealPath();
        descPath = URLDecoder.decode(descPath, Constants.UTF8);

        for(int i = 0, l = paths.length; i < l; i++) {
            String path = paths[i];
            path = URLDecoder.decode(path, Constants.UTF8);
            //只保留.zip的
            if(!path.toLowerCase().endsWith(".zip")) {
                continue;
            }
            paths[i] = rootPath + File.separator + path;
        }

        try {

            String descAbsolutePath = rootPath + File.separator + descPath;
            for(String path : paths) {
                CompressUtils.unzip(path, descAbsolutePath, "override".equals(conflict));
            }
            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "解压成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
        }

        redirectAttributes.addAttribute("path", URLEncoder.encode(descPath, Constants.UTF8));
        return redirectToUrl(viewName("list"));
    }
	
}
