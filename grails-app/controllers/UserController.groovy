import org.jsecurity.*
import org.jsecurity.crypto.hash.Sha1Hash

class UserController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ userList: User.list( params ) ]
    }

    def show = {
        def user = User.get( params.id )

        if(!user) {
            flash.message = "User not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ user : user ] }
    }

    def delete = {
        def user = User.get( params.id )
        if(user) {
            user.delete()
            flash.message = "User ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def user = User.get( params.id )
        if(!user) {
            flash.message = "User not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ user : user ]
        }
    }

    def update = {
        def user = User.get( params.id )
        if(user) {
            user.properties = params

			if (password_verify_matches(params) && params?.password != "" && params?.verify_password != "") {
				user.passwordHash = new Sha1Hash(params.password).toHex()
			} else {
				flash.message = "Passwords do not match!"
				render(view:'edit',model:[user:user])
				return
			}

            if(!user.hasErrors() && user.save()) {
                flash.message = "User ${params.id} updated"
                redirect(action:show,id:user.id)
            }
            else {
                render(view:'edit',model:[user:user])
            }
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def user = new User()
        user.properties = params
        return ['user':user]
    }

    def save = {
        def user = new User(params)
        if(!user.hasErrors() && user.save()) {
            flash.message = "User ${user.id} created"
            redirect(action:show,id:user.id)
        }
        else {
            render(view:'create',model:[user:user])
        }
    }

	def password_verify_matches(params) {
		def password = params.password
		def verify_password = params.verify_password
		println "HERE ${password} and ${verify_password}"
		if (password != verify_password) {
			return false
		}
		return true
	}
}
