

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>User List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
        </div>
        <div class="body">
            <h1>User List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="username" title="Username" />
                        
                   	        <g:sortableColumn property="passwordHash" title="Password Hash" />
                        
                   	        <g:sortableColumn property="apiKey" title="Api Key" />
                        
                   	        <g:sortableColumn property="email" title="Email" />
                        
                   	        <th>Company</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${userList}" status="i" var="user">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${user.id}">${fieldValue(bean:user, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:user, field:'username')}</td>
                        
                            <td>${fieldValue(bean:user, field:'passwordHash')}</td>
                        
                            <td>${fieldValue(bean:user, field:'apiKey')}</td>
                        
                            <td>${fieldValue(bean:user, field:'email')}</td>
                        
                            <td>${fieldValue(bean:user, field:'company')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${User.count()}" />
            </div>
        </div>
    </body>
</html>
