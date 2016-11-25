package com.anjz.core.controller.maintain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.controller.BaseCRUDController;
import com.anjz.base.entity.response.ValidateResponse;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.util.LogUtils;
import com.anjz.base.util.MessageUtils;
import com.anjz.base.util.WebUtils;
import com.anjz.core.enums.IconType;
import com.anjz.core.model.MaintainIcon;
import com.anjz.core.service.intf.maintain.MaintainIconService;
import com.anjz.result.BaseResult;
import com.anjz.upload.FileUploadService;
import com.anjz.upload.FileUploadUtils;
import com.anjz.upload.exception.FileNameLengthLimitExceededException;
import com.anjz.upload.exception.InvalidExtensionException;
import com.google.common.collect.Lists;

/**
 * 图标管理
 * 
 * @author ding.shuai
 * @date 2016年8月29日下午4:08:23
 */
@Controller
@RequestMapping("/maintain/icon")
public class IconController extends BaseCRUDController<MaintainIcon, String> {

	@Resource
	private MaintainIconService maintainIconService;

	@Resource
	private FileUploadService fileUploadService;

	/**
	 * css样式表存的位置
	 */
	@Value("${icon.css.file.src}")
	private String iconClassFile;
	
	/**
	 * 文件图标上传的位置
	 */
	@Value("${file.icon.location}")
	private String fileIconUploadBaseDir;

	public IconController() {
		setListAlsoSetCommonData(true);
		setResourceIdentity("maintain:icon");
	}

	@Override
	protected void setCommonData(Model model) {
		super.setCommonData(model);
		model.addAttribute("types", IconType.values());
	}

	
	@RequestMapping(value = "{type}/create", method = RequestMethod.GET)
	public String showCreateForm(@PathVariable(value = "type") IconType type, Model model) {
		MaintainIcon icon = new MaintainIcon();
		icon.setType(type);
		if (type == IconType.css_sprite || type == IconType.upload_file) {
			icon.setWidth(16);
			icon.setHeight(16);
		}
		model.addAttribute("m", icon);
		return super.showCreateForm(model);
	}

	@RequestMapping(value = "{type}/create", method = RequestMethod.POST)
	public String create(
			HttpServletRequest request,
			Model model,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@ModelAttribute("m") MaintainIcon icon, BindingResult result, RedirectAttributes redirectAttributes) {

		if (file != null && !file.isEmpty()) {
			try {
				String rootPath = WebUtils.getRootRealPath();
        		File parent = new File(rootPath + File.separator + fileIconUploadBaseDir);
        		      		
        		String filename = file.getOriginalFilename();
        		File current = new File(parent, filename);
        		//判断上传的文件是否存在
        		if(current.exists() ) {
        			result.reject("upload.conflict.warn");
                }else{
                	String uploadPath= FileUploadUtils.upload(request, fileIconUploadBaseDir, file, FileUploadUtils.IMAGE_EXTENSION, 
    						FileUploadUtils.DEFAULT_MAX_SIZE, false);
    				icon.setImgSrc(uploadPath.replace("WEB-INF/", ""));	
                }												
			} catch (IOException e) {
	            LogUtils.logError("file upload error", e);
	            result.reject("upload.server.error");
	        } catch (InvalidExtensionException.InvalidImageExtensionException e) {
	            result.reject("upload.not.allow.image.extension");
	        } catch (InvalidExtensionException.InvalidFlashExtensionException e) {
	            result.reject("upload.not.allow.flash.extension");
	        } catch (InvalidExtensionException.InvalidMediaExtensionException e) {
	            result.reject("upload.not.allow.media.extension");
	        } catch (InvalidExtensionException e) {
	            result.reject("upload.not.allow.extension");
	        } catch (FileUploadBase.FileSizeLimitExceededException e) {
	            result.reject("upload.exceed.maxSize");
	        } catch (FileNameLengthLimitExceededException e) {
	            result.reject("upload.filename.exceed.length");
	        }		
		}
		String view = super.create(request,model, icon, result, redirectAttributes);
		// genIconCssFile(request);
		return view;
	}
	
