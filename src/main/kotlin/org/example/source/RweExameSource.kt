package org.example.source

import org.example.model.Exame
import org.example.model.ResultadoBusca
import java.io.File
import org.slf4j.LoggerFactory
import io.github.cdimascio.dotenv.dotenv
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright

/**
 * Implementação de ExameSource para a plataforma RWE (RadiologyWeb).
 * 
 * Similar ao AOL, mas com sua própria lógica de automação específica.
 */
class RweExameSource : ExameSource {
    
    companion object {
        private val logger = LoggerFactory.getLogger(RweExameSource::class.java)
    }
    private var playwright: Playwright? = null
    private var browser: Browser? = null
    private var page: Page? = null
    override val nomePlataforma: String = "RWE"
    override val urlBase: String = dotenv()["RWE_URL"] ?: ""


    override suspend fun login(usuario: String, senha: String): ResultadoBusca {
        return try {
            logger.info("[RWE] Iniciando login para usuário: $usuario")
            // TODO: Implementar login específico do RWE
            playwright = Playwright.create()
            browser = playwright!!.chromium().launch(
                BrowserType.LaunchOptions()
                    .setHeadless(false) // true para rodar sem abrir janela
                    .setSlowMo(100.0)   // atraso em ms entre ações — ajuda a ver o que acontece
            )
            page = browser!!.newPage()
            page!!.navigate(urlBase)
            page!!.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

            page!!.fill("input[name='email']", dotenv ()["RWE_USER"] ?: "")
            page!!.fill("input[type='password']", dotenv ()["RWE_PASSWORD"] ?: "")
            page!!.click("button.buttonEnter")
            page!!.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

            logger.info("[RWE] Login realizado com sucesso")
            ResultadoBusca(
                sucesso = true,
                mensagem = "Login RWE realizado com sucesso"
            )
        } catch (e: Exception) {
            logger.error("[RWE] Erro ao fazer login", e)
            ResultadoBusca(
                sucesso = false,
                mensagem = "Erro ao fazer login no RWE",
                erro = e
            )
        }
    }
    
    override suspend fun buscarExames(pacienteNome: String): ResultadoBusca {
        return try {
            logger.info("[RWE] Buscando exames para paciente: $pacienteNome")
            // TODO: Implementar busca específica do RWE
            ResultadoBusca(
                sucesso = true,
                exames = emptyList(),
                mensagem = "Busca realizada com sucesso"
            )
        } catch (e: Exception) {
            logger.error("[RWE] Erro ao buscar exames", e)
            ResultadoBusca(
                sucesso = false,
                mensagem = "Erro ao buscar exames no RWE",
                erro = e
            )
        }
    }
    
    override suspend fun baixarExame(exame: Exame, diretorioDestino: File): File? {
        return try {
            logger.info("[RWE] Baixando exame: ${exame.id}")
            // TODO: Implementar download específico do RWE
            null
        } catch (e: Exception) {
            logger.error("[RWE] Erro ao baixar exame", e)
            null
        }
    }
    
    override suspend fun logout(): ResultadoBusca {
        return try {
            logger.info("[RWE] Realizando logout")
            ResultadoBusca(
                sucesso = true,
                mensagem = "Logout realizado com sucesso"
            )
        } catch (e: Exception) {
            logger.error("[RWE] Erro ao fazer logout", e)
            ResultadoBusca(
                sucesso = false,
                mensagem = "Erro ao fazer logout",
                erro = e
            )
        }
    }
    
    override suspend fun finalizar() {
        try {
            logger.info("[RWE] Finalizando recursos")
        } catch (e: Exception) {
            logger.error("[RWE] Erro ao finalizar", e)
        }
    }
}

