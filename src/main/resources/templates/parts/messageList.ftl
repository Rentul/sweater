<#include "security.ftl">
<div class="card-columns" id="message-list">
    <#list messages as message>
        <div class="card my-3" data-id="${message.id}">
            <div>
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top" />
                </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span><br />
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId || isAdmin>
                    <a href="/user-message-edit/${message.author.id}?message=${message.id}" class="btn btn-primary">
                        Edit
                    </a>
                </#if>
                <#if (message.author.id == currentUserId || isAdmin || message.author.isSubscribed(currentUserId)) && message.filename??>
                    <a href="/download-file/${message.author.id}?message=${message.id}" class="btn btn-primary">
                        Download File
                    </a>
                </#if>
                <#if message.author.id == currentUserId || isAdmin>
                    <a href="/delete-message/${message.author.id}?message=${message.id}" class="btn btn-primary">
                        Delete message
                    </a>
                </#if>
            </div>
        </div>
    <#else>
        <div class="form-group mt-3">
            No messages
        </div>
    </#list>
</div>