/**
 * GUIA PRÁTICO - Como Implementar Webscraping com Playwright
 * 
 * Este arquivo contém exemplos práticos de como usar Playwright
 * para implementar login, busca e download de exames.
 * 
 * COPIE E ADAPTE ESTES EXEMPLOS NAS CLASSES *ExameSource.kt
 */

// ============================================================================
// EXEMPLO 1: INICIALIZAR PLAYWRIGHT
// ============================================================================

suspend fun exemploInicializarPlaywright() {
    // Criar instância do Playwright
    val playwright = Playwright.create()
    
    // Opção A: Abrir navegador em modo headless (sem interface gráfica)
    val browser = playwright.chromium().launch(
        BrowserType.LaunchOptions()
            .setHeadless(true)  // Não mostrar janela do navegador
            .setTimeout(30000.0) // Timeout de 30 segundos
    )
    
    // Opção B: Abrir navegador com interface visível (útil para debug)
    val browserComUI = playwright.chromium().launch(
        BrowserType.LaunchOptions()
            .setHeadless(false) // Mostrar navegador
    )
    
    // Criar nova página
    val page = browser.newPage()
    
    // Configurar viewport (tamanho da tela)
    page.setViewportSize(1920, 1080)
}

// ============================================================================
// EXEMPLO 2: FAZER LOGIN (Formulário com email e senha)
// ============================================================================

suspend fun exemploLogin() {
    val page = browser!!.newPage()
    
    // Navegar para página de login
    page.navigate("https://exemplo.com/login")
    
    // Aguardar carregamento da página
    page.waitForLoadState()
    
    // Preencher campo de email
    page.fill("input#email", "usuario@exemplo.com")
    
    // Preencher campo de senha
    page.fill("input#senha", "minhaSenha123")
    
    // Clique no botão de login
    page.click("button:has-text('Entrar')")
    
    // Aguardar redirecionamento para página após login
    page.waitForNavigation(Page.WaitForNavigationOptions().setTimeout(30000.0))
    
    // Verificar se login foi bem-sucedido
    if (page.url().contains("dashboard")) {
        println("✓ Login bem-sucedido!")
    } else {
        println("✗ Login falhou!")
    }
}

// ============================================================================
// EXEMPLO 3: BUSCAR ELEMENTO (Diferentes estratégias de seletor)
// ============================================================================

suspend fun exemploBuscarElementos() {
    val page = browser!!.newPage()
    
    // Estratégia 1: Seletor CSS
    val botaoBuscar = page.querySelector("button.btn-buscar")
    
    // Estratégia 2: XPath (mais complexo, mas mais flexível)
    val input = page.querySelector("//input[@placeholder='Pesquisar paciente']")
    
    // Estratégia 3: Text content (buscar por texto)
    val botao = page.querySelector("button:has-text('Buscar')")
    
    // Estratégia 4: Atributo específico
    val link = page.querySelector("a[data-test='download-pdf']")
    
    // Clicar no elemento encontrado
    botaoBuscar?.click()
}

// ============================================================================
// EXEMPLO 4: PREENCHER FORMULÁRIO E BUSCAR
// ============================================================================

suspend fun exemploFormulareBusca(nomePaciente: String) {
    val page = browser!!.newPage()
    
    // Navegar para página de busca
    page.navigate("https://exemplo.com/busca")
    page.waitForLoadState()
    
    // Limpar campo se já houver texto
    page.fill("input#busca-paciente", "")
    
    // Preencher nome do paciente
    page.fill("input#busca-paciente", nomePaciente)
    
    // Clique em buscar
    page.click("button#btn-buscar")
    
    // Aguardar resultados aparecerem
    page.waitForSelector("table.resultados", Page.WaitForSelectorOptions().setTimeout(10000.0))
    
    println("✓ Busca realizada para: $nomePaciente")
}

// ============================================================================
// EXEMPLO 5: EXTRAIR DADOS DE TABELA (WEB SCRAPING)
// ============================================================================

