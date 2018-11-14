# Nucifera-Chat
### Datasource configuration
version: 2018-11-14 22:58

#### Table of contents
* [Description](#Description)
* [Datasource prerequities](#datasource-prerequisites)
* [Example with PostgreSQL](#example-with-postgresql)
	* [Driver](#driver)
	* [Connection pool](#connection-pool)
	* [Resource](#resource)

### Description
JDBC Datasource is a database connection that a JEE application can use. Nucifera-Chat needs a properly configured datasource.

This document contains an example connection configuration with Glassfish 5 and PostgreSQL 11, but the process is similar on other platforms.

### Datasource prerequisites

You will need a properly configured database with an account that can login, and create or modify tables. Check your database vendor and version to be sure which driver to use.

If this account has ALL WITH GRANT OPTION privileges, JPA should automatically create tables.

### Example with PostgreSQL
Provided we have our database set up, we will configure the datasource.

#### Driver
Prepare your database driver. For PostgreSQL you can download one from [the official page](https://jdbc.postgresql.org/). We'll be using version 42.2.5, which is the most recent one as of 2018-11-14.
Put this driver in your drivers folder. For Glassfish 5, it would be `glassfish/domains/${domain}/lib`. Then restart your server.

#### Connection pool
Connect to your Glassfish admin console (usually `http://localhost:4848`) and go to 
`Resources` > `JDBC` > `JDBC Connection Pools`. Create a new connection pool of type `java.sql.Driver` and select `Postgresql` as the Database Driver Vendor.

| Property | Value |
| -------- | ----- |
| Pool Name | eg. `Postgres` |
| Resource Type | `java.sql.Driver` |
| Database Driver Vendor | `Postgresql` |
| Driver Classname | `org.postgresql.Driver` |
| `password` | your database password |
| `user` | your database username |
| `URL` | pattern: `jdbc:postgresql://{address}:{port}/{database}` |

You can leave the defaults for any property unlisted here.

Save and test your connection by clicking `Ping` on the edit page of the newly added connection pool.

#### Resource
Go to `Resources` > `JDBC` > `JDBC Resources`. Add a new resource named `jdbc/nucifera` using your newly created connection pool and save it.

| Property | Value |
| -------- | ----- |
| JNDI Name | `jdbc/nucifera` |
| Pool Name | eg. `Postgres` |
