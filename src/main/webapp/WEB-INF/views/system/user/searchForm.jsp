<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="esform" uri="http://www.anjz.com/tags/es-form" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <%-- <esform:label path="search.id-in">编号</esform:label>
    <esform:input path="search.id-in" cssClass="input-small" placeholder="多个使用空格分隔"/>
    &nbsp;&nbsp; --%>

    <esform:label path="search.user_name-like">用户名</esform:label>
    <esform:input path="search.user_name-like" cssClass="input-small" placeholder="模糊匹配"/>
    &nbsp;&nbsp;

    <esform:label path="search.status-eq">状态</esform:label>
    <esform:select path="search.status-eq" cssClass="input-small">
        <esform:option label="所有" value=""/>
        <esform:options items="${statusList}" itemLabel="info"/>
    </esform:select>

    &nbsp;&nbsp;
    <input type="submit" class="btn" value="查询"/>
    <a class="btn btn-link accordion-toggle" data-toggle="collapse" href="#searchMore">高级查询</a>
    <a class="btn btn-link btn-clear-search">清空</a>


    <%--more--%>
    <div id="searchMore" class="accordion-body collapse">
        <div class="accordion-inner">
            <esform:label path="search.user_create_time-gte">创建时间从</esform:label>

            <div class="input-append date">
                <esform:input path="search.user_create_time-gte" cssClass="input-medium"
                              data-format="yyyy-MM-dd hh:mm:ss"
                              placeholder="大于等于"/>
                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
            </div>
            <esform:label path="search.user_create_time-lte">到</esform:label>
            <div class="input-append date">
                <esform:input path="search.user_create_time-lte" cssClass="input-medium"
                              data-format="yyyy-MM-dd hh:mm:ss"
                              data-position="bottom-left"
                              placeholder="小于等于"/>
                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
            </div>
        </div>
    </div>
</form>
