# CentralAnexo - Centralizador de Exames

## 📋 Sobre o Projeto

**CentralAnexo** é um sistema de automação que centraliza a busca e download de exames médicos de múltiplas plataformas web (RWE, AOL, +Laudo) de forma automatizada e assíncrona.

---

## 🎯 Objetivo

1. ✅ Fazer login em 3 sistemas web diferentes
2. ✅ Pesquisar por um paciente específico em cada plataforma
3. ✅ Baixar arquivos PDF de exames automaticamente
4. ✅ Organizar arquivos em diretório estruturado: `C:/ExamesCentralizados/{NomePaciente}/{exame}.pdf`

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **Kotlin** | 2.3.0 | Linguagem principal |
| **JDK** | 21 | Runtime Java |
| **Playwright** | 1.48.2 | Automação web (RPA) |
| **Kotlin Coroutines** | 1.8.1 | Operações assíncronas |
| **Logback** | 1.5.6 | Sistema de logging |
| **Gradle** | 8.x | Build system (Kotlin DSL) |

---

## 📁 Estrutura do Projeto

```
src/main/kotlin/org/example/
├── Main.kt                          # Função principal com exemplo
├── model/
│   ├── Exame.kt                    # Data class com info do exame
│   └── ResultadoBusca.kt           # Data class com resultado de operações
├── source/                          # Implementações Strategy
│   ├── ExameSource.kt              # Interface (contrato)
│   ├── AolExameSource.kt           # Implementação AOL
│   ├── RweExameSource.kt           # Implementação RWE
│   └── LaudoExameSource.kt         # Implementação +Laudo
└── service/
    └── GerenciadorExames.kt        # Orquestrador central
```

---

## 🏛️ Padrões de Design Utilizados

### 1. **Strategy Pattern**
Cada plataforma tem sua própria implementação de `ExameSource`:
```kotlin
// Interface (contrato)
interface ExameSource {
    suspend fun login(usuario: String, senha: String): ResultadoBusca
    suspend fun buscarExames(pacienteNome: String): ResultadoBusca
    suspend fun baixarExame(exame: Exame, diretorioDestino: File): File?
}

// Implementações específicas
class AolExameSource : ExameSource { /* ... */ }
class RweExameSource : ExameSource { /* ... */ }
class LaudoExameSource : ExameSource { /* ... */ }
```

**Vantagem**: Adicionar nova plataforma é trivial - apenas criar uma nova classe!

### 2. **Kotlin Coroutines** (Programação Assíncrona)
Permite buscar em múltiplas plataformas simultaneamente:
```kotlin
// Busca síncrona (lento) - serial
buscarPlataformaAOL()      // ⏳ 5 segundos
buscarPlataformaRWE()      // ⏳ 5 segundos
buscarPlataformaLaudo()    // ⏳ 5 segundos
// Total: 15 segundos

// Busca com coroutines (rápido) - paralelo
val resultados = gerenciador.buscarExamesEmTodasPlataformas(nomePaciente, credenciais)
// Total: ~5 segundos (executadas em paralelo!)
```

---

## 🚀 Como Usar

### 1. Compilar o Projeto
```bash
cd C:\Users\gusma\Documents\CentralAnexo
./gradlew build
```

### 2. Executar
```bash
./gradlew run
```

Ou executar pelo IntelliJ IDEA:
- Clique com botão direito em `Main.kt`
- Selecione "Run 'MainKt'"

### 3. Configurar Credenciais
Edite o arquivo `Main.kt` e procure por:
```kotlin
val credenciais = mapOf(
    "AOL" to ("seu_usuario_aol" to "sua_senha_aol"),
    "RWE" to ("seu_usuario_rwe" to "sua_senha_rwe"),
    "+Laudo" to ("seu_usuario_laudo" to "sua_senha_laudo")
)
```

**⚠️ Segurança**: Em produção, use variáveis de ambiente ou arquivos de configuração!

---

## 💻 Implementando o Webscraping (Playwright)

Cada classe `*ExameSource` tem comentários `// TODO:` mostrando onde implementar:

### Exemplo: AolExameSource.kt

#### 1. **Login**
```kotlin
override suspend fun login(usuario: String, senha: String): ResultadoBusca {
    // Abrir navegador
    val playwright = Playwright.create()
    val browser = playwright.chromium().launch()
    val page = browser.newPage()
    
    // Navegar para login
    page.navigate("$urlBase/login")
    
    // Preencher formulário
    page.fill("input[name='usuario']", usuario)
    page.fill("input[name='senha']", senha)
    page.click("button[type='submit']")
    
    // Aguardar navegação
    page.waitForNavigation()
    
    return ResultadoBusca(sucesso = true)
}
```

