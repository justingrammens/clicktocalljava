import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

	def adminRole
	def userRole
	
	def init = { servletContext ->
		
		def env = GrailsUtil.getEnvironment()
		
		if (GrailsApplication.ENV_DEVELOPMENT == env) 
		{
			printEnvironmentInfo(true)
			createRoles()
			createTestUsers()
		}
		else {
			printEnvironmentInfo(false)
		}
		
		/*def callerManager = CallerManager.getInstance()
		Thread t = new Thread(callerManager)
		t.start()*/
	}
	
	def createRoles()
	{
		adminRole = saveDomain(new Role(name: "Administrator"))
		userRole = saveDomain(new Role(name: "User"))
	}
	
	def createTestUsers()
	{
		def localtoneCompany = new Company(
			name: "Lcoaltone Interactive", 
			address: "123 Test St.", 
			city: "Minneapolis", 
			state: "Minnesota", 
			zip: "55401"
		)
		saveDomain(localtoneCompany)
		
		def adminUser = new User(
			username: "admin", 
			password: "admin", 
			email: "test@localtone.com", 
			apiKey: "TESTKEY",
			company: localtoneCompany
		)
		saveDomain(adminUser)
		saveDomain(new UserRoleRel(user: adminUser, role: adminRole))

		def tizone = new Company(
			name: "Tizone", 
			address: "1130 Tizone Ave.", 
			city: "Duluth", 
			state: "MN", 
			zip: "55613"
		)
		saveDomain(tizone)
		
		def testUser = new User(
			username: "test", 
			password: "test", 
			email: "test@tizone.com", 
			company: tizone
		)
		saveDomain(testUser)
		saveDomain(new UserRoleRel(user: testUser, role: userRole))
	}
	
    def destroy = {
    }

	def saveDomain(domain)
	{
		if (!domain?.save())
		{
			println "### ERROR Creating ${domain?.class?.name} in ApplicationBootStrap ###"
			domain?.errors?.allErrors?.each {
				println '\tError: ' + it
			}
		}
		return domain
	}

	def printEnvironmentInfo(dataInsert)
	{
		println ' --- --- --- '
		println ' Application: ClickToCallJava'
		println " Environment: ${GrailsUtil.getEnvironment()}"
		println " Inserting data? ${dataInsert ? 'YES' : 'NO'}"
		println ' --- --- --- '
	}
}