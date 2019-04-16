<#import "parts/common.ftl" as c>
<@c.page>
Subscription requests
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Page</th>
        <th>Accept</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.username}</td>
        <td><a href="/user/${user.id}">go to profile</a></td>
        <th><a href="/user/accept-subscription/${user.id}">accept subscription</a></th>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>
