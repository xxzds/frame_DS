package com.anjz.core.controller.personal;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.AvoidDuplicateSubmission;
import com.anjz.base.bind.annotation.CurrentUser;
import com.anjz.base.bind.annotation.PageableDefaults;
import com.anjz.base.controller.BaseController;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.core.enums.MessageState;
import com.anjz.core.model.PersonalMessage;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.result.BaseResult;
import com.anjz.util.UuidUtil;

/**
 * 我的消息,因当前用户操作，不需要权限控制
 * @author ding.shuai
 * @date 2016年9月18日下午12:15:06
 */
@Controller
@RequestMapping("personal/message")
public class MessageController extends BaseController<PersonalMessage, String>{
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private PersonalMessageService personalMessageService;
	
	
	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("states", MessageState.values());
	}

	
	/**
	 * 主页
	 */
	@RequestMapping(value = "{state}/list", method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String list(@CurrentUser SysUser user,
            @PathVariable("state") MessageState state,
            Pageable pageable,
            Model model) {

        model.addAttribute("state", state);
        
        model.addAttribute("page",personalMessageService.findUserMessage(user.getId(), state, pageable));
        
        setCommonData(model);
        return viewName("list");
    }

    /**
     * 仅返回表格数据
     *
     * @return
     */
    @RequestMapping(value = "{state}/list", method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String listTable(@CurrentUser SysUser user,
                            @PathVariable("state") MessageState state,
                            Pageable pageable,
                            Model model) {
        list(user, state, pageable, model);
        return viewName("listTable");
    }
    
    
    /**
     * 发送新消息
     * @param model
     * @return
     */
    @AvoidDuplicateSubmission(needSaveToken=true)
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String showSendForm(Model model) {
    	   	
        if (!model.containsAttribute("m")) {
            model.addAttribute("m", newModel());
        }
        model.addAttribute(Constants.OP_NAME, "发送新消息");
        return viewName("sendForm");
    }
    
    @AvoidDuplicateSubmission(needRemoveToken=true)
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String send(
            @CurrentUser SysUser user,
            @ModelAttribute("m") PersonalMessage message,
            BindingResult result,
            @RequestParam(value = "receiver", required = false) String receiverUsername,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        SysUser receiver = sysUserService.findByUsername(receiverUsername).getData();
        if (receiver == null) {
            result.rejectValue("receiverId", "receiver.not.exists");
        }
        if (user.equals(receiver)) {
            result.rejectValue("receiverId", "receiver.not.self");
        }

        if (result.hasErrors()) { 
        	request.getSession(false).setAttribute("token", UuidUtil.generateUuid32());        
            return showSendForm(model);
        }
        message.setReceiverId(receiver.getId());
        message.setSenderId(user.getId());
        personalMessageService.send(message);


        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "发送成功！");
        return redirectToUrl(viewName(MessageState.out_box + "/list"));
    }
    
    /**
     * 查看消息
     */
    @RequestMapping("{id}")
    public String view(@CurrentUser SysUser user, @PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
    	PersonalMessage message=personalMessageService.findOne(id).getData();
        if (message==null) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "您查看的消息不存在");
            return redirectToUrl("list");
        }

        if (user.getId().equals(message.getReceiverId())) {
            personalMessageService.markRead(message);
        }

        //得到消息之前的 和 之后的
        List<PersonalMessage> messages = personalMessageService.findAncestorsAndDescendants(message);
        model.addAttribute("messages", messages);

        model.addAttribute("m", message);
        return viewName("view");
    }
    
    
    /**
     * 展开消息的内容
     */
    @RequestMapping("{id}/content")
    public String viewContent(@CurrentUser SysUser user, @PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
    	PersonalMessage m=personalMessageService.findOne(id).getData();
        if (user.getId().equals(m.getReceiverId())) {
        	personalMessageService.markRead(m);
        }
        model.addAttribute("m", m);
        return viewName("viewContent");
    }
    
    /**
     * 回复
     */
    @AvoidDuplicateSubmission(needSaveToken=true)
    @RequestMapping(value = "/{parentId}/reply", method = RequestMethod.GET)
    public String showReplyForm(@PathVariable("parentId") String parentId, Model model) {
    	PersonalMessage parent = personalMessageService.findOne(parentId).getData();
        if (!model.containsAttribute("m")) {
        	PersonalMessage m = newModel();
            m.setParentId(parent.getId());
            m.setParentIds(parent.getParentIds());
            m.setReceiverId(parent.getSenderId());
            m.setTitle(Constants.REPLY_PREFIX + parent.getTitle());
            model.addAttribute("m", m);
        }
        model.addAttribute("parent", parent);
        model.addAttribute(Constants.OP_NAME, "回复消息");
        return viewName("sendForm");
    }
    
    @AvoidDuplicateSubmission(needRemoveToken=true)
    @RequestMapping(value = "/{parentId}/reply", method = RequestMethod.POST)
    public String reply(
            @CurrentUser SysUser user,
            @PathVariable("parentId") String parentId,
            @ModelAttribute("m") PersonalMessage m, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,HttpServletRequest request) {

        if (result.hasErrors()) {
        	request.getSession(false).setAttribute("token", UuidUtil.generateUuid32());  
            return showReplyForm(parentId, model);
        }
        m.setSenderId(user.getId());
        personalMessageService.send(m);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "回复成功！");
        return redirectToUrl(viewName(MessageState.out_box + "/list"));
    }
    
    /**
     * 转发
     */
    @AvoidDuplicateSubmission(needSaveToken=true)
    @RequestMapping(value = "/{parentId}/forward", method = RequestMethod.GET)
    public String showForwardForm(@PathVariable("parentId") String parentId, Model model) {
    	PersonalMessage parent = personalMessageService.findOne(parentId).getData();
    	
        if (!model.containsAttribute("m")) {
        	String receiverUsername = sysUserService.findOne(parent.getReceiverId()).getData().getUserName();
            String senderUsername = sysUserService.findOne(parent.getSenderId()).getData().getUserName();
            
        	PersonalMessage m = newModel();
            m.setTitle(Constants.FOWRARD_PREFIX + parent.getTitle());
            m.setContent(
                    String.format(
                    		Constants.FOWRARD_TEMPLATE,
                            senderUsername,
                            receiverUsername,
                            parent.getTitle(),
                            parent.getContent()
                    ));
            model.addAttribute("m", m);
        }
        model.addAttribute("parent", parent);
        model.addAttribute(Constants.OP_NAME, "转发消息");
        return viewName("sendForm");
    }
    
    @AvoidDuplicateSubmission(needRemoveToken=true)
    @RequestMapping(value = "/{parentId}/forward", method = RequestMethod.POST)
    public String forward(
            @CurrentUser SysUser user,
            @RequestParam(value = "receiver", required = false) String receiverName,
            @PathVariable("parentId") String parentId,
            @ModelAttribute("m") PersonalMessage m, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,HttpServletRequest request) {
        SysUser receiver = sysUserService.findByUsername(receiverName).getData();
        if (receiver == null) {
            result.rejectValue("receiverId", "receiver.not.exists");
        }

        if (user.equals(receiver)) {
            result.rejectValue("receiverId", "receiver.not.self");
        }

        if (result.hasErrors()) {
        	request.getSession(false).setAttribute("token", UuidUtil.generateUuid32()); 
            return showForwardForm(parentId, model);
        }
        m.setReceiverId(receiver.getId());
        m.setSenderId(user.getId());
        personalMessageService.send(m);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "转发成功！");
        return redirectToUrl(viewName(MessageState.out_box + "/list"));
    }
    
    /**
     * 保存草稿
     */
    @RequestMapping(value = "draft/save", method = RequestMethod.POST)
    public String saveDraft(
            @CurrentUser SysUser user,
            @RequestParam(value = "receiver", required = false) String receiverName,
            @ModelAttribute("m") PersonalMessage m,BindingResult result,
            RedirectAttributes redirectAttributes) {

    	SysUser receiver = sysUserService.findByUsername(receiverName).getData();
        if (receiver != null) {
            m.setReceiverId(receiver.getId());
        }
        m.setSenderId(user.getId());

        BaseResult baseResult = personalMessageService.saveDraft(m);
        
        if(!baseResult.isSuccess()){
        	 redirectAttributes.addFlashAttribute(Constants.ERROR, baseResult.getMessage());
        }else{
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, "保存草稿成功！");
        }
        return redirectToUrl(viewName(MessageState.draft_box + "/list"));
    }
    
    /**
     * 发送草稿
     */
    @RequestMapping(value = "draft/{id}/send", method = RequestMethod.GET)
    public String showResendDraftForm(@PathVariable("id") String id, Model model) {
    	PersonalMessage m=personalMessageService.findOne(id).getData();
        if (m.getReceiverId() != null) {
            SysUser user = sysUserService.findOne(m.getReceiverId()).getData();
            if (user != null) {
                model.addAttribute("receiver", user.getUserName());
            }
        }
        model.addAttribute("m", m);
        String viewName = showSendForm(model);
        model.addAttribute(Constants.OP_NAME, "发送草稿");
        return viewName;
    }
    
    @RequestMapping(value = "draft/{id}/send", method = RequestMethod.POST)
    public String resendDraft(
            @CurrentUser SysUser user,
            @ModelAttribute("m") PersonalMessage m,
            BindingResult result,
            @RequestParam(value = "receiver", required = false) String receiver,
            Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        String viewName = send(user, m, result, receiver, model, redirectAttributes,request);
        model.addAttribute(Constants.OP_NAME, "发送草稿");
        return viewName;
    }
    
    /**
     * 收藏消息
     */
    @RequestMapping("batch/store")
    public String batchStore(
            @CurrentUser SysUser user,
            @RequestParam(value = "ids", required = false) String[] ids,
            RedirectAttributes redirectAttributes) {

        personalMessageService.store(user.getId(), ids);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "收藏成功！");
        return redirectToUrl(viewName(MessageState.store_box + "/list"));
    }
    
    /**
     * 清空
     */
    @RequestMapping("clear/{state}")
    public String clear(
            @CurrentUser SysUser user,
            @PathVariable("state") MessageState state,
            RedirectAttributes redirectAttributes) {

    	personalMessageService.clearBox(user.getId(), state);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, String.format("清空%s成功！", state.getInfo()));
        return redirectToUrl(viewName(MessageState.trash_box + "/list"));
    }
    
    /**
     * 标记为已读
     */
    @RequestMapping("markRead")
    public String markRead(
            @CurrentUser SysUser user,
            @RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam("BackURL") String backURL,
            RedirectAttributes redirectAttributes) {

    	personalMessageService.markRead(user.getId(), ids);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功标记为已读！");
        return redirectToUrl(backURL);
    }
    
    /**
     * 移动到垃圾箱
     * @param user
     * @param ids
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("batch/recycle")
    public String batchRecycle(
            @CurrentUser SysUser user,
            @RequestParam(value = "ids", required = false) String[] ids,
            RedirectAttributes redirectAttributes) {

        personalMessageService.recycle(user.getId(), ids);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "移动到垃圾箱成功！");
        return redirectToUrl(viewName(MessageState.trash_box + "/list"));
    }

    /**
     * 逻辑删除垃圾箱中的数据
     */
    @RequestMapping("batch/delete")
    public String batchDelete(
            @CurrentUser SysUser user,
            @RequestParam(value = "ids", required = false) String[] ids,
            RedirectAttributes redirectAttributes) {

    	personalMessageService.delete(user.getId(), ids);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功！");
        return redirectToUrl(viewName(MessageState.trash_box + "/list"));
    }
}
