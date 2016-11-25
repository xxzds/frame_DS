package com.anjz.base.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.PageableDefaults;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.Treeable;
import com.anjz.base.entity.ZTree;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.enums.BooleanEnum;
import com.anjz.base.service.BaseTreeableService;
import com.anjz.result.BaseResult;
import com.google.common.collect.Lists;

/**
 * tree型结构控制器基类
 * @author ding.shuai
 * @date 2016年8月27日上午10:40:29
 */
public class BaseTreeableController<M extends BaseEntity & Treeable,ID extends Serializable> extends BaseController<M, ID> {

	@Autowired
	private BaseTreeableService<M, ID> baseService;
	
	protected PermissionList permissionList = null;
	
	/**
     * 权限前缀：如sys:user
     * 则生成的新增权限为 sys:user:create
     */
    public void setResourceIdentity(String resourceIdentity) {
        if (!StringUtils.isEmpty(resourceIdentity)) {
            permissionList = PermissionList.newPermissionList(resourceIdentity);
        }
    }
    
    /**
     * 设置公共数据
     */
    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
    }
	
    /**
     * 模块中的主页
     * @return
     */
	@RequestMapping(value = {"", "main"}, method = RequestMethod.GET)
    public String main() {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }
        return viewName("main");
    }
	
	/**
	 * 显示树型结构（界面左侧）
	 * @param request
	 * @param searchName
	 * @param async
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tree", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    public String tree(@RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            Searchable searchable, Model model) {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        List<M> models = null;

        if (!StringUtils.isEmpty(searchName)) {
            searchable.addSearchFilter("name", SearchOperator.eq, searchName);
            models = baseService.findAllByName(searchable, null);
            if (!async) { //非异步 查自己和子子孙孙
            	searchable.addSearchFilter("name", SearchOperator.eq, searchName);
                List<M> children = baseService.findChildren(models, searchable);
                models.removeAll(children);
                models.addAll(children);
            } else { //异步模式只查自己

            }
        } else {
            if (!async) {  //非异步 查自己和子子孙孙
                models = baseService.findAllWithSort(searchable);
            } else {  //异步模式只查根 和 根一级节点
                models = baseService.findRootAndChild(searchable);
            }
        }
        
        model.addAttribute("trees",convertToZtreeList(models,async,true));
        return viewName("tree");
    }
	
	
	
	
	/**
	 * 查看
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") ID id, Model model) {
		if (permissionList != null) {
			permissionList.assertHasViewPermission();
		}

		setCommonData(model);

		M m = baseService.findOne(id).getData();
		model.addAttribute("m", m);
		model.addAttribute(Constants.OP_NAME, "查看");
		return viewName("editForm");
	}
	 
	
	/**
	 * 修改
	 * @param id
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") ID id, Model model, RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }
        
        M m=baseService.findOne(id).getData();        
        if (m == null) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "您修改的数据不存在！");
            return redirectToUrl(viewName("success"));
        }
        
        //设置公共数据
        setCommonData(model);
                
        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "修改");
        return viewName("editForm");
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(Model model,
            @Valid @ModelAttribute("m") M m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        if (result.hasErrors()) {
            return updateForm((ID)m.getId(), model, redirectAttributes);
        }

        BaseResult baseResult=baseService.updateSelectiveById(m);
        if(baseResult.isSuccess()){
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }
       
        return redirectToUrl(viewName("success"));
    }
	
	
	/**
	 * 删除
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
	public String deleteForm(@PathVariable("id") ID id, Model model) {

		if (permissionList != null) {
			permissionList.assertHasDeletePermission();
		}

		setCommonData(model);

		M m = baseService.findOne(id).getData();
		model.addAttribute("m", m);
		model.addAttribute(Constants.OP_NAME, "删除");
		return viewName("editForm");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
	public String deleteSelfAndChildren(Model model, @Valid @ModelAttribute("m") M m, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (permissionList != null) {
			permissionList.assertHasDeletePermission();
		}

		if (m.isRoot()) {
			result.reject("您删除的数据中包含根节点，根节点不能删除");
			return deleteForm((ID) m.getId(), model);
		}

		baseService.deleteSelfAndChild(m);		
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
		return redirectToUrl(viewName("success"));
	}
	
	
	/**
	 * 添加子节点
	 * @param parentId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{parentId}/appendChild", method = RequestMethod.GET)
	public String appendChildForm(@PathVariable("parentId") ID parentId, Model model) {

		if (permissionList != null) {
			permissionList.assertHasCreatePermission();
		}
		
		// 查询父节点
		M parent = baseService.findOne(parentId).getData();
		model.addAttribute("parent", parent);

		setCommonData(model);
		if (!model.containsAttribute("child")) {
			model.addAttribute("child", newModel());
		}

		model.addAttribute(Constants.OP_NAME, "添加子节点");
		return viewName("appendChildForm");
	}
	
	 @RequestMapping(value = "{parentId}/appendChild", method = RequestMethod.POST)
	    public String appendChild(
	            Model model,
	            @PathVariable("parentId") ID parentId,
	            @Valid @ModelAttribute("child") M child, BindingResult result,
	            RedirectAttributes redirectAttributes) {

	        if (permissionList != null) {
	            permissionList.assertHasCreatePermission();
	        }

	        setCommonData(model);

	        if (result.hasErrors()) {
	            return appendChildForm(parentId, model);
	        }

	        M parent= baseService.findOne(parentId).getData();        
	        baseService.appendChild(parent, child);

	        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "添加子节点成功");
	        return redirectToUrl(viewName("success"));
	    }
	
	
	/**
	 * 移动节点
	 * @param request
	 * @param async
	 * @param sourceId
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{sourceId}/move", method = RequestMethod.GET)
	@PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    public String showMoveForm(@RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            @PathVariable("sourceId") ID sourceId,
            Searchable searchable,
            Model model) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        List<M> models = null;

        M source=baseService.findOne(sourceId).getData();
        model.addAttribute("source", source);
        
        //排除自己及子子孙孙
        searchable.addSearchFilter("id", SearchOperator.ne, source.getId());
        searchable.addSearchFilter("parent_ids",SearchOperator.notLike,source.makeSelfAsNewParentIds());

        if (!async) {
            models = baseService.findAllWithSort(searchable);
        } else {
            models = baseService.findRootAndChild(searchable);
        }

		model.addAttribute("trees", convertToZtreeList(models, async, true));

        model.addAttribute(Constants.OP_NAME, "移动节点");

        return viewName("moveForm");
    }
	
	@RequestMapping(value = "{sourceId}/move", method = RequestMethod.POST)
    @PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    public String move(
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            @PathVariable("sourceId") ID sourceId,
            @RequestParam("targetId") ID targetId,
            @RequestParam("moveType") String moveType,
            Searchable searchable,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        M source=baseService.findOne(sourceId).getData();
        M target=baseService.findOne(targetId).getData();
        
        if (target.isRoot() && !moveType.equals("inner")) {
            model.addAttribute(Constants.ERROR, "不能移动到根节点之前或之后");
            return showMoveForm(async, sourceId, searchable, model);
        }

        baseService.move(source, target, moveType);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "移动节点成功");
        return redirectToUrl(viewName("success"));
    }
	
	
	/**
	 * 查看子节点
	 * @param parentId
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{parentId}/children", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    public String list(@PathVariable("parentId") ID parentId,Searchable searchable, Model model) {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        M parent=baseService.findOne(parentId).getData();
        if (parent != null) {
            searchable.addSearchFilter("parent_id", SearchOperator.eq, parent.getId());
        }
        
        model.addAttribute("parent", parent);
        model.addAttribute("page", baseService.findAll(searchable));

        return viewName("listChildren");
    }

	/**
	 * 仅返回表格数据
	 * @param parentId
	 * @param searchable
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "{parentId}/children", headers = "table=true", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    public String listTable(@PathVariable("parentId") ID parentId,Searchable searchable, Model model) {
        list(parentId, searchable, model);
        
        return viewName("listChildrenTable");
    }
    
    
    /**
     * 选中显示，选中隐藏
     * @param request
     * @param newStatus
     * @param ids
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/changeStatus/{newStatus}")
    public String changeStatus(HttpServletRequest request,@PathVariable("newStatus") Boolean newStatus,
            @RequestParam("ids") String[] ids) {

    	if (permissionList != null) {
    		 this.permissionList.assertHasUpdatePermission();
        }

        for (String id : ids) {
            M m = baseService.findOne((ID)id).getData();
            m.setIsShow(newStatus);
            baseService.updateSelectiveById(m);
        }
        return "redirect:" + request.getAttribute(Constants.BACK_URL);
    }
    
    
    /**
     * 批量删除
     * @param ids
     * @param backURL
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "batch/delete")
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        //如果要求不严格 此处可以删除判断 前台已经判断过了
        Searchable searchable = Searchable.newSearchable().addSearchFilter("id", SearchOperator.in, ids);
        List<M> mList = baseService.findAllWithNoPageNoSort(searchable);
        for (M m : mList) {
            if (m.isRoot()) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "您删除的数据中包含根节点，根节点不能删除");
                return redirectToUrl(backURL);
            }
        }

        baseService.deleteSelfAndChild(mList);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
    }


	/*******************************************异步ajax请求*****************************************************/
	@RequestMapping(value = "ajax/load")
	@PageableDefaults(sort = {"parent_ids=asc", "weight=asc"})
    @ResponseBody
    public Object load(
            HttpServletRequest request,
            @RequestParam(value = "async", defaultValue = "true") boolean async,
            @RequestParam(value = "asyncLoadAll", defaultValue = "false") boolean asyncLoadAll,
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "id", required = false) ID parentId,
            @RequestParam(value = "excludeId", required = false) ID excludeId,
            @RequestParam(value = "onlyCheckLeaf", required = false, defaultValue = "false") boolean onlyCheckLeaf,
            Searchable searchable) {
        M excludeM = baseService.findOne(excludeId).getData();

        List<M> models = null;

        if (!StringUtils.isEmpty(searchName)) {//按name模糊查
            searchable.addSearchFilter("name", SearchOperator.like, searchName);
            models = baseService.findAllByName(searchable, excludeM);
            if (!async || asyncLoadAll) {//非异步模式 查自己及子子孙孙 但排除
                searchable.removeSearchFilter("name", SearchOperator.like);
                List<M> children = baseService.findChildren(models, searchable);
                models.removeAll(children);
                models.addAll(children);
            } else { //异步模式 只查匹配的一级

            }
        } else { //根据有没有parentId加载

            if (parentId != null) { //只查某个节点下的 异步
                searchable.addSearchFilter("parent_id", SearchOperator.eq, parentId);
            }

            if (async && !asyncLoadAll) { //异步模式下 且非异步加载所有
                //排除自己 及 子子孙孙
                baseService.addExcludeSearchFilter(searchable, excludeM);

            }

            if (parentId == null && !asyncLoadAll) {
                models = baseService.findRootAndChild(searchable);
            } else {
                models = baseService.findAllWithSort(searchable);
            }
        }

		return convertToZtreeList(models, async && !asyncLoadAll && parentId != null, onlyCheckLeaf);
    }
	
	
	@RequestMapping(value = "ajax/{parentId}/appendChild", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxAppendChild(HttpServletRequest request, @PathVariable("parentId") ID parentId) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        M child = newModel();
        child.setName("新节点");
                
        //通过id查询
        M parent=baseService.findOne(parentId).getData();
        
        baseService.appendChild(parent, child);
        return convertToZtree(child, true, true);
    }
	
	
	@RequestMapping(value = "ajax/{id}/delete", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxDeleteSelfAndChildren(@PathVariable("id") ID id) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        M tree = baseService.findOne(id).getData();
        baseService.deleteSelfAndChild(tree);
        return tree;
    }
	
	@RequestMapping(value = "ajax/{id}/rename", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxRename(HttpServletRequest request, @PathVariable("id") ID id, @RequestParam("newName") String newName) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        M tree = baseService.findOne(id).getData();
        tree.setName(newName);
        baseService.updateSelectiveById(tree);
        return convertToZtree(tree, true, true);
    }
	
	@RequestMapping(value = "ajax/{sourceId}/{targetId}/{moveType}/move", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxMove(
            @PathVariable("sourceId") ID sourceId, @PathVariable("targetId") ID targetId,
            @PathVariable("moveType") String moveType) {
		
        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        M source=baseService.findOne(sourceId).getData();
    	M target=baseService.findOne(targetId).getData();

        baseService.move(source, target, moveType);
        return source;
    }
	
	/**
	 * 模糊查询，提示
	 * @param searchable
	 * @param term
	 * @param excludeId
	 * @return
	 */
	@RequestMapping("ajax/autocomplete")
    @PageableDefaults(value = 30)
    @ResponseBody
    public Set<String> autocomplete(
            Searchable searchable,
            @RequestParam("term") String term,
            @RequestParam(value = "excludeId", required = false) ID excludeId) {

        return baseService.findNames(searchable, term, excludeId);
    }
	
	
	
	
	
	/*******************************************公共方法**************************************************/
	
	private List<ZTree> convertToZtreeList(List<M> models, boolean async, boolean onlySelectLeaf) {
        List<ZTree> zTrees = Lists.newArrayList();

        if (models == null || models.isEmpty()) {
            return zTrees;
        }

        for (M m : models) {
            ZTree zTree = convertToZtree(m, !async, onlySelectLeaf);
            zTrees.add(zTree);
        }
        return zTrees;
    }
	
	private ZTree convertToZtree(M m, boolean open, boolean onlyCheckLeaf) {
        ZTree zTree = new ZTree();
        
        zTree.setId(m.getId());
        zTree.setpId(m.getParentId());
        zTree.setName(m.getName());
        zTree.setIconSkin(m.getIconByDefault());
        zTree.setOpen(open);
        zTree.setRoot(m.isRoot());       
        zTree.setIsParent(m.isHasChildren());

        if (onlyCheckLeaf && zTree.isIsParent()) {
            zTree.setNocheck(true);
        } else {
            zTree.setNocheck(false);
        }

        return zTree;
    }
	
	
	/**
	 * 操作完成后，跳转到成功页面
	 * @return
	 */
	@RequestMapping(value = "success")
    public String success() {
        return viewName("success");
    }
	
	@Override
    protected String redirectToUrl(String backURL) {
        if (!StringUtils.isEmpty(backURL)) {
            return super.redirectToUrl(backURL);
        }
        return super.redirectToUrl(viewName("success"));
    }
}
