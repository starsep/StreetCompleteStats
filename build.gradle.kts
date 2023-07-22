plugins {
    kotlin("jvm") version "1.8.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.starsep.streetcompletestats"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.geodesk:geodesk:0.1.7")
    implementation("com.soywiz.korlibs.korte:korte:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}