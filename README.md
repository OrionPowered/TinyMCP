# TinyMCP
Utility library/application for generating TinyV2 mappings from MCP and mapping jars back and forth between MCP & Notch
(obfuscated)

Currently supports Beta 1.1_02 but will eventually support all major Minecraft Beta versions.

## NOTICE
You must NOT distribute any sources generated using this tool. MCP's license prohibits distribution of their mappings
as well as Mojang prohibits the distribution of Minecraft.

## Usage
### Generating mappings
```shell
java -jar tinymcp.jar -create -type <server/client> -version b1.1_02 -o server.tinyv2
```
#### Options
| Option           | Required | Description                                       |
|------------------|----------|---------------------------------------------------|
| `-t`, `-type`    | true     | Sets the type of mappings (client/server)         |
| `-v`, `-version` | true     | Sets the version used for generating mappings     |
| `-o`, `-output`  | false    | Sets the output file for the TinyV2 mappings file |

### Applying Mappings (bidirectional)
```shell
java -jar tinymcp.jar -apply -mappings server.tinyv2 -type MCP -in server.jar -out server-mapped.jar
```
#### Options
| Option        | Required | Description                                   |
|---------------|----------|-----------------------------------------------|
| `-i`, `-in`   | true     | Sets the input JAR file                       |
| `-o`, `-out`  | false    | Sets the version used for generating mappings |
| `-t`, `-type` | true     | Sets the type of mapping - MCP or NOTCH       |

