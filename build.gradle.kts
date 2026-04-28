plugins {
    kotlin("jvm") version "2.3.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // ========== Playwright para automação web (RPA) ==========
    implementation("com.microsoft.playwright:playwright:1.48.2")
    
    // ========== Kotlin Coroutines para operações assíncronas ==========
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    
    // ========== Logging para melhor rastreamento de operações ==========
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    
    // ========== Testes ==========
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

// ========== Configuração de aplicação ==========
application {
    mainClass.set("org.example.MainKt")
}
