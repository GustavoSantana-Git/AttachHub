# 🔐 RWE LOGIN - GUIA PRÁTICO

## 📍 URL: https://cliente.rweclinica.com/

## 🎯 Fluxo de Login Implementado

```
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 1: Inicializar Playwright                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  val playwright = Playwright.create()                              │
│  val browser = playwright.chromium().launch()                      │
│  val page = browser.newPage()                                      │
│                                                                     │
│  ✓ Navegador Chromium aberto                                       │
│  ✓ Nova página criada                                              │
│  ✓ Viewport: 1920x1080                                             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 2: Navegar para RWE                                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  page.navigate("https://cliente.rweclinica.com/")                  │
│  page.waitForLoadState()  // Aguardar carregamento completo         │
│                                                                     │
│  ✓ Página de login carregada                                       │
│  ✓ Todas as imagens e scripts carregados                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 3: Localizar Campos de Login                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Como fazer (Use Chrome DevTools - F12):                           │
│  1. Abra: https://cliente.rweclinica.com/                          │
│  2. Pressione: F12 (Chrome DevTools)                               │
│  3. Clique: Em Assets → Elements                                   │
│  4. Clique: No ícone de inspetor (lupa)                            │
│  5. Clique: No campo de usuário na página                          │
│  6. Procure: O seletor CSS (ex: input#usuario)                     │
│                                                                     │
│  Seletores possíveis:                                              │
│  • input[type='email']                                              │
│  • input[type='text'][placeholder*='usuario']                      │
│  • input#usuario                                                    │
│  • input[name='usuario']                                            │
│                                                                     │
│  Nosso código tenta vários!                                         │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 4: Preencher Formulário                                       │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  page.fill("input[type='email']", "usuario@rweclinica.com")        │
│  page.fill("input[type='password']", "sua_senha")                  │
│                                                                     │
│  ✓ Campo de email preenchido                                       │
│  ✓ Campo de senha preenchido                                       │
│  ✓ Sem clicar em botões ainda                                      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 5: Clicar em Login                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  page.click("button[type='submit']")  // ou outro seletor          │
│                                                                     │
│  ✓ Clique efetuado                                                 │
│  ✓ Página iniciando redirecionamento                               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 6: Aguardar Redirecionamento                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  page.waitForNavigation(                                            │
│      Page.WaitForNavigationOptions().setTimeout(15000.0)           │
│  )                                                                  │
│                                                                     │
│  ⏳ Aguardando (máximo 15 segundos)...                              │
│  ✓ Login processado                                                │
│  ✓ Redirecionado para dashboard/home                               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 7: Validar Sucesso                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  val urlAtual = page.url()                                         │
│                                                                     │
│  URL esperada (SUCESSO):                                           │
│  ✓ https://cliente.rweclinica.com/dashboard                        │
│  ✓ https://cliente.rweclinica.com/home                             │
│  ✓ https://cliente.rweclinica.com/inicio                           │
│  ✓ https://cliente.rweclinica.com/pacientes                        │
│  ✓ (qualquer URL que NÃO seja /login)                             │
│                                                                     │
│  URL de ERRO:                                                      │
│  ✗ https://cliente.rweclinica.com/login (ficou na mesma)          │
│  ✗ https://cliente.rweclinica.com/login?erro=1                    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 8: Retornar Resultado                                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  if (urlAtual.contains("dashboard", ignoreCase = true)) {          │
│      ResultadoBusca(sucesso = true, mensagem = "Login OK")         │
│  } else {                                                           │
│      ResultadoBusca(sucesso = false, mensagem = "Falha")          │
│  }                                                                  │
│                                                                     │
│  ✓ Login bem-sucedido OU ✗ Falha documentada                      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────────┐
│ ETAPA 9: Limpar Recursos                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  finally {                                                          │
│      page.close()                                                   │
│      browser.close()                                               │
│      playwright.close()                                            │
│  }                                                                  │
│                                                                     │
│  ✓ Página fechada                                                  │
│  ✓ Navegador encerrado                                             │
│  ✓ Playwright finalizado                                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔍 Como Encontrar Seletores CSS (Chrome DevTools)

### Passo 1: Abrir DevTools
```
Acesse: https://cliente.rweclinica.com/
Pressione: F12
```

### Passo 2: Inspecionar Elemento
```
1. Clique no ícone de inspetor (lupa) no canto superior esquerdo do DevTools
2. Clique no campo de email/usuário
3. Você verá o HTML destacado
```

### Passo 3: Encontrar o Seletor
```html
<!-- Exemplo do que você vai ver: -->

<div class="form-group">
    <label for="usuario">Usuário:</label>
    <input 
        type="text"
        id="usuario"
        name="usuario"
        class="form-control"
        placeholder="Digite seu usuário"
    />
</div>

<!-- Seletores válidos para este elemento: -->
input#usuario                      ← Por ID (mais específico)
input[type='text']                 ← Por tipo
input[name='usuario']              ← Por nome
input.form-control                 ← Por classe
input[placeholder*='usuário']      ← Por atributo (contém)
```

### Passo 4: Copiar Seletor (Chrome Automático)
```
1. Clique direito no elemento em DevTools
2. Selecione "Copy selector"
3. Cole em: page.fill("AQUI")
```

---

## ✅ Teste o Login Localmente

### 1. Editar Main.kt
```kotlin
// Descomentar teste RWE
val plataformas = listOf(
    RweExameSource()  // ← Apenas RWE para testar
)

