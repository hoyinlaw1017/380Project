<!DOCTYPE html>
<html>
    <head>
        <title>Lecture</title>
    </head>
    <body>
        <security:authorize access="!isAuthenticated()">
        <form action="<c:url value="/login" />">
            <input type="submit" value="login" />
        </form>
        </security:authorize>
            
        <security:authorize access="isAuthenticated()"> 
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        </security:authorize>

        <h2>Welcome to COMPS380 Project Course page</h2>
        <security:authorize access="hasRole('LECTURER')"> 
            <a href="<c:url value="/user" />">Manage User Accounts (Lecturer Only)</a><br /><br />
        </security:authorize>
        <security:authorize access="hasRole('LECTURER')">
            <a href="<c:url value="/lecture/create" />">Create a Lecture (Lecturer Only)</a><br /><br />
        </security:authorize>
        <security:authorize access="hasRole('LECTURER')">
            <a href="<c:url value="/poll/create" />">Create a Poll (Lecturer Only)</a><br /><br />
        </security:authorize>
        <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/comment/list" />">Comment History</a><br /><br />
        </security:authorize>
        <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/comment/create" />">Create new comment</a><br /><br />
        </security:authorize>
        <h3>Lecture(s):</h3>
        <c:choose>
            <c:when test="${fn:length(lectureDatabase) == 0}">
                <i>There are no lectures in the system.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${lectureDatabase}" var="lecture">
                    - Lecture ${lecture.id}:
                    <a href="<c:url value="/lecture/view/${lecture.id}" />">
                        <c:out value="${lecture.subject}" /></a>
                    (created by: <c:out value="${lecture.userName}" />)
                    <security:authorize access="hasRole('LECTURER')">
                        [<a href="<c:url value="/lecture/edit/${lecture.id}" />">Edit</a>]
                    </security:authorize>
                    <security:authorize access="hasRole('LECTURER')">
                        [<a href="<c:url value="/lecture/delete/${lecture.id}" />">Delete</a>]
                    </security:authorize>
                    <br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
                    
        <h3>Polls:</h3>

        <c:choose>
            <c:when test="${fn:length(pollDatabase) == 0}">
                <i>There are no polls in the system.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${pollDatabase}" var="poll">
                    - Poll ${poll.id}:
                    <a href="<c:url value="/poll/view/${poll.id}" />">
                        <c:out value="${poll.body}" /></a>
                        <security:authorize access="hasRole('LECTURER')">
                        [<a href="<c:url value="/poll/delete/${poll.id}" />">Delete</a>]
                    </security:authorize>
                    <br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </body>
</html>