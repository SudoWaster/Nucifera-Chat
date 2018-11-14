# Nucifera-Chat
### nucifera.xml config
version: 2018-11-14 11:27

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

#### Table of contents
* [NuciferaConfguration](#NuciferaConfiguration)
  * [Token](#Token)

### NuciferaConfiguration
The main section here is `NuciferaConfiguration`, which holds everything in this config file.

### Token
`Token` is the section that stores any configuration regarding token and identity management.

| Element    | Applicable values | Example |
| ---------- | ----------------- | ------- |
| Expiration | A token expiration in milliseconds measured since last token activity | `1200000` (20 minutes) |
