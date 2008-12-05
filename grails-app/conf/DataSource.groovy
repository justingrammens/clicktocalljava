dataSource {
	pooled = true
	driverClassName = "com.mysql.jdbc.Driver"
	username = "sa"
	password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
            //loggingSql = true
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/clicktocalljava_development"
			username = "root"
			password = ""
        }
	}
	
	test {
		dataSource {
			// dbCreate = "update"
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			driverClassName = "org.hsqldb.jdbcDriver"
            url = "jdbc:hsqldb:mem:devDB"
		}
	}
	
	qa {
		dataSource {
			url = "jdbc:mysql://localhost/clicktocalljava_qa"
            //loggingSql = true
			username = "root"
			password = ""
        }
	}
	
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/clicktocalljava_production"
            username = "root"
            password = ""
        }
	}
}