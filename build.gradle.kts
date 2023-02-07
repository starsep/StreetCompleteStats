plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.starsep.streetcompletestats"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.geodesk:geodesk:0.1.4")
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