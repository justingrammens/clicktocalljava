class Company {
	
	String name
	String address
	String city
	String state
	String zip

	static constraints = {
		name(blank:false, unique:true)
		address(blank:false)
		city(blank:false)
		state(blank:false)
		zip(blank:false)
	}
	
}
