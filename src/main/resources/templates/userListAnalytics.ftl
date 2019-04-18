<#import "parts/common.ftl" as c>
<@c.page>
Analytics of users
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Amount of messages</th>
        <th>Amount of files</th>
        <th>Number of downloads</th>
        <th>Analytics</th>
    </tr>
    </thead>
    <tbody>
    <#list userAnalyticsList as userAnalytics>
    <tr>
        <td>${userAnalytics.username}</td>
        <td>${userAnalytics.numberOfMessages}</td>
        <td>${userAnalytics.amountOfFiles}</td>
        <td>${userAnalytics.numberOfDownloads}</td>
        <td><a href="/user-analytics/${userAnalytics.id}">link</a></td>
    </tr>
    </#list>
    <tr>
        <td>Total:</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>${userAnalyticsTotal.userCount}</td>
        <td>${userAnalyticsTotal.messageCount}</td>
        <td>${userAnalyticsTotal.fileCount}</td>
        <td>${userAnalyticsTotal.downloadCount}</td>
        <td>-</td>
    </tr>
    </tbody>
</table>
</@c.page>