suspend fun exemploExtrairDados(): List<Map<String, String>> {
    val page = browser!!.newPage()
    
    val dados = mutableListOf<Map<String, String>>()
    
    // Selecionar todas as linhas da tabela
    val linhas = page.querySelectorAll("table.resultados tbody tr")
    
    for (linha in linhas) {
        // Extrair cada coluna da linha
        val colunas = linha.querySelectorAll("td")
        
        if (colunas.size >= 4) {
            val registro = mapOf(
                "id" to colunas[0].textContent(),
                "tipo" to colunas[1].textContent(),
                "data" to colunas[2].textContent(),
                "status" to colunas[3].textContent()
            )
            dados.add(registro)
        }
    }
    
    println("✓ ${dados.size} registros extraídos")
    return dados
}

// ============================================================================
// EXEMPLO 6: AGUARDAR ELEMENTO E INTERAGIR
// ============================================================================

suspend fun exemploAguardarElemento() {
    val page = browser!!.newPage()
    
    // Aguardar elemento aparecer (útil para carregamentos dinâmicos)
    page.waitForSelector(".loading-spinner", Page.WaitForSelectorOptions().setTimeout(20000.0))
    
    // Aguardar elemento desaparecer (fim do carregamento)
    page.waitForSelector(".loading-spinner", 
        Page.WaitForSelectorOptions()
            .setState(WaitForSelectorOptions.State.HIDDEN)
            .setTimeout(20000.0)
    )
    
    println("✓ Carregamento concluído")
}

// ============================================================================
// EXEMPLO 7: DOWNLOAD DE ARQUIVO COM PLAYWRIGHT
// ============================================================================

suspend fun exemploDownloadArquivo(diretorioDestino: String) {
    val page = browser!!.newPage()
    
    // Listener para capturar download
    val downloadPromise = page.waitForDownloadFunction {
        // Clique no link que inicia o download
        page.click("a[data-action='download-pdf']")
    }
    
    // Aguardar o arquivo ser baixado
    val download = downloadPromise.get()
    
    // Salvar arquivo em local específico
    download.saveAs("$diretorioDestino/${download.suggestedFilename()}")
    
    println("✓ Arquivo baixado: ${download.suggestedFilename()}")
}

// ============================================================================
// EXEMPLO 8: LIDAR COM MÚLTIPLAS PÁGINAS (ABAS/TABS)
// ============================================================================

suspend fun exemploMultiplasPaginas() {
    val browser = Playwright.create().chromium().launch()
    
    // Criar contexto para simular múltiplos usuários
    val context = browser.newContext()
    
    // Primeira página
    val page1 = context.newPage()
    page1.navigate("https://exemplo.com/paciente1")
    
    // Segunda página
    val page2 = context.newPage()
    page2.navigate("https://exemplo.com/paciente2")
    
    // Trabalhar com ambas simultaneamente
    println("Página 1: ${page1.url()}")
    println("Página 2: ${page2.url()}")
    
    // Fechar
    page1.close()
    page2.close()
    context.close()
    browser.close()
}

// ============================================================================
// EXEMPLO 9: TRATAMENTO DE ERROS
// ============================================================================

suspend fun exemploTratamentoErros() {
    try {
        val page = browser!!.newPage()
        
        // Timeout de página
        page.navigate("https://exemplo.com/lento", 
            Page.NavigateOptions().setTimeout(30000.0))
        
        // Timeout de seletor
        try {
            page.waitForSelector("#elemento-inexistente", 
                Page.WaitForSelectorOptions().setTimeout(5000.0))
        } catch (e: PlaywrightException) {
            println("✗ Elemento não encontrado: ${e.message}")
        }
        
    } catch (e: Exception) {
        println("✗ Erro: ${e.message}")
        e.printStackTrace()
    }
}

// ============================================================================
// EXEMPLO 10: SCREENSHOT PARA DEBUG
// ============================================================================

suspend fun exemploScreenshot() {
    val page = browser!!.newPage()
    
    page.navigate("https://exemplo.com")
    
    // Tirar screenshot da página inteira
    page.screenshot(Page.ScreenshotOptions()
        .setPath("src/main/resources/debug-full.png")
        .setFullPage(true)
    )
    
    // Tirar screenshot de um elemento específico
    val elemento = page.querySelector(".resultado")
    elemento?.screenshot(ElementHandle.ScreenshotOptions()
        .setPath("src/main/resources/debug-elemento.png")
    )
    
    println("✓ Screenshots salvos em src/main/resources/")
}

