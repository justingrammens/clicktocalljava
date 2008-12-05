import java.io.IOException

import org.asteriskjava.manager.AuthenticationFailedException
import org.asteriskjava.manager.ManagerConnection
import org.asteriskjava.manager.ManagerConnectionFactory
import org.asteriskjava.manager.TimeoutException
import org.asteriskjava.manager.action.*
import org.asteriskjava.manager.response.ManagerResponse
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.asteriskjava.manager.event.*
import org.asteriskjava.manager.AbstractManagerEventListener
import org.asteriskjava.live.internal.AsteriskServerImpl
import java.util.*
import org.asteriskjava.live.*;

// status -
// 1 - call queued
// 2 - calling source
// 3 - calling desitnation
// 4 - call in progress
// 5 - call complete
// 0 - call in error

//extends AbstractManagerEventListener implements Runnable

class CallerManager 
{
	ManagerConnection managerConnection
	static CallerManager myself;
	
	def loggedIn
	def availChars = []
	static HashMap queue 
	
	def static CallerManager getInstance()
	{
		if (myself == null) {
			myself = new CallerManager("AsteriskManager")
			queue = new HashMap()
		}
		return myself
	}
	
	def CallerManager(str) throws IOException
	{	
		// setup the available characters for random guid
		('A'..'Z').each { availChars << it.toString() }  
	 	// even it out to about the same odds of getting a char or a number  
		3.times { (0..9).each { availChars << it.toString() } }
		
		// get the configuration from the configuration in config.groovy
		def user = ConfigurationHolder.config.amiservice.ami_user
		def password = ConfigurationHolder.config.amiservice.ami_password
		def astrisk_server = ConfigurationHolder.config.amiservice.astrisk_server

		ManagerConnectionFactory factory = new ManagerConnectionFactory("${astrisk_server}", "${user}", "${password}")		
		managerConnection = factory.createManagerConnection();	
	}

	def PhoneCall placeCall(source, destination, user) 
	{
		def action = createOriginateAction(source, destination)
		
		AsteriskServerImpl impl = new AsteriskServerImpl(managerConnection)
		
		ClickToCallCallback cb = new ClickToCallCallback()
		
		cb.user = user
		cb.source = source
		cb.destination = destination
		def guid = generateRandomString(5) 
		cb.guid = guid
		
		impl.originateAsync(action, cb)
		
		def result = [guid: guid, status: "100", description: cb.CALL_QUEUED]

		queue.put(source, guid)
		
		return result
	}
	
	def createOriginateAction(source, destination) {
		OriginateAction originateAction = new OriginateAction()
		originateAction.setChannel("${source}")
		originateAction.setVariable("source", "${source}")
		originateAction.setContext("dialme")
		originateAction.setVariable("destination", "${destination}")
		originateAction.setCallerId("${destination}")
		originateAction.setPriority(new Integer(1));
		originateAction.setTimeout(new Integer(30000));
		originateAction.setAsync(true)
		return originateAction
	}
		
	def generateRandomString = { length ->   
		def max = availChars.size      
	   	def rnd = new Random()  
		def sb = new StringBuilder()  
	   	length.times { sb.append(availChars[rnd.nextInt(max)]) }  
	  	sb.toString()  
	}
	
}