import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.*;
import org.asteriskjava.manager.response.ManagerResponse;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.*;


class AsteriskManagerService {

	boolean transactional = true

	def Map placeCall(source, destination, user) {
	
		def cm = CallerManager.getInstance()
		
		if (!cm.queue.containsKey(source)) {
 			def callResult = cm.placeCall(source, destination, user)
			def result = [guid: callResult.guid, status: callResult.status, description: callResult.description]
			return result
		} else {
			return [status: "Call to ${source} already being called. Guid is: " + cm.queue.get(source)]
		}
	}
	
	def Map checkStatus(guid) {
		
		def call = PhoneCall.findByGuid(guid)
		if (call) {
			def result = [guid: call.guid, status: call.status, description: call.description, source: call.source, destination: call.destination, user: call.user]
			println "CALL USER!: ${call.user}"
			return result
		} else {
			return [status: "No guid with id of : ${guid} being processed."]
		}
	}
}
