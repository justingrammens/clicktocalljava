import org.asteriskjava.live.*;
import org.asteriskjava.live.internal.*;

class ClickToCallCallback implements OriginateCallback {
	
	def guid	
	def user
	def source
	def destination
	def channelId
	
	def CALL_QUEUED = "Call queued"
	
	def void onBusy(AsteriskChannel channel) {
		def call = PhoneCall.findByGuid(guid)
		call.status = "500"
		call.description = "Busy"
		call.save(flush:true)
		removeFromQueue()
	}
	
	def void onDialing(AsteriskChannel channel) {
		channelId = channel.id
		def call = new PhoneCall(guid: guid, channelId: channelId, source: source, destination: destination, user: user, status: "100", description: CALL_QUEUED)
		call.save(flush:true)		
	}
	
	def void onFailure(LiveException cause) {
		def call = PhoneCall.findByGuid(guid)
		call.status = "300"
		call.description = "Failure"
		call.save(flush:true)
		removeFromQueue()	
	}
	
	def void onNoAnswer(AsteriskChannel channel) {
		def call = PhoneCall.findByGuid(guid)
		call.status = "200"
		call.description = "No Answer"
		call.save(flush:true)
		removeFromQueue()
	}
	
	def void onSuccess(AsteriskChannel channel) {
		def call = PhoneCall.findByGuid(guid)
		call.status = "400"
		call.description = "Answered"
		call.save(flush:true)
		removeFromQueue()	
	}
	
	def void removeFromQueue() {
		def cm = CallerManager.getInstance()
		cm.queue.remove(source)
	}
	
}