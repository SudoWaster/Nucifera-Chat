# Nucifera-Chat
version: 2018-11-14 16:05

Nucifera Chat is a secure chat server with end-to-end encryption written in Java EE.

## Thorough documentation
See [`doc/DeployingNucifera.md`](./doc/DeployingNucifera.md) and follow the steps from there.

## Quick start
### Building
This source is a standard Maven 2 project, to build it you have to run
`mvn clean install`

Maven will automatically resolve dependencies and pack a war file for you.

### Before you deploy
#### JDBC
At first you should setup a JDBC resource named `jdbc/nucifera`.

See [`doc/JDBCDatasource.md`](./doc/JDBCDatasource.md) for more info.

#### Encryption
Client-server connection uses additional layer of encryption regardless of any other secure connection configuration and because of that you need to include a RSA key.

Place your `rsa_private.pem` in the classpath (eg. `lib/classes`) and make sure it is a valid **PKCS#8** RSA private key.

See [`doc/Encryption.md`](./doc/Encryption.md) for more info.

### Configuration
#### Logging
Sample config file is located in `NuciferaWeb/resources/log4j2.xml`. Note that this file is also default config and all your logs 
will be saved in a application relative path (for Glassfish it would be `domains/{domain}/config/logs`).

To override this config, place your log4j2.xml in the classpath (eg. `lib/classes` folder).

See [`doc/Logging.md`](./doc/Logging.md) for more info.

#### Default config
Default Nucifera server config is located in `NuciferaWeb/resources/nucifera.xml` file. 

See [`doc/NuciferaConfig.md`](./doc/NuciferaConfig.md) for more info.

### Deploying the app
On Glassfish: make sure to disable `Implicit CDI`. 

For more info, see [`doc/DeployingNucifera.md`](./doc/DeployingNucifera.md).

# License
It is licensed under GNU General Public License v.3 as of 29 June 2007. See the `LICENSE` file for more.
