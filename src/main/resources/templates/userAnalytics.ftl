<#import "parts/common.ftl" as c>
<@c.page>
Analytics of user
<table>
    <thead>
    <tr>
        <th>Filename</th>
        <th>Number of downloads</th>
    </tr>
    </thead>
    <tbody>
    <#list userAnalyticsList as userAnalytics>
    <tr>
        <td>${userAnalytics.filename}</td>
        <td>${userAnalytics.downloads}</td>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>