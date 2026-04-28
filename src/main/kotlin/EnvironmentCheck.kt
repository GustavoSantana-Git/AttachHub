package org.example

import kotlin.system.exitProcess

/**
 * Script de verificação do ambiente CentralAnexo
 * 
 * Valida:
 * - Java version
 * - Gradle version
 * - Estrutura de diretórios
 * - Dependências
 */

fun main() {
    println("""
        ╔════════════════════════════════════════════════════════╗
        ║           CENTRALANEXO - Environment Check              ║
        ╚════════════════════════════════════════════════════════╝
    """.trimIndent())
    
    var todasAsVerificacoesPassaram = true
    
    // 1. Verificar Java
    println("\n✓ Verificando Java...")
    val javaVersion = System.getProperty("java.version")
    val javaHome = System.getProperty("java.home")
    println("  - Java Version: $javaVersion")
    println("  - Java Home: $javaHome")
    
    if (!javaVersion.startsWith("21")) {
        println("  ✗ ERRO: Java 21 é necessário! Você tem $javaVersion")
        todasAsVerificacoesPassaram = false
    } else {
        println("  ✓ Java 21 OK")
    }
    
    // 2. Verificar Gradle
    println("\n✓ Verificando estrutura do projeto...")
    val srcDir = java.io.File("src/main/kotlin/org/example")
    val buildGradle = java.io.File("build.gradle.kts")
    
    if (srcDir.exists()) {
        println("  ✓ Diretório src/main/kotlin/org/example encontrado")
    } else {
        println("  ✗ Diretório src/main/kotlin/org/example NÃO encontrado")
        todasAsVerificacoesPassaram = false
    }
    
    if (buildGradle.exists()) {
        println("  ✓ build.gradle.kts encontrado")
    } else {
        println("  ✗ build.gradle.kts NÃO encontrado")
        todasAsVerificacoesPassaram = false
    }
    
    // 3. Verificar classes
    println("\n✓ Verificando classes...")
    val classes = listOf(
        "src/main/kotlin/org/example/Main.kt" to "Main.kt",
        "src/main/kotlin/org/example/model/Exame.kt" to "Exame.kt",
        "src/main/kotlin/org/example/source/ExameSource.kt" to "ExameSource.kt",
        "src/main/kotlin/org/example/source/AolExameSource.kt" to "AolExameSource.kt",
        "src/main/kotlin/org/example/service/GerenciadorExames.kt" to "GerenciadorExames.kt"
    )
    
    for ((path, nome) in classes) {
        if (java.io.File(path).exists()) {
            println("  ✓ $nome")
        } else {
            println("  ✗ $nome NÃO encontrado")
            todasAsVerificacoesPassaram = false
        }
    }
    
    // 4. Verificar documentação
    println("\n✓ Verificando documentação...")
    val docs = listOf(
        "README.md",
        "QUICKSTART.md",
        "GUIA_PLAYWRIGHT.kt",
        "SETUP.md"
    )
    
    for (doc in docs) {
        if (java.io.File(doc).exists()) {
            println("  ✓ $doc")
        } else {
            println("  ✗ $doc NÃO encontrado")
            todasAsVerificacoesPassaram = false
        }
    }
    
    // Resultado final
    println("\n" + "═".repeat(60))
    if (todasAsVerificacoesPassaram) {
        println("✅ AMBIENTE OK - Pronto para começar!")
        println("\nPróximos passos:")
        println("  1. Leia: QUICKSTART.md")
        println("  2. Execute: ./gradlew run")
        println("  3. Implemente: AolExameSource.kt")
        exitProcess(0)
    } else {
        println("❌ AMBIENTE COM PROBLEMAS - Verifique os erros acima")
        println("\nConsulte: SETUP.md para instruções")
        exitProcess(1)
    }
}

