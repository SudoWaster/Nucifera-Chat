# Nucifera-Chat
### Deploying the app
version: 2018-12-04 13:01

#### Table of contents
* [Prerequisites](#prerequisites)
* [Deploying your app](#deploying-your-app)
	* [Glassfish 5](#glassfish-5)

### Prerequisites
Nucifera-Chat will need you to configure datasource, encryption and optionally general application options and logging.

Please refer to the following before you deploy

| Description | Link |
| --- | --- |
| **Datasource configuration** (required) | [`JDBCDatasource.md`](./JDBCDatasource.md) |
| **Encryption** (required/optional) | [`Encryption.md`](./Encryption.md) |
| **nucifera.xml config** | [`NuciferaConfig.md`](./NuciferaConfig.md) |
| **Loggers** | [`Logging.md`](./Logging.md) |

You can opt out of additional encryption by [setting AdditionalEncryption to false](./Encryption.md#security).

### Deploying your app
#### Glassfish 5
If you have configured your server successfully, you can now deploy your app. Login to the Glassfish admin console (default: `http://localhost:4848`) and go to the `Applications` section.

Click on `Deploy...` and select Nucifera war file from releases or your build. You can leave the defaults **but make sure to uncheck `Implicit CDI` option**.

| Property | Value |
| --- | --- |
| Type | `Web Application` |
| Context Root | eg. `/Nucifera`, `/chat` or nothing |
| ApplicationName | eg. `NuciferaChat` or `NuciferaWeb`
| Implicit CDI | &#9633; *(unchecked)* |

*Voila!* Application should deploy successfully. If not, reconfigure Glassfish carefully following documentation from [Prerequisites](#prerequisites).