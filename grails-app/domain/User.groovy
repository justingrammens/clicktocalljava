import org.jsecurity.*
import org.jsecurity.crypto.hash.Sha1Hash

class User {
	
	static transients = [ "password", "verify_password" ]
	
	String username
	String passwordHash
	String apiKey
	String email
	String password
	String verify_password
	Company company
	
	static constraints = {
		username(blank: false)
		passwordHash(nullable:true, blank:true)
		apiKey(nullable:true, blank:true)
		email(email:true, unique:true, blank:false)
	}

	def isAdmin = {
		def subject = SecurityUtils.getSubject()
		return subject.hasRole("Administrator")
	}

	def isUser = {
		def subject = SecurityUtils.getSubject()
		return subject.hasRole("User")
	}
	
	def beforeInsert = {
		passwordHash = new Sha1Hash(this.password).toHex()
		createAPIKey()
	}

	def createAPIKey() 
	{
		Random randVal = new Random()
		//return Long.toHexString( randVal.nextLong() & Long.MAX_VALUE ) + Long.toHexString( randVal.nextLong() & Long.MAX_VALUE )
		apiKey = Long.toHexString( randVal.nextLong() & Long.MAX_VALUE ) + Long.toHexString( randVal.nextLong() & Long.MAX_VALUE )
	}

}
