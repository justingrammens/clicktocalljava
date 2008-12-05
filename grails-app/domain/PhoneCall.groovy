class PhoneCall {
	
	String guid
	String status
	String description
	
	String source
	String destination
	
	String channelId
	
	User user
	
	static constraints = {
		//srcUniqueId(nullable:true)
		//destUniqueId(nullable:true)
		//callLength(nullable:true)
	}
	
}