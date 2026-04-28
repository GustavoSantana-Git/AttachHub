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

    // ========== Variáveis de Ambiente (.env) ==========
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // ========== Playwright para automação web (RPA) ==========
    implementation("com.microsoft.playwright:playwright:1.44.0")

    // ========== Kotlin Coroutines para operações assíncronas ==========
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    
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
