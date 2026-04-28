/**
 * EXEMPLOS AVANÇADOS - Como Estender CentralAnexo
 * 
 * Este arquivo mostra como:
 * 1. Adicionar nova plataforma
 * 2. Usar Coroutines avançadas
 * 3. Processar múltiplos pacientes
 * 4. Integrar com banco de dados
 */

// ============================================================================
// EXEMPLO 1: ADICIONAR NOVA PLATAFORMA (BioExame)
// ============================================================================

/*
class BioExameSource : ExameSource {
    override val nomePlataforma: String = "BioExame"
    override val urlBase: String = "https://www.bioexame.com.br"
    
    override suspend fun login(usuario: String, senha: String): ResultadoBusca {
        // Implementar login específico de BioExame
        // ...
        return ResultadoBusca(sucesso = true)
    }
    
    override suspend fun buscarExames(pacienteNome: String): ResultadoBusca {
        // Implementar busca específica de BioExame
        // ...
        return ResultadoBusca(sucesso = true, exames = emptyList())
    }
    
    override suspend fun baixarExame(exame: Exame, diretorioDestino: File): File? {
        // Implementar download específico de BioExame
        // ...
        return null
    }
    
    override suspend fun logout(): ResultadoBusca {
        return ResultadoBusca(sucesso = true)
    }
    
    override suspend fun finalizar() {
        // Limpar recursos
    }
}
*/

// Depois, adicione em Main.kt:
// val plataformas = listOf(
//     AolExameSource(),
//     RweExameSource(),
//     LaudoExameSource(),
//     BioExameSource()      // ← Nova plataforma
// )

// ============================================================================
// EXEMPLO 2: COROUTINES - BUSCAR MÚLTIPLOS PACIENTES EM PARALELO
// ============================================================================

import kotlinx.coroutines.*

suspend fun buscarMultiplosPacientes(
    gerenciador: GerenciadorExames,
    pacientes: List<String>,
    credenciais: Map<String, Pair<String, String>>
) {
    val logger = LoggerFactory.getLogger("main")
    
    // Criar coroutines para cada paciente
    val tarefas = pacientes.map { paciente ->
        async {
            try {
                logger.info("Iniciando busca para: $paciente")
                val resultados = gerenciador.buscarExamesEmTodasPlataformas(paciente, credenciais)
                
                var totalExames = 0
                for ((_, resultado) in resultados) {
                    if (resultado.sucesso) {
                        totalExames += resultado.exames.size
                    }
                }
                
                logger.info("✓ Busca finalizada para $paciente: $totalExames exame(s)")
                paciente to totalExames
            } catch (e: Exception) {
                logger.error("✗ Erro ao buscar $paciente", e)
                paciente to 0
            }
        }
    }
    
    // Aguardar todas as tarefas concluírem
    val resultados = tarefas.awaitAll()
    
    // Exibir resumo
    logger.info("\n========== RESUMO FINAL ==========")
    var totalGeral = 0
    for ((paciente, count) in resultados) {
        logger.info("$paciente: $count exame(s)")
        totalGeral += count
    }
    logger.info("Total geral: $totalGeral exame(s)")
}

// Uso:
suspend fun mainComMultiplosPacientes() {
    val plataformas = listOf(
        AolExameSource(),
        RweExameSource(),
        LaudoExameSource()
    )
    
    val gerenciador = GerenciadorExames(
        plataformas = plataformas,
        diretorioSaida = "C:/ExamesCentralizados"
    )
    
    val credenciais = mapOf(
        "AOL" to ("usuario" to "senha"),
        "RWE" to ("usuario" to "senha"),
        "+Laudo" to ("usuario" to "senha")
    )
    
    // Buscar múltiplos pacientes
    val pacientes = listOf("Fulano", "Ciclano", "Beltrano", "Maria")
    buscarMultiplosPacientes(gerenciador, pacientes, credenciais)
}

// ============================================================================
// EXEMPLO 3: TIMEOUT E RETRY COM COROUTINES
// ============================================================================

