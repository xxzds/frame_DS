package com.anjz.base.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.PageableDefaults;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.service.BaseService;
import com.anjz.result.BaseResult;

/**
 * 基础的CRUD控制器
 * 
 * @author ding.shuai
 * @date 2016年8月29日下午4:10:44
 */
public class BaseCRUDController<M extends BaseEntity, ID extends Serializable> extends BaseController<M, ID> {

	@Autowired
	private BaseService<M, ID> baseService;

	protected PermissionList permissionList = null;
	
	/**
	 * 是否设置公共数据
	 */
	private boolean listAlsoSetCommonData = false;
	
	public boolean isListAlsoSetCommonData() {
		return listAlsoSetCommonData;
	}

	public void setListAlsoSetCommonData(boolean listAlsoSetCommonData) {
		this.listAlsoSetCommonData = listAlsoSetCommonData;
	}

	/**
	 * 权限前缀：如sys:user 则生成的新增权限为 sys:user:create
	 */
	public void setResourceIdentity(String resourceIdentity) {
		if (!StringUtils.isEmpty(resourceIdentity)) {
			permissionList = PermissionList.newPermissionList(resourceIdentity);
		}
	}

	/**
	 * 模块中的主页
	 * 
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	@PageableDefaults(sort = "id=desc")
	public String list(Searchable searchable, Model model) {

		if (permissionList != null) {
			this.permissionList.assertHasViewPermission();
		}

		model.addAttribute("page", baseService.findAll(searchable));
		if (listAlsoSetCommonData) {
			setCommonData(model);
		}
		return viewName("list");
	}
	
	/**
     * 仅返回表格数据
     *
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value = { "", "list" },method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String listTable(Searchable searchable, Model model) {
        list(searchable, model);
        return viewName("listTable");
    }
	
	/**
	 * 增加
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("m")) {
            model.addAttribute("m", newModel());
        }
        return viewName("editForm");
    }


    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(
    		HttpServletRequest request,
            Model model,  @Valid @ModelAttribute("m") M m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }

        if (hasError(m, result)) {
            return showCreateForm(model);
        }
        BaseResult baseResult=baseService.saveSelective(m);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
        }       
        return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
    }
    
    /**
     * 查看
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") ID id,Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }

        setCommonData(model);
        
        model.addAttribute("m", baseService.findOne(id).getData());
        model.addAttribute(Constants.OP_NAME, "查看");
        return viewName("editForm");
    }
    
    
    /**
     * 修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") ID id, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改");
        
        M m=baseService.findOne(id).getData();
        model.addAttribute("m", m);
        return viewName("editForm");
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
            Model model,@Valid @ModelAttribute("m") M m, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        if (hasError(m, result)) {
            return showUpdateForm((ID)m.getId(), model);
        }
        BaseResult baseResult=baseService.updateSelectiveById(m);
        if(!baseResult.isSuccess()){
        	 redirectAttributes.addFlashAttribute(Constants.ERROR, baseResult.getMessage());
        }else{
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        }       
        return redirectToUrl(backURL);
    }
    
    /**
     * 删除
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    public String showDeleteForm(@PathVariable("id") ID id, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "删除");
        model.addAttribute("m", baseService.findOne(id).getData());
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String delete(
            @PathVariable("id") ID id,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        BaseResult result= baseService.deleteById(id);
        if(!result.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, result.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }        
        return redirectToUrl(backURL);
    }
    
    
    /**
     * 批量删除
     * @param ids
     * @param backURL
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) ID[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        BaseResult baseResult= baseService.batchDeleteByIds(ids);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.ERROR,baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }
        return redirectToUrl(backURL);
    }

}
