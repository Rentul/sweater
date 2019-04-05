<#import "parts/common.ftl" as c>
<@c.page>
Analytics of users
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Amount of files</th>
        <th>Number of downloads</th>
        <th>Analytics</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.username}</td>
        <td>${user.messages.getSize()}</td>
        <td></td>
        <td><a href="/user/${user.id}">link</a></td>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>