suspend fun buscarComRetry(
    gerenciador: GerenciadorExames,
    paciente: String,
    credenciais: Map<String, Pair<String, String>>,
    maxRetries: Int = 3,
    delayMs: Long = 1000
): Map<String, ResultadoBusca> {
    val logger = LoggerFactory.getLogger("retry")
    
    var tentativa = 0
    while (tentativa < maxRetries) {
        try {
            logger.info("Tentativa ${tentativa + 1}/$maxRetries para $paciente")
            
            return withTimeoutOrNull(30000) {  // 30 segundos timeout
                gerenciador.buscarExamesEmTodasPlataformas(paciente, credenciais)
            } ?: throw TimeoutException("Busca excedeu 30 segundos")
            
        } catch (e: Exception) {
            tentativa++
            if (tentativa >= maxRetries) {
                logger.error("Falha após $maxRetries tentativas: ${e.message}")
                return emptyMap()
            }
            logger.warn("Erro na tentativa $tentativa, aguardando ${delayMs}ms...")
            delay(delayMs)  // Aguardar antes de tentar novamente
        }
    }
    
    return emptyMap()
}

// ============================================================================
// EXEMPLO 4: SALVAR RESULTADOS EM ARQUIVO JSON
// ============================================================================

import java.nio.file.Files
import java.nio.file.Paths

suspend fun salvarResultadosJSON(
    paciente: String,
    resultados: Map<String, ResultadoBusca>,
    diretorioSaida: String
) {
    // JSON manual (alternativa: adicionar dependency "kotlinx-serialization")
    val json = buildString {
        append("{\n")
        append("  \"paciente\": \"$paciente\",\n")
        append("  \"timestamp\": \"${java.time.LocalDateTime.now()}\",\n")
        append("  \"resultados\": {\n")
        
        resultados.forEach { (plataforma, resultado) ->
            append("    \"$plataforma\": {\n")
            append("      \"sucesso\": ${resultado.sucesso},\n")
            append("      \"exames\": ${resultado.exames.size},\n")
            append("      \"mensagem\": \"${resultado.mensagem}\"\n")
            append("    }")
            
            if (resultados.entries.last() != resultados.entries.find { it.key == plataforma }) {
                append(",")
            }
            append("\n")
        }
        
        append("  }\n")
        append("}")
    }
    
    // Salvar arquivo
    val caminhoArquivo = Paths.get(diretorioSaida, "logs", "${paciente}_resultados.json")
    caminhoArquivo.parent.toFile().mkdirs()
    Files.write(caminhoArquivo, json.toByteArray())
    
    println("✓ Resultados salvos em: $caminhoArquivo")
}

// ============================================================================
// EXEMPLO 5: MONITOR DE PROGRESSO COM PROGRESSBAR
// ============================================================================

suspend fun buscarComProgresso(
    gerenciador: GerenciadorExames,
    pacientes: List<String>,
    credenciais: Map<String, Pair<String, String>>
) {
    val total = pacientes.size
    var processados = 0
    
    for (paciente in pacientes) {
        // Exibir barra de progresso
        val percentual = (processados * 100) / total
        val barra = "█".repeat(percentual / 5) + "░".repeat(20 - (percentual / 5))
        println("[$barra] $percentual% - $paciente")
        
        try {
            gerenciador.buscarExamesEmTodasPlataformas(paciente, credenciais)
        } catch (e: Exception) {
            println("Erro: ${e.message}")
        }
        
        processados++
    }
    
    println("[████████████████████] 100% - Concluído!")
}

// ============================================================================
// EXEMPLO 6: FILTRAR EXAMES POR TIPO E DATA
// ============================================================================

data class FiltroExame(
    val tiposPermitidos: List<String> = emptyList(),  // Ex: ["Radiografia", "Ultrassom"]
    val dataMínima: String? = null,                    // Ex: "2024-01-01"
    val dataMaxima: String? = null                     // Ex: "2024-12-31"
)

fun filtrarExames(exames: List<Exame>, filtro: FiltroExame): List<Exame> {
    return exames.filter { exame ->
        // Filtrar por tipo
        if (filtro.tiposPermitidos.isNotEmpty() && 
            exame.tipoExame !in filtro.tiposPermitidos) {
            return@filter false
        }
        
        // Filtrar por data mínima
        if (filtro.dataMínima != null && exame.data < filtro.dataMínima) {
            return@filter false
        }
        
        // Filtrar por data máxima
        if (filtro.dataMaxima != null && exame.data > filtro.dataMaxima) {
            return@filter false
        }
        
        true
    }
}

// Uso:
suspend fun exemploFiltro() {
    val filtro = FiltroExame(
        tiposPermitidos = listOf("Radiografia", "Tomografia"),
        dataMínima = "2024-01-01",
        dataMaxima = "2024-12-31"
    )
    
    // Assumindo que temos uma lista de exames
    // val examessFiltrados = filtrarExames(exames, filtro)
}