	/**
	 * 重写，覆盖父类中的映射，防止出现
	 * java.lang.IllegalStateException: Ambiguous mapping found. Cannot map 'iconController' bean method 
	 */
	@RequestMapping(value = "{id}/update/discard", method = RequestMethod.POST)
    @Override
    public String update(Model model, MaintainIcon m, BindingResult result, String backURL, RedirectAttributes redirectAttributes) {
		throw new RuntimeException("discarded method");
	};
	
    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(Model model,HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @ModelAttribute("m") MaintainIcon icon, BindingResult result,
            @RequestParam(value = "BackURL") String backURL,
            RedirectAttributes redirectAttributes) {
        if (file != null && !file.isEmpty()) {
        	try {
        		String rootPath = WebUtils.getRootRealPath();
        		File parent = new File(rootPath + File.separator + fileIconUploadBaseDir);
        		      		
        		String filename = file.getOriginalFilename();
        		File current = new File(parent, filename);
        		//判断上传的文件是否存在
        		if(current.exists() ) {
        			result.reject("upload.conflict.warn");
                }else{
                	String uploadPath= FileUploadUtils.upload(request, fileIconUploadBaseDir, file, FileUploadUtils.IMAGE_EXTENSION, 
    						FileUploadUtils.DEFAULT_MAX_SIZE, false);
    				icon.setImgSrc(uploadPath.replace("WEB-INF/", ""));	
                }							
			} catch (IOException e) {
	            LogUtils.logError("file upload error", e);
	            result.reject("upload.server.error");
	        } catch (InvalidExtensionException.InvalidImageExtensionException e) {
	            result.reject("upload.not.allow.image.extension");
	        } catch (InvalidExtensionException.InvalidFlashExtensionException e) {
	            result.reject("upload.not.allow.flash.extension");
	        } catch (InvalidExtensionException.InvalidMediaExtensionException e) {
	            result.reject("upload.not.allow.media.extension");
	        } catch (InvalidExtensionException e) {
	            result.reject("upload.not.allow.extension");
	        } catch (FileUploadBase.FileSizeLimitExceededException e) {
	            result.reject("upload.exceed.maxSize");
	        } catch (FileNameLengthLimitExceededException e) {
	            result.reject("upload.filename.exceed.length");
	        }	
        }
        String view = super.update(model, icon, result, backURL, redirectAttributes);
//	        genIconCssFile(request);
        return view;
    }

