<!DOCTYPE html>
<html>
    <head>
        <title>Comments</title></head>
    <body>      

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
        <a href="<c:url value="/comment/create" />">Add New Comment</a><br /><br />
        <a href="<c:url value="/" />">Return to Homepage</a><br /><br />
    </body>
</html>