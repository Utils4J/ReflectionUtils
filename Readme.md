![[Java CI]](https://github.com/Utils4J/ReflectionUtils/actions/workflows/check.yml/badge.svg)
![[Latest Version]](https://maven.mineking.dev/api/badge/latest/releases/de/cyklon/ReflectionUtils?prefix=v&name=Latest%20Version&color=0374b5)

# Installation

ReflectionUtils is hosted on a custom repository at [https://maven.mineking.dev](https://maven.mineking.dev/#/releases/de/cyklon/ReflectionUtils). Replace VERSION with the lastest version (without the `v` prefix).
Alternatively, you can download the artifacts from jitpack (not recommended).

### Gradle

```groovy
repositories {
  maven { url "https://maven.mineking.dev/releases" }
}

dependencies {
  implementation "de.cyklon:ReflectionUtils:VERSION"
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>Utils4J</id>
    <url>https://maven.mineking.dev/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.cyklon</groupId>
    <artifactId>ReflectionUtils</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```