	/**
	 * 如果量大 建议 在页面设置按钮 然后点击生成
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/genCssFile")
	@ResponseBody
	public String genIconCssFile(HttpServletRequest request) {

		if (this.permissionList != null) {
			this.permissionList.assertHasEditPermission();
		}

		String uploadFileTemplate = ".%1$s{background:url(%2$s/%3$s);width:%4$spx;height:%5$spx;display:inline-block;vertical-align: middle;%6$s}";
		String cssSpriteTemplate = ".%1$s{background:url(%2$s/%3$s) no-repeat -%4$spx -%5$spx;width:%6$spx;height:%7$spx;display:inline-block;vertical-align: middle;%8$s}";
		//图片来自于网络
        String cssSpriteFromHttpTemplate = ".%1$s{background:url(%2$s) no-repeat -%3$spx -%4$spx;width:%5$spx;height:%6$spx;display:inline-block;vertical-align: middle;%7$s}";

		ServletContext sc = request.getSession().getServletContext();
		String ctx = sc.getContextPath();

		List<String> cssList = Lists.newArrayList();

		Searchable searchable = Searchable.newSearchable().addSearchParam("type|in",
				new IconType[] { IconType.upload_file, IconType.css_sprite });

		List<MaintainIcon> iconList = maintainIconService.findAllWithNoPageNoSort(searchable);

		for (MaintainIcon icon : iconList) {

			if (icon.getType() == IconType.upload_file) {
				cssList.add(String.format(uploadFileTemplate, icon.getIdentity(), ctx, icon.getImgSrc(),
						icon.getWidth(), icon.getHeight(), icon.getStyle()));
				continue;
			}

			if (icon.getType() == IconType.css_sprite) {
				String spriteSrc=icon.getSpriteSrc();
				if(StringUtils.isNotEmpty(spriteSrc) && (spriteSrc.startsWith("http://") || spriteSrc.startsWith("https://"))){
					cssList.add(String.format(cssSpriteFromHttpTemplate, icon.getIdentity(), icon.getSpriteSrc(),
							icon.getOffsetLeft(), icon.getOffsetTop(), icon.getWidth(), icon.getHeight(), icon.getStyle()));
				}else{
					cssList.add(String.format(cssSpriteTemplate, icon.getIdentity(), ctx, icon.getSpriteSrc(),
							icon.getOffsetLeft(), icon.getOffsetTop(), icon.getWidth(), icon.getHeight(), icon.getStyle()));
				}				
				continue;
			}

		}

		try {
			ServletContextResource resource = new ServletContextResource(sc, iconClassFile);
			FileUtils.writeLines(resource.getFile(), cssList);
		} catch (Exception e) {
			LogUtils.logError("gen icon error", e);
			return "生成失败：" + e.getMessage();
		}

		return "生成成功";
	}
	
	
	/**
	 * 校验标识符是否重复
	 * @param fieldId
	 * @param fieldValue
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "validate", method = RequestMethod.GET)
	@ResponseBody
	public Object validate(@RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
			@RequestParam(value = "id", required = false) String id) {
		ValidateResponse response = ValidateResponse.newInstance();

		if ("identity".equals(fieldId)) {
			MaintainIcon icon = maintainIconService.findByIdentity(fieldValue).getData();
			if (icon == null || (icon.getId().equals(id) && icon.getIdentity().equals(fieldValue))) {
				// 如果msg 不为空 将弹出提示框
				response.validateSuccess(fieldId, "该标识符可以使用");
			} else {
				response.validateFail(fieldId, "该标识符已被其他人使用");
			}
		}
		return response.result();
	}
	
	
	/**
	 * 选择图标的展示页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/select")
    public String select(Model model) {
        setCommonData(model);
        model.addAttribute("icons", maintainIconService.findAll().getData());
        return viewName("select");
    }
	
	
	@Override
	@RequestMapping(value = "{id}/delete/discard", method = RequestMethod.POST)
	public String delete(@PathVariable("id") String id,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes){
		throw new RuntimeException("delete discard");
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
	public String delete(HttpServletRequest request,
			@PathVariable("id") String id,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
		if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
		
		MaintainIcon icon= maintainIconService.findOne(id).getData();
		//删除服务器的文件
		if(StringUtils.isNotEmpty(icon.getImgSrc())){
			String imgSrc=icon.getImgSrc();			
			try {
				FileUploadUtils.delete(request, "WEB-INF/"+imgSrc);
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute(Constants.ERROR, MessageUtils.message("upload.delete.error",imgSrc));
				return redirectToUrl(backURL);
			}
		}
        BaseResult resultDelete= maintainIconService.deleteById(id);
        if(!resultDelete.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, resultDelete.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }        
        return redirectToUrl(backURL);
	}
	
	
	@Override
	@RequestMapping(value = "batch/delete/discard", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteInBatch(@RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes){
		throw new RuntimeException("batch delete discard");
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteInBatch(HttpServletRequest request,
			@RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
		if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
		
		for(String id:ids){
			MaintainIcon icon= maintainIconService.findOne(id).getData();
			//删除服务器的文件
			if(StringUtils.isNotEmpty(icon.getImgSrc())){
				String imgSrc=icon.getImgSrc();			
				try {
					FileUploadUtils.delete(request, "WEB-INF/"+imgSrc);
				} catch (IOException e) {
					e.printStackTrace();
					redirectAttributes.addFlashAttribute(Constants.ERROR, MessageUtils.message("upload.delete.error",imgSrc));
					return redirectToUrl(backURL);
				}
			}
	        BaseResult resultDelete= maintainIconService.deleteById(id);
	        if(!resultDelete.isSuccess()){
	        	redirectAttributes.addFlashAttribute(Constants.ERROR,"主键为："+id+"的数据删除失败，批量删除终止");
	        	return redirectToUrl(backURL);
	        }
		}
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
	}
}
