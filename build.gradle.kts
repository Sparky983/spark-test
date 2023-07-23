plugins {
    java
    jacoco
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "sparky"
            url = if (project.version.toString().endsWith("-SNAPSHOT")) {
                uri("https://repo.sparky983.me/snapshots")
            } else {
                uri("https://repo.sparky983.me/releases")
            }
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
