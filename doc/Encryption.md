# Nucifera-Chat
### Server communication encryption 
version: 2018-11-14 21:37

#### Table of contents
* [Description](#Description)
* [Server-side setup](#server-side-setup)
* [Client-side setup](#client-side-setup)
  * [Algorithms](#Algorithms)
  * [Secure connection](#secure-connection)

### Description
Nucifera-Chat uses additional layer of encryption to ensure that the client-server communication process is secure. 
You can use SSL/TLS within your JEE server instance (and this is very much recommended), but just to prevent i.a. password leaks (by a MITM attack for example).

The initial communication should always start with `HELLO_INIT` from the client if the client has not aquired a token yet. Client should send a challenge of length 16, 24 or 32 characters encrypted with server's RSA public key.

**Only server** should store its private key (inaccessible to the public). It is used to decrypt challenge and sign it. Client then verifies the signature to ensure that the server that it connected to is the correct and trusted one.

The client confirms that it trusts the server by issuing `HELLO_CLIENT_DONE` and the communication from now on is encrypted with AES using the challenge sent by a client.

NOTE: As you can see, it's the client responsibility to sent a unique challenge and correctly connect to a trusted server. If any of these processes is handled incorrectly by the client, it may connect to a suspicious (and incorrect) server.

### Server-side setup
Prepare a set of **PKCS#8** in a pem file. These files should have the following format:

`rsa_private.pem`:
```
-----BEGIN PRIVATE KEY-----
(key bytes)
-----END PRIVATE KEY-----
```

As you can see, key files should be named `rsa_private.pem`.

Place this file in your application classpath. For Glassfish it would be `lib/classes` (it may be also called shared libraries folder or something like this).

From now on, the server should give no error and the connection can be established successfully.

### Client-side setup
#### Algorithms
For RSA, Nucifera-Chat uses standard Java `RSA` algorithm from `javax.crypto.Cipher`.
```java
javax.crypto.Cipher.getInstance("RSA");
```

For AES, it uses `AES/ECB/PKCS5Padding` and creates key with just `AES` using `new SecretKeySpec`
```java
new javax.crypto.spec.SecretKeySpec(challenge, "AES");
javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding");
```

Be sure to use the same implementations.

#### Secure connection
Start your connection by a POST request to `/auth` with the following data
```json
{
	"state": "HELLO_INIT",
	"challenge": "{RSA encrypted challenge}"
}
```
Your challenge should be a unique String. Allowed lengths are 16, 24 and 32 - AES does not support other sizes.

Server should respond with `HELLO`, signed challenge and a token
```json
{
	"state": "HELLO",
	"token": "{your security}",
	"challenge": "{your challenge signed}"
}
```

**VERY IMPORTANT NOTE:** From now on attach this token to your request header as `X-Nucifera-Token`. 

To verify server's identity, respond with `HELLO_CLIENT_DONE` and then switch to AES encryption. If everything goes right, you should get encrypted `AUTH_VALID` response, if it doesn't, you will get `FAIL` or an error code with a message.

Example error code for not attaching challenge:
```json
{
	"error": "400"
}
```

Or with a challenge of an incorrect length:
```json
{
	"error": "400",
	"message": "Wrong key size"
}
```