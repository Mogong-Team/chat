import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    kotlin("jvm") version "1.8.22"
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("plugin.spring") version "1.8.22"
}

java.sourceCompatibility = JavaVersion.VERSION_17

version = "0.0.1-SNAPSHOT"

allprojects {
    group = "com.mogong"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {

    apply {
        plugin("io.spring.dependency-management")
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter-web")
    }
}

project(":core") {
    dependencies {
        implementation("org.springframework.data:spring-data-mongodb")
    }
    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project(":api") {
    extra["springCloudVersion"] = "2022.0.4"

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    dependencies {
        implementation(project(":core"))
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true