// ============================================================================
// EXEMPLO 11: FLUXO COMPLETO - IMPLEMENTAÇÃO REAL
// ============================================================================

suspend fun exemploFluxoCompleto(usuario: String, senha: String, paciente: String) {
    var playwright: Playwright? = null
    var browser: Browser? = null
    
    try {
        // 1. Inicializar Playwright
        playwright = Playwright.create()
        browser = playwright.chromium().launch(
            BrowserType.LaunchOptions().setHeadless(true)
        )
        val page = browser.newPage()
        
        // 2. Fazer login
        println("1️⃣ Fazendo login...")
        page.navigate("https://exemplo.com/login")
        page.fill("input#usuario", usuario)
        page.fill("input#senha", senha)
        page.click("button:has-text('Entrar')")
        page.waitForNavigation()
        
        // 3. Buscar paciente
        println("2️⃣ Buscando paciente...")
        page.navigate("https://exemplo.com/busca")
        page.fill("input#busca", paciente)
        page.click("button:has-text('Buscar')")
        page.waitForSelector("table.resultados")
        
        // 4. Extrair dados
        println("3️⃣ Extraindo dados...")
        val linhas = page.querySelectorAll("table.resultados tbody tr")
        for (linha in linhas) {
            val colunas = linha.querySelectorAll("td")
            if (colunas.size >= 3) {
                println("  - ${colunas[1].textContent()} (${colunas[2].textContent()})")
            }
        }
        
        // 5. Download
        println("4️⃣ Baixando arquivo...")
        val download = page.waitForDownloadFunction {
            page.click("a[data-type='pdf']")
        }.get()
        download.saveAs("C:/ExamesCentralizados/$paciente/${download.suggestedFilename()}")
        
        // 6. Logout
        println("5️⃣ Fazendo logout...")
        page.click("a[href='/logout']")
        page.waitForNavigation()
        
        println("✅ Fluxo completo realizado com sucesso!")
        
    } catch (e: Exception) {
        println("❌ Erro: ${e.message}")
        e.printStackTrace()
    } finally {
        // 7. Limpar recursos
        browser?.close()
        playwright?.close()
    }
}

// ============================================================================
// EXEMPLO 12: USAR EM CLASSE REAL (AolExameSource)
// ============================================================================

/*
class AolExameSource : ExameSource {
    private var browser: Browser? = null
    private var page: Page? = null
    private var playwright: Playwright? = null
    
    override suspend fun login(usuario: String, senha: String): ResultadoBusca {
        return try {
            playwright = Playwright.create()
            browser = playwright!!.chromium().launch()
            page = browser!!.newPage()
            
            page!!.navigate("$urlBase/login")
            page!!.fill("input[name='email']", usuario)
            page!!.fill("input[name='password']", senha)
            page!!.click("button[type='submit']")
            page!!.waitForNavigation()
            
            if (page!!.url().contains("dashboard")) {
                ResultadoBusca(sucesso = true, mensagem = "Login bem-sucedido")
            } else {
                ResultadoBusca(sucesso = false, mensagem = "Login falhou")
            }
        } catch (e: Exception) {
            ResultadoBusca(sucesso = false, erro = e)
        }
    }
    
    override suspend fun buscarExames(pacienteNome: String): ResultadoBusca {
        return try {
            page!!.navigate("$urlBase/busca")
            page!!.fill("input#busca", pacienteNome)
            page!!.click("button:has-text('Buscar')")
            page!!.waitForSelector("table.exames")
            
            val exames = mutableListOf<Exame>()
            val linhas = page!!.querySelectorAll("table.exames tr")
            
            for (linha in linhas) {
                val colunas = linha.querySelectorAll("td")
                if (colunas.size >= 4) {
                    exames.add(Exame(
                        id = colunas[0].textContent(),
                        pacienteNome = pacienteNome,
                        tipoExame = colunas[1].textContent(),
                        data = colunas[2].textContent(),
                        plataforma = nomePlataforma
                    ))
                }
            }
            
            ResultadoBusca(sucesso = true, exames = exames)
        } catch (e: Exception) {
            ResultadoBusca(sucesso = false, erro = e)
        }
    }
    
    override suspend fun finalizar() {
        page?.close()
        browser?.close()
        playwright?.close()
    }
}
*/

