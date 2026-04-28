# 🚀 QUICK START - Começar em 5 minutos

## 1️⃣ Configurar Ambiente (1 minuto)

### Verificar Java
```powershell
java -version
# Esperar: java version "21.x.x" ou superior
```

Se não aparecer, fazer download de [Eclipse Temurin JDK 21](https://adoptium.net/)

### Configurar Variável de Ambiente (Windows)
```powershell
# Como Administrador:
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21", "Machine")

# Verificar:
echo $env:JAVA_HOME
```

---

## 2️⃣ Compilar Projeto (2 minutos)

```powershell
cd "C:\Users\gusma\Documents\CentralAnexo"

# Limpar e compilar
./gradlew clean build --no-daemon
```

**Primeira vez**: Vai baixar ~500MB de dependências (pode levar 5-10 min)

---

## 3️⃣ Executar Aplicação (2 minutos)

### Opção A: Terminal
```powershell
cd "C:\Users\gusma\Documents\CentralAnexo"
./gradlew run
```

### Opção B: IntelliJ IDEA
1. Abrir `src/main/kotlin/Main.kt`
2. Clicar botão verde ▶ no lado esquerdo
3. Ou pressionar `Ctrl+Shift+F10`

---

## 4️⃣ Entender a Estrutura (1 minuto)

### Estrutura de Pastas
```
src/main/kotlin/org/example/
├── Main.kt                    ← Executa aqui
├── model/
│   ├── Exame.kt             ← Dados do exame
│   └── ResultadoBusca.kt    ← Resultado de operação
├── source/
│   ├── ExameSource.kt       ← Interface (contrato)
│   ├── AolExameSource.kt    ← Implementação AOL (TODO)
│   ├── RweExameSource.kt    ← Implementação RWE (TODO)
│   └── LaudoExameSource.kt  ← Implementação +Laudo (TODO)
└── service/
    └── GerenciadorExames.kt ← Orquestrador
```

### Fluxo Simplificado
```
Main.kt
  └─ Cria 3 plataformas (Strategy Pattern)
     └─ Usa GerenciadorExames
        └─ Busca em paralelo (Coroutines)
           └─ Salva em C:/ExamesCentralizados/
```

---

## 5️⃣ Começar a Implementar (Agora!)

### Para AOL:

1. Abrir: `src/main/kotlin/org/example/source/AolExameSource.kt`

2. Procurar por: `// TODO: Implementar`

3. Usar exemplo do `GUIA_PLAYWRIGHT.kt`:

```kotlin
// Exemplo simples:
override suspend fun login(usuario: String, senha: String): ResultadoBusca {
    val playwright = Playwright.create()
    val browser = playwright.chromium().launch()
    val page = browser.newPage()
    
    page.navigate("$urlBase/login")
    page.fill("input[name='email']", usuario)
    page.fill("input[name='password']", senha)
    page.click("button[type='submit']")
    page.waitForNavigation()
    
    return ResultadoBusca(sucesso = true, mensagem = "Login OK")
}
```

4. Testar: `./gradlew run`

5. Repetir para RWE e +Laudo

---

## 🎯 O Que Já Está Pronto

- ✅ Estrutura de projeto
- ✅ Interface ExameSource
- ✅ Gerenciador de exames
- ✅ Logging configurado
- ✅ Coroutines setup
- ✅ Main.kt com exemplo
- ✅ Documentação completa

---

## 🤔 O Que Você Precisa Fazer

- ❌ Implementar webscraping (login, busca, download) para cada plataforma

---

## 📚 Documentação

| Arquivo | Conteúdo |
|---------|----------|
| **README.md** | O que é o projeto |
| **SETUP.md** | Como configurar |
| **GUIA_PLAYWRIGHT.kt** | Exemplos de código |
| **EXEMPLOS_AVANCADOS.kt** | Padrões avançados |
| **ARQUITETURA.md** | Visão técnica |
| **IMPLEMENTACAO.md** | Checklist step-by-step |
| **RESUMO.md** | O que foi criado |

---

## 💻 Seu Primeiro Teste

### 1. Editar credenciais em Main.kt:
```kotlin
val credenciais = mapOf(
    "AOL" to ("SEU_EMAIL" to "SUA_SENHA"),     // ← Colocar aqui
    "RWE" to ("SEU_EMAIL" to "SUA_SENHA"),     // ← Colocar aqui
    "+Laudo" to ("SEU_EMAIL" to "SUA_SENHA")   // ← Colocar aqui
)
```

### 2. Executar:
```powershell
./gradlew run
```

### 3. Verificar output:
```
09:45:32.123 [INFO] - ========== INICIANDO CENTRALANEXO ==========
09:45:32.456 [INFO] - 3 plataforma(s) configurada(s)
09:45:32.789 [INFO] - >>> Buscando exames do paciente: Fulano <<<
...
```

### 4. Verificar arquivos:
```powershell
ls "C:\ExamesCentralizados\Fulano\"
```

---

## ⚡ Dicas Rápidas

### Debug com Screenshots
```kotlin
page.screenshot(Page.ScreenshotOptions()
    .setPath("debug-screenshot.png")
    .setFullPage(true)
)
```

### Aumentar Timeout
```kotlin
page.navigate("...", Page.NavigateOptions().setTimeout(60000.0))
```

### Ver Seletores (F12 no Chrome)
```
Clicar em F12
Elements → Inspecionar elemento
Ver o seletor no painel
```

### Logs Detalhados
Editar `src/main/resources/logback.xml`:
```xml
<logger name="org.example" level="DEBUG"/>  <!-- Mais detalhes -->
```

---

## 🆘 Se Algo Quebrar

### Build não compila?
```powershell
./gradlew clean build --refresh-dependencies
```

### JAVA_HOME não definido?
```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21", "Machine")
# Reiniciar PowerShell
```

### Timeout na busca?
1. Aumentar timeout em Page.Navigate()
2. Verificar velocidade da internet
3. Testar site manualmente no Chrome

### Arquivo não baixa?
1. Usar screenshot() para ver página
2. Verificar seletor com F12
3. Verificar se link é realmente PDF

---

## 📋 Checklist Rápido

- [ ] JDK 21 instalado
- [ ] JAVA_HOME configurado
- [ ] Projeto compilado (`./gradlew build`)
- [ ] Entendido o fluxo (Main → Plataformas → Gerenciador)
- [ ] Leitura rápida de GUIA_PLAYWRIGHT.kt
- [ ] AOL implementado
- [ ] RWE implementado
- [ ] +Laudo implementado
- [ ] Tudo testado com dados reais
- [ ] Arquivos salvos corretamente em C:/ExamesCentralizados/

---

## 🎯 Próximos Passos

1. **Agora**: Ler GUIA_PLAYWRIGHT.kt
2. **Depois**: Implementar AOL (2-3 horas)
3. **Depois**: Implementar RWE (1-2 horas)
4. **Depois**: Implementar +Laudo (1-2 horas)
5. **Final**: Testar com múltiplos pacientes

---

## 🎉 Pronto?

```powershell
cd "C:\Users\gusma\Documents\CentralAnexo"
./gradlew run
```

**Boa implementação! 🚀**

---

**Precisa de ajuda?**
- Erros? → Consulte: logs/centralanexo.log
- Dúvidas sobre Playwright? → Consulte: GUIA_PLAYWRIGHT.kt
- Estrutura confusa? → Consulte: ARQUITETURA.md
- Passo a passo? → Consulte: IMPLEMENTACAO.md

