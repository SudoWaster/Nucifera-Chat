# Nucifera-Chat
### nucifera.xml config
version: 2018-11-15 16:35

#### Table of contents
* [Example file](#example-file)
* [NuciferaConfguration](#NuciferaConfiguration)
	* [Debug](#debug)
  * [Security](#security)

### Example file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<NuciferaConfiguration>
	<Debug>
		<Verbose>false</Verbose>
		<FormatLog>true</FormatLog>
	</Debug>

	<Security>
		<!-- Token inactivity expiration in ms -->
		<TokenExpiration>1200000</TokenExpiration>
		<ParseBase64>false</ParseBase64>
	</Security>
</NuciferaConfiguration>
```

### NuciferaConfiguration
The main section here is `NuciferaConfiguration`, which holds everything in this config file.

### Debug
`Debug` is an additional debug settings section that may help during developing, testing or debugging process.

| Element | Explanation | Example | Default |
| --- | --- | --- | --- |
| Verbose | Changes visibility of more detailed debug info in responses | `true` or `false` | `false` |
| FormatLog | Pretty print outgoing communication (may take more disk space) | `true` or `false` | `true` |


### Security
`Security` is the section that stores any configuration regarding token, identity management and encryption.

| Element    | Explanation | Example | Default |
| ---------- | ----------------- | ------- | --- |
| TokenExpiration | A token expiration in milliseconds measured since last security activity | `600000` [ms] (10 minutes) | `1200000` |
| ParseBase64 | Whether the communication should be decrypted from and encrypted into Base64 form (or just raw bytes) | `true` or `false` | `false` |