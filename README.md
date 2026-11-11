# Generated Java data model classes for nNGM eCRF System

This contains Java code generation for the nNGM eCRF System.

## Usage

Local package build

```bash
./gradlew clean build
```

### Maven Packages

Pre-build packages for JDK 17+

#### Maven

```xml

<repositories>
  <repository>
    <url>https://git.dnpm.dev/api/packages/public-snapshots/maven</url>
  </repository>
</repositories>

...

<dependency>
  <groupId>dev.pcvolkmer.nngm</groupId>
  <artifactId>nngm-model</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>

```

#### Gradle

```gradle
repositories {
    maven {
        url = uri("https://git.dnpm.dev/api/packages/public-snapshots/maven")
    }
    mavenCentral()
}

...

implementation 'dev.pcvolkmer.nngm:nngm-model:0.1.0-SNAPSHOT'
```

