import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.pcvolkmer.nngm"
version = "0.1.0-SNAPSHOT"

val xsd2java by configurations.creating

val jaxbTargetDir = file("src/generated/java")

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                    "src/main/java",
                    jaxbTargetDir
            )
        }
    }
}

dependencies {
    // JAXB code generation
    xsd2java("com.sun.xml.bind:jaxb-xjc:4.0.9")
    xsd2java("com.sun.xml.bind:jaxb-impl:4.0.9")

    api(platform("tools.jackson:jackson-bom:3.1.7"))
    api("tools.jackson.dataformat:jackson-dataformat-xml")
    api("tools.jackson.module:jackson-module-jakarta-xmlbind-annotations")

    api("jakarta.xml.bind:jakarta.xml.bind-api:4.0.5")

    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register("xsd2java") {
    doLast {
        jaxbTargetDir.mkdirs()

        ant.withGroovyBuilder {
            "taskdef"(
                    "name" to "xjc",
                    "classname" to "com.sun.tools.xjc.XJCTask",
                    "classpath" to xsd2java.asPath
            )

            "xjc"(
                    "destdir" to jaxbTargetDir.absolutePath,
                    "package" to "dev.pcvolkmer.nngm.model.v1",
                    "schema" to "https://nngm-demo.medicalsyn.com/api/v1/xml/schema?useNamedTypes=true",
                    "binding" to "src/main/resources/xsd/bindings.xjb"
            )
        }
    }
}

tasks.named("compileJava") {
    dependsOn("xsd2java")
}

tasks.test {
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
    useJUnitPlatform()
}

publishing {
    repositories {
        mavenLocal()
        maven {
            name = "GitDnpmDev"

            val releasesRepoUrl = uri("https://git.dnpm.dev/api/packages/public/maven")
            val snapshotsRepoUrl = uri("https://git.dnpm.dev/api/packages/public-snapshots/maven")
            url = if (version.toString().endsWith("SNAPSHOT"))
                snapshotsRepoUrl
            else
                releasesRepoUrl

            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "token ${properties["dnpm_dev_token"] ?: ""}"
            }

            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
