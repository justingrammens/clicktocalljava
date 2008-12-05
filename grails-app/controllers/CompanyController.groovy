class CompanyController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ companyList: Company.list( params ) ]
    }

    def show = {
        def company = Company.get( params.id )

        if(!company) {
            flash.message = "Company not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ company : company ] }
    }

    def delete = {
        def company = Company.get( params.id )
        if(company) {
            company.delete()
            flash.message = "Company ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Company not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def company = Company.get( params.id )

        if(!company) {
            flash.message = "Company not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ company : company ]
        }
    }

    def update = {
        def company = Company.get( params.id )
        if(company) {
            company.properties = params
            if(!company.hasErrors() && company.save()) {
                flash.message = "Company ${params.id} updated"
                redirect(action:show,id:company.id)
            }
            else {
                render(view:'edit',model:[company:company])
            }
        }
        else {
            flash.message = "Company not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def company = new Company()
        company.properties = params
        return ['company':company]
    }

    def save = {
        def company = new Company(params)
        if(!company.hasErrors() && company.save()) {
            flash.message = "Company ${company.id} created"
            redirect(action:show,id:company.id)
        }
        else {
            render(view:'create',model:[company:company])
        }
    }
}
