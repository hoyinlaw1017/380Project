<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>

<h2>Create a User</h2>
<form:form method="POST" enctype="multipart/form-data" modelAttribute="lectureUser">
    <!-- UserName -->
    <form:label path="username">Username</form:label><br/>
    <form:input type="text" path="username" /><br/><br/>
    <!-- fullname -->
    <form:label path="fullname">Full Name</form:label><br/>
    <form:input type="text" path="fullname" /><br/><br/>
    <!-- Phone Number -->
    <form:label path="phonenum">Phone Number</form:label><br/>
    <form:input type="text" path="phonenum" /><br/><br/>
    <!-- Address -->
    <form:label path="address">Address</form:label><br/>
    <form:input type="text" path="address" /><br/><br/>
    <!-- Password -->
    <form:label path="password">Password</form:label><br/>
    <form:input type="text" path="password" /><br/><br/>
    <!-- User Roles -->
    <form:label path="roles">Roles</form:label><br/>
    <form:radiobutton path="roles" value="ROLE_STUDENT" required="required"/>ROLE_STUDENT
    <form:radiobutton path="roles" value="ROLE_LECTURER" required="required"/>ROLE_LECTURER
    <br /><br />
    <input type="submit" value="Add User"/>
</form:form>
</body>

</html>