#### 2. **Buscar Exames**
```kotlin
override suspend fun buscarExames(pacienteNome: String): ResultadoBusca {
    // Navegar para busca
    page.navigate("$urlBase/busca")
    
    // Preencher campo de busca
    page.fill("input[id='busca-paciente']", pacienteNome)
    page.click("button[id='btn-buscar']")
    
    // Aguardar resultados
    page.waitForSelector("table.resultados")
    
    // Extrair dados (web scraping)
    val exames = mutableListOf<Exame>()
    val linhas = page.querySelectorAll("table.resultados tr")
    
    for (linha in linhas) {
        val id = linha.querySelector("td:nth-child(1)")?.textContent() ?: ""
        val tipo = linha.querySelector("td:nth-child(2)")?.textContent() ?: ""
        val data = linha.querySelector("td:nth-child(3)")?.textContent() ?: ""
        
        exames.add(Exame(
            id = id,
            pacienteNome = pacienteNome,
            tipoExame = tipo,
            data = data,
            plataforma = nomePlataforma
        ))
    }
    
    return ResultadoBusca(sucesso = true, exames = exames)
}
```

#### 3. **Baixar PDF**
```kotlin
override suspend fun baixarExame(exame: Exame, diretorioDestino: File): File? {
    // Listener para downloads
    page.onDownload { download ->
        val caminhoTemp = diretorioDestino.resolve(download.suggestedFilename())
        download.saveAs(caminhoTemp.toPath())
    }
    
    // Clique em download
    page.click("a[href*='${exame.id}'][data-type='pdf']")
    
    // Aguardar
    delay(5000)
    
    // Retornar arquivo salvo
    return diretorioDestino.listFiles()?.firstOrNull()
}
```

---

## 📊 Fluxo de Execução

```
┌─────────────────────────────────────────────────────────┐
│  MAIN.KT - Função Principal                             │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  1. Criar Instâncias das Plataformas (Strategy Pattern) │
│     • AolExameSource()                                  │
│     • RweExameSource()                                  │
│     • LaudoExameSource()                                │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  2. Criar GerenciadorExames                             │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  3. Chamar buscarExamesEmTodasPlataformas()              │
│     (Executa em PARALELO com Coroutines)                │
└─────────────────────────────────────────────────────────┘
                          ↓
        ┌─────────┬─────────┬──────────┐
        ↓         ↓         ↓          ↓
     AOL         RWE      +Laudo    (paralelo)
     login       login    login
     busca       busca    busca
     logout      logout   logout
        ↑         ↑         ↑
        └─────────┼─────────┘
                  ↓
┌─────────────────────────────────────────────────────────┐
│  4. Processar Resultados                                │
│     • Validar sucesso/erro                              │
│     • Listar exames encontrados                         │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  5. Baixar e Organizar Exames                           │
│     • Criar diretório: C:/ExamesCentralizados/{paciente}│
│     • Baixar PDFs                                       │
│     • Organizar por tipo/data                           │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  6. Exibir Resumo Final                                 │
└─────────────────────────────────────────────────────────┘
```

---

## 🔍 Logs

Os logs são salvos em:
- **Console**: Feedback imediato durante execução
- **Arquivo**: `logs/centralanexo.log`

Exemplo de saída:
```
09:45:32.123 [INFO] [org.example.Main] - ========== INICIANDO CENTRALANEXO ==========
09:45:32.456 [INFO] [org.example.Main] - 3 plataforma(s) configurada(s)
09:45:32.457 [INFO] [org.example.Main] -   - AOL
09:45:32.457 [INFO] [org.example.Main] -   - RWE
09:45:32.457 [INFO] [org.example.Main] -   - +Laudo
09:45:32.789 [INFO] [org.example.Main] - >>> Buscando exames do paciente: Fulano <<<
09:45:38.101 [INFO] [org.example.Main] - --- PLATAFORMA: AOL ---
09:45:38.102 [INFO] [org.example.Main] - ✓ Busca bem-sucedida!
```

---

## 🧪 Próximos Passos

- [ ] Implementar webscraping específico para cada plataforma
- [ ] Adicionar testes unitários
- [ ] Criar arquivo de configuração externo (YAML/JSON)
- [ ] Implementar banco de dados para histórico de buscas
- [ ] Adicionar autenticação 2FA quando necessário
- [ ] Criar interface web (Spring Boot?)
- [ ] Publicar artefatos Docker

---

## 📚 Referências

- [Playwright Documentation](https://playwright.dev/java/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Kotlin DSL for Gradle](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
- [Strategy Pattern](https://refactoring.guru/design-patterns/strategy)

---

## 📝 Notas

- Certifique-se de que o JDK 21 está instalado
- O Playwright baixará automaticamente os navegadores na primeira execução
- Credenciais devem ser armazenadas com segurança
- Respeite a política de termos de serviço das plataformas

---

**Desenvolvido por**: [Seu Nome]  
**Data**: 2024  
**Versão**: 1.0-SNAPSHOT

