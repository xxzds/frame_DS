package com.anjz.core.controller.maintain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.SearchableDefaults;
import com.anjz.base.controller.BaseCRUDController;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.core.enums.DictionaryType;
import com.anjz.core.model.MaintainDictionary;
import com.anjz.core.model.MaintainTemplate;
import com.anjz.core.model.SysAuth;
import com.anjz.core.service.intf.maintain.MaintainDictionaryService;
import com.anjz.core.service.intf.maintain.MaintainTemplateService;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 通知模板控制器
 * @author ding.shuai
 * @date 2016年9月13日下午4:33:33
 */
@Controller
@RequestMapping("/maintain/template")
public class TemplateController extends BaseCRUDController<MaintainTemplate, String>{
	
	@Resource
	private MaintainTemplateService templateService;
	
	@Resource
	private MaintainDictionaryService maintainDictionaryService;
	
	public TemplateController() {
		setResourceIdentity("maintain:template");
	}
	
	@Override
	protected void setCommonData(Model model) {
		//模板类型
		model.addAttribute("types", maintainDictionaryService.findDictionariesByParentCode(DictionaryType.template_type.getCode()).getData());
	}
	
	@Override
	@SearchableDefaults(value={"deleted|eq=0"})
	public String list(Searchable searchable, Model model){
		
		//模板类型查询
		String type= searchable.getValue("type|like");
		if(type != null){
			
			MaintainDictionary dictionary=new MaintainDictionary();
			dictionary.setNameCode(DictionaryType.template_type.getCode());
			List<MaintainDictionary> parentlist = maintainDictionaryService.find(dictionary).getData();
			if(parentlist.isEmpty()){
				model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
				return viewName("list");
			}
			
			Searchable searchable1=new SearchRequest()
					.addSearchFilter("name", SearchOperator.like, type)
					.addSearchFilter("parent_id", SearchOperator.eq, parentlist.get(0).getId());
			List<MaintainDictionary> list= maintainDictionaryService.findAllWithNoPageNoSort(searchable1);
			if(list.isEmpty()){
				model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
				return viewName("list");
			}
			
			Set<String> dictionaryIds=Sets.newHashSet(Lists.transform(list, new Function<MaintainDictionary, String>() {
				@Override
				public String apply(MaintainDictionary input) {
					return input.getId();
				}
			}));
			searchable.removeSearchFilter("type", SearchOperator.like);
			searchable.addSearchFilter("type",SearchOperator.in,dictionaryIds);
		}
		
		model.addAttribute("deleted",searchable.getValue("deleted|eq") );
		return super.list(searchable, model);
	}
	
	@Override
	@SearchableDefaults(value={"deleted|eq=0"})
	public String listTable(Searchable searchable, Model model){
		return super.listTable(searchable, model);
	}
	
	
	/**
	 * 逻辑删除、还原
	 * @param deleted
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("change/{status}")
    public String updateStatusByBatch(HttpServletRequest request,
    		@PathVariable("status") Boolean deleted,
            @RequestParam(value = "ids", required = false) String[] ids,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        List<MaintainTemplate> templates=Lists.newArrayList();
        for(String id:ids){
        	MaintainTemplate template=new MaintainTemplate();
        	template.setId(id);
        	template.setDeleted(deleted);
        	templates.add(template);
        }
        
        BaseResult baseResult= templateService.batchUpdateTemplate(templates);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE,baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }
        return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
    }
	
	
	
	
	
	
}
