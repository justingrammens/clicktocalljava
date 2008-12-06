import javax.naming.AuthenticationException
import javax.naming.Context
import javax.naming.directory.BasicAttribute
import javax.naming.directory.BasicAttributes
import javax.naming.directory.InitialDirContext

import org.jsecurity.authc.AccountException
import org.jsecurity.authc.IncorrectCredentialsException
import org.jsecurity.authc.UnknownAccountException

/**
 * Simple realm that authenticates users against an LDAP server.
 */
class @realm.name@ {
    static authTokenClass = org.jsecurity.authc.UsernamePasswordToken

    def grailsApplication

    def authenticate(authToken) {
        log.info "Attempting to authenticate ${authToken.username} in LDAP realm..."
        def username = authToken.username

        // Null username is invalid
        if (username == null) {
            throw new AccountException('Null usernames are not allowed by this realm.')
        }

        // Get LDAP config for application. Use defaults when no config
        // is provided.
        def appConfig = grailsApplication.config
        def ldapUrl = appConfig.ldap.server.url ?: "ldap://localhost:389/"
        def searchBase = appConfig.ldap.search.base ?: ""
        def usernameAttribute = appConfig.ldap.username.attribute ?: "uid"

        // Connect to the LDAP server so we can search against it.
        def env = new Hashtable()
        env[Context.INITIAL_CONTEXT_FACTORY] = "com.sun.jndi.ldap.LdapCtxFactory"
        env[Context.PROVIDER_URL] = ldapUrl

        def ctx = new InitialDirContext(env)

        // Look up the DN for the LDAP entry that has a 'uid' value
        // matching the given username.
        def matchAttrs = new BasicAttributes(true)
        matchAttrs.put(new BasicAttribute(usernameAttribute, username))

        def result = ctx.search(searchBase, matchAttrs)
        if (!result.hasMore()) {
            throw new UnknownAccountException("No account found for user [${username}]")
        }

        // Now connect to the LDAP server again, but this time use
        // authentication.
        def searchResult = result.next()
        env[Context.SECURITY_AUTHENTICATION] = "simple"
        env[Context.SECURITY_PRINCIPAL] = searchResult.nameInNamespace
        env[Context.SECURITY_CREDENTIALS] = authToken.password

        try {
            new InitialDirContext(env)
            return username
        }
        catch (AuthenticationException ex) {
            log.info "Invalid password"
            throw new IncorrectCredentialsException("Invalid password for user '${username}'")
        }
    }
}
