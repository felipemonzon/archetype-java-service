# archetype-service
Archetype java service

# Version
![version](https://img.shields.io/badge/version-1.0.0-blue.svg)

Para más detalle mira el archivo [CHANGELOG](CHANGELOG)

# Quality Gate


### Pre-requisitos 📋
Tener instalado
* IntelliJ
* Gradle
* Java
* MySQL

Para desencriptar los datos del archivo properties es necesario
agregar este parámetro al iniciar el proyecto

```
./gradlew encryptProperties --password=felipemonzon
```

Para encriptar los datos sensibles es necesario compilar con la siguiente instrucción:
```
./gradlew decryptProperties --password=felipemonzon
```

### Instalación 🔧

Proyecto comprobar si el proyecto esta en orden con gradle

```
./gradlew clean check
```

## Ejecutando las pruebas ⚙

Para ejecutar las pruebas y comprobar la calidad del código en sonar

```
./gradlew clean test --info
```

### Y las pruebas unitarias de codificación ⌨️

Las pruebas se realizaron con mockito y junit

```
  
```

## Despliegue 📦

Utilizar tu container favorito

## Construido con 🛠️

* Spring Boot 4.0.6
* Java 26
* Gradle
* IntelliJ IDEA

### Formateador de sintaxis de código 📋
Para formatear el código se realiza de la siguiente manera

```
./gradlew spotlessApply
```
Para validar el formato del código se realiza del siguiente manera

```
./gradlew spotlessCheck
```

## Versionado 📌

Usamos [GitHub](https://github.com/felipemonzon/archetype-java-service) para el versionado.

## Autores ✒️

* **[Felipe Monzón](https://felipemonzon.github.io/)** - *WEB AND JAVA DEVELOPER*

## Contribuyendo 🖇


## Licencia 📄

Este proyecto está bajo la Licencia GNU General Public License v3.0 - mira el archivo [LICENSE.md](LICENSE) para detalles
