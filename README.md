# Nucifera-Chat
version: 2018-11-14 11:59

Nucifera Chat is a secure chat server with end-to-end encryption written in Java EE.

#### Before you deploy
##### JDBC
At first you should setup a JDBC resource of name `jdbc/nucifera` or specify your JTA data source in `persistence.xml`.

If you are using Glassfish or Payara, you can also skip this by using sample `glassfish-resources.xml` file provided,
but you might want to avoid using that in production.

##### Encryption
Client-server connection uses additional layer of encryption regardless of any other secure connection configuration and because of that you need to include a RSA key.

Place your `rsa_private.pem` in the classpath (eg. `lib/classes`) and make sure it is a valid **PKCS#8** RSA private key.

See [`doc/Encryption.md`](https://github.com/SudoWaster/Nucifera-Chat/blob/master/doc/Encryption.md) for more info.

### Configuration
#### Logging
Sample config file is located in `NuciferaWeb/resources/log4j2.xml`. Note that this file is also default config and all your logs 
will be saved in a application relative path (for Glassfish it would be `domains/{domain}/config/logs`).

To override this config, place your log4j2.xml in the classpath (eg. `lib/classes` folder).

See [`doc/Logging.md`](https://github.com/SudoWaster/Nucifera-Chat/blob/master/doc/Logging.md) for more info.

#### Default config
Default Nucifera server config is located in `NuciferaWeb/resources/nucifera.xml` file. 

See [`doc/NuciferaConfig.md`](https://github.com/SudoWaster/Nucifera-Chat/blob/master/doc/NuciferaConfig.md) for more info.

# License
It is licensed under GNU General Public License v.3 as of 29 June 2007. See the `LICENSE` file for more.
