<!DOCTYPE html>
<html>
<head>
    <title>update User</title>
</head>
<body>

<h2>update User</h2>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form method="POST" enctype="multipart/form-data" modelAttribute="lectureUser">
    <!-- UserName -->
    <form:label path="username">Username</form:label><br/>
    <form:input type="text" path="username" value="${userList[0]}"/><br/><br/>
    <!-- fullname -->
    <form:label path="fullname">Full Name</form:label><br/>
    <form:input type="text" path="fullname" value="${userList[1]}"/><br/><br/>
    <!-- Phone Number -->
    <form:label path="phonenum">Phone Number</form:label><br/>
    <form:input type="text" path="phonenum" value="${userList[2]}"/><br/><br/>
    <!-- Address -->
    <form:label path="address">Address</form:label><br/>
    <form:input type="text" path="address" value="${userList[3]}"/><br/><br/>
    <!-- Password -->
    <form:label path="password">Password</form:label><br/>
    <form:input type="text" path="password" value="${userList[4]}"/><br/><br/>
    <!-- User Roles -->
    <form:label path="roles">Roles</form:label><br/>
    <form:radiobutton path="roles" value="ROLE_STUDENT" required="required"/>ROLE_STUDENT
    <form:radiobutton path="roles" value="ROLE_LECTURER" required="required"/>ROLE_LECTURER
    <br /><br />
    <input type="submit" value="edit User"/>
</form:form>
</body>



</html>