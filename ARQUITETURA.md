# 🎨 RESUMO VISUAL DO PROJETO CENTRALANEXO

## 📊 Diagrama de Arquitetura

```
┌──────────────────────────────────────────────────────────────┐
│                    APLICAÇÃO (Main.kt)                       │
│  - Configura plataformas                                    │
│  - Cria GerenciadorExames                                   │
│  - Inicia busca de exames                                   │
└──────────────────────────────────────────────────────────────┘
                            ↓
┌──────────────────────────────────────────────────────────────┐
│            SERVICE - GerenciadorExames.kt                     │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ • buscarExamesEmTodasPlataformas()                   │   │
│  │ • organizarExames()                                  │   │
│  │ • listarExamesOrganizados()                          │   │
│  │ • finalizarTodos()                                   │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
                            ↓
        ┌───────────────┬───────────────┬───────────────┐
        ↓               ↓               ↓               ↓
    SOURCE 1        SOURCE 2        SOURCE 3      (Extensível)
  ┌────────────┐  ┌────────────┐  ┌────────────┐
  │   AOL      │  │    RWE     │  │  +Laudo    │
  │ (Strategy) │  │ (Strategy) │  │ (Strategy) │
  └────────────┘  └────────────┘  └────────────┘
        ↓               ↓               ↓
   (Playwright) (Playwright) (Playwright)
        ↓               ↓               ↓
  ┌────────────────────────────────────────┐
  │    NAVEGADORES WEB (Chromium)          │
  │    - Login & Automação Web             │
  │    - Busca & Web Scraping              │
  │    - Download de PDFs                  │
  └────────────────────────────────────────┘
        ↓               ↓               ↓
   ┌─────────────────────────────────────┐
   │  C:/ExamesCentralizados/             │
   │  ├── Fulano/                        │
   │  │   ├── Radiografia_2024-01-15.pdf │
   │  │   ├── Ultrassom_2024-01-16.pdf   │
   │  │   └── ...                        │
   │  ├── Ciclano/                       │
   │  │   └── ...                        │
   │  └── ...                            │
   └─────────────────────────────────────┘
```

---

## 📁 ESTRUTURA DE PASTAS (Detalhada)

```
CentralAnexo/
├── 📄 build.gradle.kts           ← Configuração Gradle (DEPENDÊNCIAS)
├── 📄 settings.gradle.kts
├── 📄 gradle.properties
├── 📁 gradle/
│   └── wrapper/                   ← Gradle Wrapper (Auto-download)
├── 📁 src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── org/example/
│   │   │       ├── 📄 Main.kt                    ← ENTRADA
│   │   │       ├── 📁 model/
│   │   │       │   ├── 📄 Exame.kt             ← Data class
│   │   │       │   └── 📄 ResultadoBusca.kt    ← Data class
│   │   │       ├── 📁 source/                   ← STRATEGY PATTERN
│   │   │       │   ├── 📄 ExameSource.kt       ← Interface
│   │   │       │   ├── 📄 AolExameSource.kt    ← Implementação
│   │   │       │   ├── 📄 RweExameSource.kt    ← Implementação
│   │   │       │   └── 📄 LaudoExameSource.kt  ← Implementação
│   │   │       └── 📁 service/
│   │   │           └── 📄 GerenciadorExames.kt ← Orquestrador
│   │   └── resources/
│   │       └── 📄 logback.xml                   ← Configuração logging
│   └── test/
│       └── kotlin/                              ← Testes (TODO)
├── 📁 build/                      ← Artefatos compilados (auto)
├── 📁 logs/                       ← Arquivos de log (gerado em runtime)
├── 🚀 gradlew                     ← Script de execução (Linux/Mac)
├── 🚀 gradlew.bat                ← Script de execução (Windows)
├── 📘 README.md                   ← Documentação principal
├── 📘 SETUP.md                    ← Instruções de setup
├── 📘 GUIA_PLAYWRIGHT.kt          ← Exemplos de Playwright
└── 📘 EXEMPLOS_AVANCADOS.kt       ← Exemplos de extensão
```

---

## 🔄 FLUXO DE DADOS (Execução)

### Cenário: Buscar exames de "Fulano"

