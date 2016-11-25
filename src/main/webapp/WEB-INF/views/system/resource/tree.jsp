<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<%@include file="/WEB-INF/views/common/import-zTree-css.jspf"%>


<ul class="nav nav-tabs">
    <li ${empty param['search.is_show|eq'] ? 'class="active"' : ''}>
        <a href="${ctx}/system/resource/tree?async=${not empty param.async and param.async eq true}">
            <i class="icon-table"></i>
            所有
            <i class="icon-refresh" title="点击刷新"></i>
        </a>
    </li>
    <li ${not empty param['search.is_show|eq'] ? 'class="active"' : ''}>
        <a href="${ctx}/system/resource/tree?search.is_show|eq=1&async=${not empty param.async and param.async eq true}">
            <i class="icon-table"></i>
            显示的
        </a>
    </li>
</ul>

<es:contentFooter/>
<%@include file="/WEB-INF/views/common/import-zTree-js.jspf"%>
<script type="text/javascript">
    var async = ${not empty param.async and param.async eq true};
    $(function() {
        var zNodes =[
            <c:forEach items="${trees}" var="m">
            { id:"${m.id}", pId:"${m.pId}", name:"${m.name}", iconSkin:"${m.iconSkin}", open: true, root : ${m.root},isParent:${m.isParent}},
            </c:forEach>
        ];


        $.zTree.initMovableTree({
            zNodes : zNodes,
            urlPrefix : "${ctx}/system/resource",
            async : async,
            onlyDisplayShow:${param['search.is_show|eq'] eq 1},
            permission: <es:treePermission resourceIdentity="system:resource"/>,
            autocomplete : {
                enable : true
            },
            
            setting : {
                callback : {
                    onClick: function(event, treeId, treeNode, clickFlag) {
                        parent.frames['listFrame'].location.href='${ctx}/system/resource/' + treeNode.id + "/update?async=" + async ;
                    }
                }
            }
        });

    });
</script>