package org.example.service

import org.example.model.Exame
import org.example.model.ResultadoBusca
import org.example.source.ExameSource
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createDirectories

/**
 * Serviço que gerencia o fluxo completo de busca e download de exames.
 * 
 * Responsabilidades:
 * - Coordenar múltiplas plataformas de forma assíncrona
 * - Organizar arquivos no diretório de saída
 * - Registrar logs de operações
 */
class GerenciadorExames(
    private val plataformas: List<ExameSource>,
    private val diretorioSaida: String = "C:/ExamesCentralizados"
) {
    
    companion object {
        private val logger = LoggerFactory.getLogger(GerenciadorExames::class.java)
    }
    
    /**
     * Busca exames de um paciente em todas as plataformas configuradas.
     * 
     * @param pacienteNome Nome do paciente a buscar
     * @param credenciais Mapa com credenciais por plataforma (chave = nomePlataforma, valor = Pair(usuario, senha))
     * @return Mapa com resultados por plataforma
     */
    suspend fun buscarExamesEmTodasPlataformas(
        pacienteNome: String,
        credenciais: Map<String, Pair<String, String>>
    ): Map<String, ResultadoBusca> {
        logger.info("Iniciando busca de exames para paciente: $pacienteNome")
        
        val resultados = mutableMapOf<String, ResultadoBusca>()
        
        // Executar busca em todas as plataformas concorrentemente (usando coroutines)
        for (plataforma in plataformas) {
            try {
                logger.info("Buscando em plataforma: ${plataforma.nomePlataforma}")
                
                val (usuario, senha) = credenciais[plataforma.nomePlataforma]
                    ?: throw IllegalArgumentException("Credenciais não encontradas para ${plataforma.nomePlataforma}")
                
                // Fazer login
                val loginResult = plataforma.login(usuario, senha)
                if (!loginResult.sucesso) {
                    logger.error("Falha ao fazer login em ${plataforma.nomePlataforma}: ${loginResult.mensagem}")
                    resultados[plataforma.nomePlataforma] = loginResult
                    continue
                }
                
                // Buscar exames
                val buscarResult = plataforma.buscarExames(pacienteNome)
                resultados[plataforma.nomePlataforma] = buscarResult
                
                // Fazer logout
                plataforma.logout()
                
            } catch (e: Exception) {
                logger.error("Erro ao buscar em ${plataforma.nomePlataforma}", e)
                resultados[plataforma.nomePlataforma] = ResultadoBusca(
                    sucesso = false,
                    mensagem = "Erro: ${e.message}",
                    erro = e
                )
            }
        }
        
        return resultados
    }
    
    /**
     * Baixa e organiza os exames no diretório de destino.
     * 
     * Estrutura criada:
     * C:/ExamesCentralizados/
     *   ├── Fulano/
     *   │   ├── Radiografia_2024-01-15.pdf
     *   │   ├── Ultrassom_2024-01-16.pdf
     *   │   └── ...
     *   └── Ciclano/
     *       └── ...
     */
    suspend fun organizarExames(
        pacienteNome: String,
        examesParaBaixar: List<Exame>,
        plataforma: ExameSource
    ): List<File> {
        logger.info("Organizando ${examesParaBaixar.size} exame(s) para $pacienteNome")
        
        val arquivosDownload = mutableListOf<File>()
        
        // Criar diretório do paciente
        val diretorioPaciente = Paths.get(diretorioSaida, pacienteNome)
            .createDirectories()
            .toFile()
        
        logger.info("Diretório criado: ${diretorioPaciente.absolutePath}")
        
        // Baixar cada exame
        for (exame in examesParaBaixar) {
            try {
                logger.info("Baixando exame: ${exame.tipoExame} de ${exame.data}")
                
                val arquivoDownload = plataforma.baixarExame(exame, diretorioPaciente)
                if (arquivoDownload != null) {
                    arquivosDownload.add(arquivoDownload)
                    logger.info("Exame salvo em: ${arquivoDownload.absolutePath}")
                } else {
                    logger.warn("Falha ao baixar exame: ${exame.id}")
                }
                
            } catch (e: Exception) {
                logger.error("Erro ao organizar exame ${exame.id}", e)
            }
        }
        
        return arquivosDownload
    }
    
    /**
     * Retorna informações sobre os exames organizados
     */
    fun listarExamesOrganizados(pacienteNome: String): List<File> {
        val diretorioPaciente = Paths.get(diretorioSaida, pacienteNome).toFile()
        
        return if (diretorioPaciente.exists()) {
            diretorioPaciente.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    /**
     * Finaliza todos os recursos das plataformas
     */
    suspend fun finalizarTodos() {
        logger.info("Finalizando todas as plataformas")
        for (plataforma in plataformas) {
            try {
                plataforma.finalizar()
            } catch (e: Exception) {
                logger.error("Erro ao finalizar ${plataforma.nomePlataforma}", e)
            }
        }
    }
}