// ============================================================================
// EXEMPLO 7: SISTEMA DE CACHE
// ============================================================================

class CacheExames {
    private val cache = mutableMapOf<String, Pair<List<Exame>, Long>>()
    private val CACHE_DURATION_MS = 3600000  // 1 hora
    
    fun obter(chave: String): List<Exame>? {
        val (exames, timestamp) = cache[chave] ?: return null
        
        // Verificar se ainda está válido
        if (System.currentTimeMillis() - timestamp > CACHE_DURATION_MS) {
            cache.remove(chave)
            return null
        }
        
        return exames
    }
    
    fun armazenar(chave: String, exames: List<Exame>) {
        cache[chave] = exames to System.currentTimeMillis()
    }
    
    fun limpar() {
        cache.clear()
    }
}

// ============================================================================
// EXEMPLO 8: NOTIFICAÇÕES VIA EMAIL (Quando exame é encontrado)
// ============================================================================

// Nota: Adicionar dependency "javax.mail:javax.mail-api:1.6.2"

suspend fun notificarPorEmail(
    destinatario: String,
    paciente: String,
    examesEncontrados: List<Exame>
) {
    val assunto = "✓ CentralAnexo - Exames encontrados para $paciente"
    val corpo = buildString {
        append("<h2>Exames Encontrados</h2>\n")
        append("<p>Paciente: <strong>$paciente</strong></p>\n")
        append("<table border='1' style='border-collapse: collapse'>\n")
        append("  <tr><th>Tipo</th><th>Data</th><th>Plataforma</th></tr>\n")
        
        examesEncontrados.forEach { exame ->
            append("  <tr>")
            append("<td>${exame.tipoExame}</td>")
            append("<td>${exame.data}</td>")
            append("<td>${exame.plataforma}</td>")
            append("</tr>\n")
        }
        
        append("</table>\n")
    }
    
    // TODO: Implementar envio de email
    // Properties props = new Properties();
    // props.put("mail.smtp.host", "smtp.gmail.com");
    // ...
    
    println("📧 Email seria enviado para: $destinatario")
}

// ============================================================================
// EXEMPLO 9: VALIDAÇÃO DE CREDENCIAIS
// ============================================================================

data class CredenciaisValidas(
    val plataforma: String,
    val valido: Boolean,
    val mensagem: String
)

suspend fun validarCredenciais(
    plataforma: ExameSource,
    usuario: String,
    senha: String
): CredenciaisValidas {
    return try {
        val resultado = plataforma.login(usuario, senha)
        
        if (resultado.sucesso) {
            plataforma.logout()
            CredenciaisValidas(
                plataforma = plataforma.nomePlataforma,
                valido = true,
                mensagem = "Credenciais válidas"
            )
        } else {
            CredenciaisValidas(
                plataforma = plataforma.nomePlataforma,
                valido = false,
                mensagem = resultado.mensagem
            )
        }
    } catch (e: Exception) {
        CredenciaisValidas(
            plataforma = plataforma.nomePlataforma,
            valido = false,
            mensagem = "Erro: ${e.message}"
        )
    }
}

// ============================================================================
// EXEMPLO 10: MANTER SESSÃO ABERTA (Para múltiplas buscas)
// ============================================================================

class SessaoExameSource(
    private val exameSource: ExameSource,
    private val usuario: String,
    private val senha: String
) {
    private var logadoComSucesso = false
    
    suspend fun iniciar(): Boolean {
        val resultado = exameSource.login(usuario, senha)
        logadoComSucesso = resultado.sucesso
        return resultado.sucesso
    }
    
    suspend fun buscarMultiplos(pacientes: List<String>): Map<String, ResultadoBusca> {
        if (!logadoComSucesso) {
            throw IllegalStateException("Não está logado")
        }
        
        val resultados = mutableMapOf<String, ResultadoBusca>()
        for (paciente in pacientes) {
            resultados[paciente] = exameSource.buscarExames(paciente)
            delay(1000)  // Pequeno delay entre requisições
        }
        return resultados
    }
    
    suspend fun finalizar() {
        exameSource.logout()
        exameSource.finalizar()
    }
}

// Uso:
suspend fun exemploSessao() {
    val sessao = SessaoExameSource(AolExameSource(), "usuario", "senha")
    
    if (sessao.iniciar()) {
        val resultados = sessao.buscarMultiplos(listOf("Fulano", "Ciclano"))
        println("Resultados: $resultados")
        sessao.finalizar()
    }
}

