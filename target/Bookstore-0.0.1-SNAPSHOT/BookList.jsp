<%@ page import="net.codejava.javaee.bookstore.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Books Store Application</title>
</head>
<body>
	<center>
		<h1>Books Management</h1>
        <h2>
        	<a href="new">Add New Book</a>
        	&nbsp;&nbsp;&nbsp;
        	<a href="list">List All Books</a>
        	
        </h2>
	</center>
    <div align="center">
        <%
            HttpSession sessions = request.getSession();
            User user = (User) sessions.getAttribute("user");
            if (user==null) {%>
           <caption> <h2 > List of Books</h2 ></caption >
            <h1 > No user logged in( roam around the food court until you are authenticated)</h1 >
        <a href="login.jsp">Login</a>
        <%} else {%>
        <c:set var="currentUser"  scope="session" value = "${id}"/>
        <table border="1" cellpadding="5">
            <caption><h2>List of Books</h2></caption>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Price</th>
                <th>Actions</th>
            </tr>

            <c:forEach var="book" items="${listBook}">
                <tr>
                    <td><c:out value="${book.id}" /></td>
                    <td><c:out value="${book.title}" /></td>
                    <td><c:out value="${book.author}" /></td>
                    <td><c:out value="${book.price}" /></td>
                    <td>
                        <c:if test="${book.id==currentUser}">
                    	<a href="edit?id=<c:out value='${book.id}' />">Edit</a>
                    	<a href="delete?id=<c:out value='${book.id}' />">Delete</a>
                        </c:if>

                    </td>
                </tr>
            </c:forEach>
        </table>
        <a href="logout">Logout</a>
        <%}%>

    </div>	
</body>
</html>