# Nucifera-Chat
### nucifera.xml config
version: 2018-11-15 15:58

#### Table of contents
* [Example file](#example-file)
* [NuciferaConfguration](#NuciferaConfiguration)
	* [Debug](#debug)
  * [Token](#Token)

### Example file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<NuciferaConfiguration>
	<Debug>
		<Verbose>false</Verbose>
		<FormatLog>true</FormatLog>
	</Debug>

    <Token>
        <!-- Token inactivity expiration in ms -->
        <Expiration>1200000</Expiration>
    </Token>
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


### Token
`Token` is the section that stores any configuration regarding token and identity management.

| Element    | Explanation | Example | Default |
| ---------- | ----------------- | ------- | --- |
| Expiration | A token expiration in milliseconds measured since last token activity | `600000` [ms] (10 minutes) | `1200000` |
