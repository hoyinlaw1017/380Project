<!DOCTYPE html>
<html>
    <head>
        <title>Lecture ${lectureID}</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Lecture #${lectureId}: <c:out value="${lecture.subject}" /></h2>
        <security:authorize access="hasRole('LECTURER')">
            [<a href="<c:url value="/lecture/edit/${lectureId}" />">Edit</a>]
        </security:authorize>
        <security:authorize access="hasRole('LECTURER')">
            [<a href="<c:url value="/lecture/delete/${lectureId}" />">Delete</a>]
        </security:authorize>
        <br /><br />
        <i>Lecturer Name - <c:out value="${lecture.userName}" /></i><br /><br />
        Description:</br>
        <c:out value="${lecture.body}" /><br /><br />
        <c:if test="${lecture.numberOfAttachments > 0}">
            Attachments:
            <c:forEach items="${lecture.attachments}" var="attachment"
                       varStatus="status">
                <c:if test="${!status.first}">, </c:if>
                <a href="<c:url value="/lecture/${lectureId}/attachment/${attachment.name}" />">
                    <c:out value="${attachment.name}" /></a>
            </c:forEach><br /><br />
        </c:if><br />


        <h2>Comments:</h2>

        <c:choose>
            <c:when test="${fn:length(commentDatabase) == 0}">
                <i>There is no comment here, be the first one?</i>
                <c:out value="${fn:length(commentDatabase)}" />
            </c:when>
            <c:otherwise>
                <c:forEach items="${commentDatabase}" var="comment">
                    # ${comment.id}:

                    (created by: <c:out value="${comment.username}" />)

                    <security:authorize access="hasRole('LECTURER')">
                        [<a href="<c:url value="/comment/delete/${comment.id}" />">Delete</a>]
                    </security:authorize>

                    <ul>
                        <li><c:out value="${comment.body}" /></li></ul>

                </c:forEach>
            </c:otherwise>
        </c:choose><br /><br />
        <a href="<c:url value="/comment/create" />">Create new comment</a><br /><br />
        <a href="<c:url value="/lecture" />">Return to Homepage</a>
    </body>
</html>