```
┌─────────────────────────────────────────────────────────────┐
│ 1. INICIALIZAÇÃO                                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ Main.kt                                                    │
│  ↓                                                         │
│  val plataformas = listOf(                                │
│      AolExameSource(),    ← Nova instância              │
│      RweExameSource(),    ← Nova instância              │
│      LaudoExameSource()   ← Nova instância              │
│  )                                                        │
│  ↓                                                         │
│  val gerenciador = GerenciadorExames(plataformas, ...)  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. BUSCA EM PARALELO (Coroutines)                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  gerenciador.buscarExamesEmTodasPlataformas(                │
│      "Fulano",                                             │
│      credenciais                                           │
│  )                                                          │
│                                                             │
│  ┌────────────────────────────────────────┐               │
│  │ Coroutine 1: AOL        Coroutine 2: RWE        │  
│  │ ├─ login()              ├─ login()               │
│  │ ├─ buscarExames()       ├─ buscarExames()        │
│  │ └─ logout()             └─ logout()              │
│  │                         (Executando em paralelo!)      │
│  │ Coroutine 3: +Laudo                   │
│  │ ├─ login()                            │
│  │ ├─ buscarExames()                     │
│  │ └─ logout()                           │
│  └────────────────────────────────────────┘               │
│                                                             │
│  Total esperado: ~5 segundos (vs 15 segundos sequencial)  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. PROCESSAMENTO DE RESULTADOS                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ resultados = {                                             │
│     "AOL" → ResultadoBusca(                               │
│         sucesso = true,                                   │
│         exames = [Exame(...), Exame(...), ...]           │
│     ),                                                    │
│     "RWE" → ResultadoBusca(                               │
│         sucesso = true,                                   │
│         exames = [Exame(...), Exame(...), ...]           │
│     ),                                                    │
│     "+Laudo" → ResultadoBusca(                            │
│         sucesso = false,                                 │
│         erro = Exception(...)                            │
│     )                                                    │
│ }                                                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. DOWNLOAD E ORGANIZAÇÃO                                  │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ Para cada plataforma com sucesso:                         │
│   ├─ Criar: C:/ExamesCentralizados/Fulano/               │
│   ├─ Para cada exame:                                     │
│   │   ├─ plataforma.baixarExame(exame, destino)          │
│   │   └─ Salvar em: Radiografia_2024-01-15.pdf           │
│   └─ Registrar download em log                            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. RESUMO FINAL                                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ Total de exames encontrados: X                            │
│ Diretório: C:/ExamesCentralizados/Fulano/               │
│ Arquivos salvos:                                         │
│   • Radiografia_2024-01-15.pdf                          │
│   • Ultrassom_2024-01-16.pdf                            │
│   • ...                                                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 PADRÕES DE DESIGN

### 1️⃣ **Strategy Pattern** (Múltiplas Plataformas)

```
ExameSource (Interface - Contrato)
    ↑
    │ implementa
    ├── AolExameSource
    ├── RweExameSource
    ├── LaudoExameSource
    └── (BioExameSource, LabExameSource, ...)

Benefício: Adicionar nova plataforma sem modificar código existente!
```

### 2️⃣ **Async/Await** (Coroutines)

```
Serial (Lento):                 Paralelo (Rápido):
┌─ AOL   5s                     ┌─ AOL   \
├─ RWE   5s  = 15s total       ├─ RWE   ├ 5s total (executam em paralelo)
└─ Laudo 5s                     └─ Laudo /
```

### 3️⃣ **Separação de Responsabilidades**

```
- Main.kt: Orquestração (o que fazer)
- GerenciadorExames: Coordenação (como fazer)
- ExameSource + implementações: Específico por plataforma
- Model: Estruturas de dados
```

---

## 💡 COMO EXPANDIR

### Adicionar Nova Plataforma

```kotlin
// 1. Criar nova classe
class MinhaNovaPlataformaSource : ExameSource {
    override val nomePlataforma = "Minha Plataforma"
    override val urlBase = "https://..."
    
    override suspend fun login(...) { /* ... */ }
    override suspend fun buscarExames(...) { /* ... */ }
    override suspend fun baixarExame(...) { /* ... */ }
    override suspend fun logout() { /* ... */ }
    override suspend fun finalizar() { /* ... */ }
}

// 2. Adicionar em Main.kt
val plataformas = listOf(
    AolExameSource(),
    RweExameSource(),
    LaudoExameSource(),
    MinhaNovaPlataformaSource()  // ← Nova!
)

// 3. Pronto! Funciona automaticamente!
```

### Adicionar Novo Paciente

```kotlin
val pacientes = listOf(
    "Fulano",
    "Ciclano",
    "Beltrano"  // ← Novo paciente
)

for (paciente in pacientes) {
    val resultados = gerenciador.buscarExamesEmTodasPlataformas(paciente, credenciais)
    // ... processar ...
}
```

---

## 📊 ESTATÍSTICAS

| Métrica | Valor |
|---------|-------|
| **Linhas de Código** | ~2000 (comentadas e bem estruturadas) |
| **Classes** | 6 principais |
| **Interfaces** | 1 |
| **Dependências Externas** | 4 (Playwright, Coroutines, SLF4J, Logback) |
| **Tempo de Busca (3 plataformas)** | ~5 segundos (paralelo) |
| **Suporte para Plataformas** | Ilimitado (Strategy Pattern) |

---

## ✅ CHECKLIST DE SETUP

- [ ] JDK 21 instalado e JAVA_HOME configurado
- [ ] Gradle 8.x disponível
- [ ] Dependências baixadas (./gradlew build)
- [ ] Playwright navegadores baixados
- [ ] Credenciais configuradas em Main.kt
- [ ] Lógica de webscraping implementada em *ExameSource.kt
- [ ] Testado com paciente real
- [ ] Diretório C:/ExamesCentralizados/ criado com sucesso

---

## 🚀 PRÓXIMAS FEATURES

- [ ] Banco de dados (SQLite/PostgreSQL) para histórico
- [ ] Interface web (Spring Boot)
- [ ] Agendamento de buscas (Quartz)
- [ ] Notificações por email
- [ ] Cache inteligente
- [ ] Autenticação 2FA
- [ ] Exportar relatórios (PDF)
- [ ] API REST

---

## 📞 REFERÊNCIA RÁPIDA

| Arquivo | Propósito |
|---------|----------|
| `Main.kt` | 🎯 Ponto de entrada |
| `ExameSource.kt` | 📋 Interface (contrato) |
| `*ExameSource.kt` | 💻 Implementações específicas |
| `GerenciadorExames.kt` | 🎮 Orquestrador |
| `GUIA_PLAYWRIGHT.kt` | 📖 Exemplos de código |
| `EXEMPLOS_AVANCADOS.kt` | 🚀 Padrões avançados |

---

**Desenvolvido com ❤️ usando Kotlin + Playwright + Coroutines**

