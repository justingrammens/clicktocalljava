import org.jsecurity.SecurityUtils

class SecurityFilters 
{
	def filters = {
		
		// Ensure that all controllers and actions require an authenticated user,
		// except for the public controllers
		auth(controller: "*", action: "*") {
			before = {
				// Exclude the "public" controller.
				if (controllerName == "public") return true

				if (controllerName == "location" || (controllerName == "user" && actionName == "apiKey") ) return true
				// This just means that the user must be authenticated. He does
				// not need any particular role or permission.
				accessControl { true }
			}
		}

		// Creating, modifying, or deleting a company requires the "Administrator" role.
		companyEditing(controller: "company", action: "(create|edit|save|update|delete)") {
			before = {
				accessControl {
					role("Administrator")
				}
			}
		}

		// Showing a company requires the "Administrator" *or* the "User" roles.
		companyShow(controller: "company", action: "show") {
			before = {
				accessControl {
					role("Administrator") || role("User")
				}
			}
		}

		userProfile(controller:"user", action:"profile") {
			before = {
				accessControl {
					role("User") || role("Administrator")
				}
			}
		}

		userInRequest(controller:"*", action:"*") {
			before = {
				def subject = SecurityUtils.getSubject()
				if (subject && subject?.principal) {
					request.user = User.findByUsername(subject.principal)
				}
			}
		}
	}
}