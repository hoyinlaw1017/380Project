<!DOCTYPE html>
<html>
    <head>
        <title>new comment</title>
    </head>
    <body>
    <c:url var="logoutUrl" value="/logout"/>
    <form action="${logoutUrl}" method="post">
        <input type="submit" value="Log out" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

<h2>Add a new comment</h2>
    <form:form method="POST" enctype="multipart/form-data" modelAttribute="commentForm">
       
        <form:label path="body">Comment:</form:label><br />
        <form:textarea path="body" rows="5" cols="30" /><br /><br />
        
        <input type="submit" value="Submit"/>
    </form:form>
    </body>
</html>
