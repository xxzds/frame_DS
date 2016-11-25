<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.MaintainDictionary" %>
<%@ tag import="com.anjz.core.service.intf.maintain.MaintainDictionaryService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的字典的id" %>
<%@ attribute name="needLink" type="java.lang.Boolean" required="false" description="是否需要链接" %>
<%!private MaintainDictionaryService dictionaryService;%>
<%

    if(dictionaryService == null) {
    	dictionaryService = SpringUtils.getBean(MaintainDictionaryService.class);
    }

	MaintainDictionary dictionary = dictionaryService.findOne(id).getData();

    if(dictionary == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }
    String name = dictionary.getName();
    String availableInfo = (Boolean.FALSE.equals(dictionary.getIsShow()) ? "<span class='label label-important'>[模板类型不可用]</span>" : "");
    if(Boolean.FALSE.equals(needLink)) {
        out.write(name+availableInfo);
        return;
    }
    out.write(
            String.format(
                    "<a class='btn btn-link no-padding' href='%s/maintain/dictionary/%s'>%s</a>%s",
                    request.getContextPath(),
                    id,
                    name,
                    availableInfo));
%>