val credenciais = mapOf(
    "RWE" to ("SEU_EMAIL_RWE" to "SUA_SENHA_RWE")  // ← Colocar credenciais
)

val nomePaciente = "Fulano"
val resultados = gerenciador.buscarExamesEmTodasPlataformas(nomePaciente, credenciais)
```

### 2. Executar
```powershell
./gradlew run
```

### 3. Ver Resultado
```
[RWE] ========== INICIANDO LOGIN ==========
[RWE] Usuário: seu_email@exemple.com
[RWE] [1/6] Criando instância Playwright...
[RWE] [2/6] Lançando navegador Chromium...
...
[RWE] ✅ LOGIN BEM-SUCEDIDO!
```

---

## 🐛 Se Não Funcionar

### Problema 1: "Elemento não encontrado"
```
Solução:
1. Abra Chrome DevTools (F12)
2. Procure pelo elemento correto
3. Atualize o seletor em RweExameSource.kt
4. Tente novamente
```

### Problema 2: "Timeout"
```
Solução:
1. O site pode estar lento
2. Aumentar timeout: setTimeout(30000.0) → setTimeout(60000.0)
3. Verificar conexão de internet
```

### Problema 3: "Redirecionamento não aconteceu"
```
Solução:
1. Credenciais incorretas?
2. URL mudou? (site pode ter trocado endpoint)
3. Capturar screenshot:
   page.screenshot(Page.ScreenshotOptions()
       .setPath("debug-rwe-login.png")
       .setFullPage(true)
   )
4. Abrir debug-rwe-login.png e inspeccionar
```

### Problema 4: "Credenciais não funcionam"
```
Solução:
1. Testar login manual no Chrome (funciona?)
2. Se não funciona manualmente, credenciais estão erradas
3. Se funciona manualmente, problema é no script
4. Adicionar logs para debug
```

---

## 💡 Dicas Profissionais

### 1. Adicionar Delay se Site Responder Lento
```kotlin
page.navigate(urlBase)
delay(1000)  // 1 segundo extra
page.waitForLoadState()
```

### 2. Capturar Screenshot para Debug
```kotlin
page.screenshot(Page.ScreenshotOptions()
    .setPath("debug-passo-1.png")
    .setFullPage(true)
)
// Arquivo salvo: debug-passo-1.png
```

### 3. Tentar Múltiplos Seletores
```kotlin
try {
    page.fill("input[type='email']", usuario)
} catch (e: Exception) {
    try {
        page.fill("input#usuario", usuario)
    } catch (e2: Exception) {
        page.fill("input[placeholder*='usuario']", usuario)
    }
}
```

### 4. Logar Tudo para Debug
```kotlin
logger.info("[RWE] Antes de navegar")
page.navigate(urlBase)
logger.info("[RWE] Depois de navegar, URL: ${page.url()}")
logger.info("[RWE] Preenchendo usuário...")
page.fill("input[type='email']", usuario)
logger.info("[RWE] Preenchendo senha...")
```

### 5. Não Esquecer do Finally
```kotlin
return try {
    // seu código aqui
} catch (e: Exception) {
    // tratamento de erro
} finally {
    page?.close()
    browser?.close()
    playwright?.close()
}
```

---

## 📊 Código Resumido

```kotlin
override suspend fun login(usuario: String, senha: String): ResultadoBusca {
    return try {
        // 1. Setup
        val playwright = Playwright.create()
        val browser = playwright.chromium().launch()
        val page = browser.newPage()
        
        // 2. Navegar
        page.navigate("$urlBase/")
        page.waitForLoadState()
        
        // 3. Preencher
        page.fill("input[type='email']", usuario)
        page.fill("input[type='password']", senha)
        
        // 4. Clicar
        page.click("button[type='submit']")
        page.waitForNavigation()
        
        // 5. Validar
        val sucesso = !page.url().contains("login")
        
        // 6. Cleanup
        page.close()
        browser.close()
        playwright.close()
        
        // 7. Retornar
        ResultadoBusca(sucesso = sucesso)
        
    } catch (e: Exception) {
        ResultadoBusca(sucesso = false, erro = e)
    }
}
```

---

## 🎯 Próximos Passos

1. ✅ Login implementado ← Você está aqui
2. ⏳ Buscar exames (buscarExames)
3. ⏳ Baixar PDFs (baixarExame)
4. ⏳ Implementar AOL (mesmo processo)
5. ⏳ Implementar +Laudo (mesmo processo)

---

## 📞 Referência Rápida

| Preciso fazer... | Código |
|------------------|--------|
| Navegar | `page.navigate(url)` |
| Preencher campo | `page.fill("selector", valor)` |
| Clicar botão | `page.click("selector")` |
| Aguardar | `page.waitForNavigation()` |
| URL atual | `page.url()` |
| Screenshot | `page.screenshot()` |
| Fechar | `page.close()` |

---

**Boa implementação! 🚀**

