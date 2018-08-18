# Nucifera-Chat
Nucifera Chat is a secure chat server with end-to-end encryption written in Java EE.

#### Before you deploy
At first you should setup a JDBC resource of name `jdbc/nucifera` or specify your JTA data source in `persistence.xml`.

If you are using Glassfish or Payara, you can also skip this by using sample `glassfish-resources.xml` file provided,
but please not that **this is not recommended** if you wish to share your Nucifera derivative, as you are compromising 
your database very easily.

# Lincense
It is licensed under GNU General Public License v.3 as of 29 June 2007. See the `LICENSE` file for more.