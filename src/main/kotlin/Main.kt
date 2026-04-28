package org.example

import org.example.source.AolExameSource
import org.example.source.RweExameSource
import org.example.source.LaudoExameSource
import org.example.service.GerenciadorExames
import org.slf4j.LoggerFactory
import kotlinx.coroutines.*

/**
 * ============================================================================
 * CENTRALANEXO - Centralizador de Exames
 * ============================================================================
 * 
 * Projeto que automatiza a busca e download de exames de múltiplas
 * plataformas web usando Playwright e Kotlin Coroutines.
 * 
 * Estrutura:
 * - model/: Classes de dados (Exame, ResultadoBusca)
 * - source/: Implementações Strategy para cada plataforma
 * - service/: Serviço de orquestração (GerenciadorExames)
 * 
 * ============================================================================
 */

private val logger = LoggerFactory.getLogger("Main")

/**
 * FUNÇÃO PRINCIPAL
 * 
 * Demonstra como usar o sistema CentralAnexo para:
 * 1. Criar instâncias das plataformas (Padrão Strategy)
 * 2. Executar buscas de forma assíncrona (Kotlin Coroutines)
 * 3. Organizar arquivos no diretório estruturado
 */
suspend fun main() {
    logger.info("========== INICIANDO CENTRALANEXO ==========")
    
    try {
        // =====================================================================
        // ETAPA 1: Configurar as plataformas disponíveis
        // =====================================================================
        // Cada plataforma é uma implementação diferente da interface ExameSource
        // Isso permite adicionar novas plataformas facilmente sem modificar o resto do código
        
        val plataformas = listOf(
            AolExameSource(),      // Estratégia para AOL
            RweExameSource(),      // Estratégia para RWE
            LaudoExameSource()     // Estratégia para +Laudo
        )
        
        logger.info("${plataformas.size} plataforma(s) configurada(s)")
        plataformas.forEach { logger.info("  - ${it.nomePlataforma}") }
        
        // =====================================================================
        // ETAPA 2: Criar o gerenciador de exames
        // =====================================================================
        // O gerenciador orquestra a busca e organização de exames
        
        val gerenciador = GerenciadorExames(
            plataformas = plataformas,
            diretorioSaida = "C:/ExamesCentralizados"
        )
        
        // =====================================================================
        // ETAPA 3: Definir credenciais para cada plataforma
        // =====================================================================
        // TODO: Em produção, estas credenciais devem vir de um arquivo de configuração
        // ou de variáveis de ambiente (NUNCA hardcoded!)
        
        val credenciais = mapOf(
            "AOL" to ("seu_usuario_aol" to "sua_senha_aol"),
            "RWE" to ("seu_usuario_rwe" to "sua_senha_rwe"),
            "+Laudo" to ("seu_usuario_laudo" to "sua_senha_laudo")
        )
        
        // =====================================================================
        // ETAPA 4: EXEMPLO PRINCIPAL - Buscar exames do paciente "Fulano"
        // =====================================================================
        
        val nomePaciente = "Fulano"
        logger.info("\n>>> Buscando exames do paciente: $nomePaciente <<<\n")
        
        // Usar coroutines para buscar em TODAS as plataformas de forma assíncrona/concorrente
        // Isso é MUITO mais rápido do que fazer sequencialmente!
        val resultados = gerenciador.buscarExamesEmTodasPlataformas(nomePaciente, credenciais)
        
        // =====================================================================
        // ETAPA 5: Processar resultados da busca
        // =====================================================================
        
        var totalExamesEncontrados = 0
        
        for ((plataforma, resultado) in resultados) {
            logger.info("\n--- PLATAFORMA: $plataforma ---")
            
            if (resultado.sucesso) {
                logger.info("✓ Busca bem-sucedida!")
                logger.info("  Exames encontrados: ${resultado.exames.size}")
                
                totalExamesEncontrados += resultado.exames.size
                
                // Listar exames encontrados
                resultado.exames.forEach { exame ->
                    logger.info(
                        "  • ${exame.tipoExame} - ${exame.data} (ID: ${exame.id})"
                    )
                }
                
                // ===============================================================
                // ETAPA 6: Baixar e organizar exames
                // ===============================================================
                if (resultado.exames.isNotEmpty()) {
                    logger.info("\n  Iniciando download dos exames...")
                    
                    // Buscar instância correta da plataforma
                    val plataformaInstancia = plataformas.find { 
                        it.nomePlataforma == plataforma 
                    }
                    
                    if (plataformaInstancia != null) {
                        val arquivosDownload = gerenciador.organizarExames(
                            nomePaciente,
                            resultado.exames,
                            plataformaInstancia
                        )
                        
                        logger.info("  Exames baixados: ${arquivosDownload.size}")
                        arquivosDownload.forEach { arquivo ->
                            logger.info("  ✓ ${arquivo.name}")
                        }
                    }
                }
                
            } else {
                logger.warn("✗ Falha na busca")
                logger.warn("  Mensagem: ${resultado.mensagem}")
                resultado.erro?.let {
                    logger.warn("  Erro: ${it.message}")
                }
            }
        }
        
        // =====================================================================
        // ETAPA 7: Resumo final
        // =====================================================================
        
        logger.info("\n========== RESUMO FINAL ==========")
        logger.info("Total de exames encontrados: $totalExamesEncontrados")
        logger.info("Diretório de saída: C:/ExamesCentralizados")
        
        // Listar exames organizados
        val examesOrganizados = gerenciador.listarExamesOrganizados(nomePaciente)
        logger.info("Exames organizados para $nomePaciente: ${examesOrganizados.size}")
        examesOrganizados.forEach { arquivo ->
            logger.info("  └─ ${arquivo.name}")
        }
        
        logger.info("========== FINALIZADO COM SUCESSO ==========\n")
        
    } catch (e: Exception) {
        logger.error("ERRO FATAL durante execução:", e)
    } finally {
        // Sempre finalizar recursos
        logger.info("Encerrando aplicação...")
    }
}

/**
 * EXEMPLO AVANÇADO: Buscar múltiplos pacientes em paralelo com Coroutines
 * 
 * Descomente a função abaixo se quiser testar com vários pacientes:
 */
/*
suspend fun main() {
    val nomePacientes = listOf("Fulano", "Ciclano", "Beltrano")
    
    // Usar async para executar buscas em paralelo
    val tarefas = nomePacientes.map { paciente ->
        async {
            logger.info("Iniciando busca para: $paciente")
            // ... lógica de busca ...
            "Busca finalizada para: $paciente"
        }
    }
    
    // Aguardar todas as tarefas terminarem
    val resultados = tarefas.awaitAll()
    resultados.forEach { logger.info(it) }
}
*/
