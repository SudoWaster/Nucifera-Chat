# Nucifera-Chat
### nucifera.xml config
version: 2018-11-14 12:27


#### Table of contents
* [Example file](#example-file)
* [NuciferaConfguration](#NuciferaConfiguration)
  * [Token](#Token)

### Example file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<NuciferaConfiguration>
    <Token>
        <!-- Token inactivity expiration in ms -->
        <Expiration>1200000</Expiration>
    </Token>
</NuciferaConfiguration>
```

### NuciferaConfiguration
The main section here is `NuciferaConfiguration`, which holds everything in this config file.

### Token
`Token` is the section that stores any configuration regarding token and identity management.

| Element    | Applicable values | Example |
| ---------- | ----------------- | ------- |
| Expiration | A token expiration in milliseconds measured since last token activity | `1200000` (20 minutes) |
