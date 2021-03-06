<#assign
    known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        isAnalytic = user.isAnalytic()
        currentUserId = user.getId()
    >
<#else>
    <#assign
        name = "guest"
        isAdmin = false
        isAnalytic = false
        currentUserId = -1
    >
</#if>