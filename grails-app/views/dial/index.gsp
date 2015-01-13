<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Create User</title>         
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
	</div>

	<div class="body">
		<h1>Create Call</h1>
		<p>
			Enter the follow information to dial and bridge the two numbers together.
		</p>
			<g:form action="create">
			<p>
				Your phone number - Replace the phone number with yours. <input type="text" name="source" value="9999999999"/>
			</p>
			<p>
				The phone number you wish to connect to  - Replace the phone number with yours. <input type="text" name="destination" value="9999999999"/>
			</p>
			<p>
				The API Key for this service call.  <input type="text" name="apiKey" value="TESTKEY" />
			</p>
			<p>			
				<input type="submit" name="Dial!" value="Dial!"/>
			</p>
		</g:form>
	</div>
</body>
</html>
