# Nucifera-Chat
### Server event logging 
version: 2018-11-14 12:45

#### Table of contents
* [Configuration file](#configuration-file)
* [Communication log](#communication-log)
* [Application log](#application-log)
* [Security log](#security-log)

### Configuration file
Nucifera-Chat uses standard Log4j 2 for logging. Please refer to [Log4j 2](https://logging.apache.org/log4j/2.x/manual/configuration.html) for configuration reference.
This documentation provides information about logger purposes and according names that let you properly set up Log4j 2. Also note that there is [a default Log4j 2 configuration file](https://github.com/SudoWaster/Nucifera-Chat/blob/master/NuciferaWeb/src/main/resources/log4j2.xml) and you can use this as a reference.

You should place your configuration file in your classpath to override defaults. In Glassfish the correct location is `lib/classes` (it might be referred as shared libraries location in other JEE servers).

### Communication log
Logger name: `communication`

Nucifera-Chat logs all of the communication that has been successfully received (and decrypted) and that has been sent.
Communication log proves to be very useful with debugging client (and server-side) problems.

### Application log
Logger name: `application`

The purpose of this logger is to provide useful application events information. Any debugging information should be logged here, all exceptions thrown by Jersey server will also go through ExceptionMapper and will be logged here.

### Security log
Logger name: `security`

This logger will log any security-related event, eg. when encryption or decryption fails or private key cannot be loaded. Most of the exceptions will also be logged to the application log, but this log might give you more direct cause of security problems (example: no private key will be logged in the application log as NullPointerException in various places, but the exact trace of the key loading will be in the security log).