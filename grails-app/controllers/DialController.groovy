class DialController {

	def asteriskManagerService

	def index = {
	}
	
	def show = {
		def result = asteriskManagerService.checkStatus(params.id)
		
		render(contentType: "text/xml", encoding: "UTF-8") 
		{
			Result('xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance', 'xml:ns': 'urn:localtone:clicktocalljava', 'xsi:schemaLocation': 'http://api.localtoneinteractive.com/ClickToCallJava/V1/DialResponse.xsd') 
			{
				Guid(result.guid)
				Status(result.status)
				Description(result.description)
				Source(result.source)
				Destination(result.destination)
				User(result.user.username)
			}
		}		
	}

	def create = {
		def messages = []
		
		if (hasValidParameters(params, messages) && isValidUser(params, messages)) 
		{
			renderResultSet(params)
		}
		else {
			renderErrors(messages)
		}		
	}
	
	private void renderResultSet(params)
	{
		def source = params.source
		def destination = params.destination
		def user = User.findByApiKey(params.apiKey)
		
		def result = asteriskManagerService.placeCall(source, destination, user)
		
		/*
		<result>
			<guid>123abc</guid>
			<status>100</status>
		</result>
		*/
		render(contentType: "text/xml", encoding: "UTF-8") 
		{
			// Need to set renderResult delegate to the same as render delegate
			//renderResult.delegate = delegate
			
			Result('xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance', 'xml:ns': 'urn:localtone:clicktocalljava', 'xsi:schemaLocation': 'http://api.localtoneinteractive.com/ClickToCallJava/V1/DialResponse.xsd') 
			{
				Guid(result.guid)
				Status(result.status)
				Description(result.description)
			}
		}
	}
		
	private boolean isValidUser(params, messages)
	{
		def user = User.findByApiKey(params.apiKey)
		if (user)
		{
			return true
		} else {
			messages.add("APIKey : " + params.apiKey + " is invalid.")
		}
		return false
	}
	
	
	private boolean hasValidParameters(params, messages)
	{
		boolean valid = true
		
		if (params.source == null) {
			messages.add("You must specify a source number.")
			valid = false
		}
		
		if (params.destination == null) {
			messages.add("You must specify a destination number.")
			valid = false
		}
				
		if (params.apiKey == null) {
			messages.add("You must specify your apiKey")
			valid = false
		}
		
		return valid
	}
	
	private void renderErrors(messages)
	{
		// output any error messages if they have been input
		if (messages.size() > 0) 
		{
			render(contentType: "text/xml", encoding: "UTF-8") 
			{
				Error('xml:ns': 'urn:localtone:clicktocalljava') {
					messages.each {
						Message(it)
					}
				}
			}
		}
	}
}