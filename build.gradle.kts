
plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val dropwizardVersion = "4.0.7"
val jacksonVersion = "2.17.1"

dependencies {
    // Dropwizard core
    implementation("io.dropwizard:dropwizard-core:$dropwizardVersion")

    //  Jackson Kotlin module
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    //  Jackson module for Java 8 time (LocalDateTime, Duration)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // Kotlin standard library
    implementation(kotlin("stdlib"))

    // Testing
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.example.AttendanceApplicationKt")
}

kotlin {
    jvmToolchain(21) // JDK 21
}

tasks.named<JavaExec>("run") {
    args = listOf("server", "src/main/resources/config.yml")
}

