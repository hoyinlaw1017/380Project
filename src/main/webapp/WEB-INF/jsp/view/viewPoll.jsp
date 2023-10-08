<!DOCTYPE html>
<html>
    <head>
        <title>Poll ${pollId}</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Poll #${pollId}:</h2>
        <security:authorize access="hasRole('LECTURER')">
            [<a href="<c:url value="/poll/delete/${pollId}" />">Delete</a>]
        </security:authorize>

        <br />

        <h3>Question: <c:out value="${poll.body}" /></h3>

        <form:form method="POST" enctype="multipart/form-data" modelAttribute="votePollForm">

            <form:hidden path="id" value="${pollId}" />
            <form:label path="opt">Options:</form:label><br/><br/>
            <form:radiobutton path="opt" value="opt1" required="required"/><c:out value="${poll.opt1}"/> (Now having <c:out value="${opt1Count}"/> votes)<br/>
            <form:radiobutton path="opt" value="opt2" required="required"/><c:out value="${poll.opt2}"/> (Now having <c:out value="${opt2Count}"/> votes)<br/>
            <c:if test="${not empty poll.opt3}" >
            <form:radiobutton path="opt" value="opt3" required="required"/><c:out value="${poll.opt3}"/> (Now having <c:out value="${opt3Count}"/> votes)<br/>
            </c:if>
            <c:if test="${not empty poll.opt4}" >
            <form:radiobutton path="opt" value="opt4" required="required"/><c:out value="${poll.opt4}"/> (Now having <c:out value="${opt4Count}"/> votes)<br/><br/>
            </c:if>
            <c:choose>
                <c:when test="${fn:length(pollAnswer.answer) == 0}">
                    <i>You haven't vote</i>
                </c:when>
                <c:otherwise>
                    <i>You have vote [<c:out value="${pollAnswer.answer}" />] for this poll, choose different Answer to vote again.</i>
                </c:otherwise>
            </c:choose>

            <br/><br/><input type="submit" value="Submit"/>
        </form:form>

        <br />


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

