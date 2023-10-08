<!DOCTYPE html>
<html>
<head>
    <title>New Poll</title>
</head>
<body>
<c:url var="logoutUrl" value="/logout"/>
<form action="${logoutUrl}" method="post">
    <input type="submit" value="Log out" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<h2>Create a New Poll</h2>
    <form:form method="POST" enctype="multipart/form-data" modelAttribute="pollForm">
        <form:label path="body">Question:</form:label><br />
        <form:textarea path="body" rows="5" cols="30" /><br /><br />
        
        <form:label path="opt1">opt1</form:label><br />
        <form:input type="text" path="opt1" /><br /><br />

        <form:label path="opt2">opt2</form:label><br />
        <form:input type="text" path="opt2" /><br /><br />
        
        <form:label path="opt3">opt3</form:label><br />
        <form:input type="text" path="opt3" /><br /><br />
        
        <form:label path="opt4">opt4</form:label><br />
        <form:input type="text" path="opt4" /><br /><br />
        
        <input type="submit" value="Submit"/>
    </form:form>
</body>
</html>