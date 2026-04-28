# 📚 INSTRUÇÕES DE SETUP DO PROJETO

## 1️⃣ CONFIGURAÇÃO DO JDK 21

### Windows 10/11 com IntelliJ IDEA

#### Opção A: Usar IntelliJ IDEA para gerenciar JDK
1. Abra **IntelliJ IDEA**
2. Vá para **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
3. Em "Gradle JVM", selecione **"java version 21"**
4. Se não aparecer, clique em "Download JDK..." e selecione **OpenJDK 21 (Eclipse Temurin)**
5. Clique OK

#### Opção B: Configurar manualmente
1. Download JDK 21:
   - [Eclipse Temurin](https://adoptium.net/)
   - Ou [Amazon Corretto](https://aws.amazon.com/pt/corretto/)
   - Ou [Oracle JDK](https://www.oracle.com/br/java/technologies/downloads/#java21)

2. Instale em: `C:\Program Files\Java\jdk-21` (ou local similar)

3. Defina variável de ambiente:
   ```powershell
   # PowerShell (como Administrador)
   [Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21", "Machine")
   ```

4. Verify:
   ```powershell
   java -version
   ```
   Deve retornar: `java version "21.x.x"`

---

## 2️⃣ PRIMEIRO BUILD DO PROJETO

### No terminal (PowerShell):
```powershell
cd "C:\Users\gusma\Documents\CentralAnexo"
./gradlew build --no-daemon
```

### Ou no IntelliJ IDEA:
1. Clique em **File → Invalidate Caches**
2. Clique em **Build → Rebuild Project**

**Primeira execução**: Pode levar 5-10 minutos (downloads de dependências)

---

## 3️⃣ DOWNLOAD DE NAVEGADORES (Playwright)

Ao executar pela primeira vez, o Playwright baixará automaticamente os navegadores:

```powershell
# PowerShell
cd "C:\Users\gusma\Documents\CentralAnexo"
./gradlew run
```

**Local de instalação**: 
- `C:\Users\{usuario}\AppData\Local\ms-playwright`

---

## 4️⃣ ESTRUTURA DE DIRETÓRIOS ESPERADA

```
C:\Users\gusma\Documents\CentralAnexo/
├── build/                          # ✓ Criado automaticamente
├── gradle/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── org/example/
│   │   │       ├── Main.kt
│   │   │       ├── model/
│   │   │       ├── source/
│   │   │       └── service/
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
├── logs/                           # ✓ Criado na primeira execução
├── build.gradle.kts                # ✓ Configurado
├── gradle.properties
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
└── GUIA_PLAYWRIGHT.kt
```

---

## 5️⃣ TESTANDO A CONFIGURAÇÃO

### Opção 1: Executar via IntelliJ
1. Abra `src/main/kotlin/Main.kt`
2. Clique com botão direito → **Run 'MainKt'**

Ou pressione `Ctrl+Shift+F10`

### Opção 2: Executar via Terminal
```powershell
cd "C:\Users\gusma\Documents\CentralAnexo"
./gradlew run
```

---

## 6️⃣ DEPENDÊNCIAS INSTALADAS

| Dependência | Versão | Propósito |
|---|---|---|
| Playwright | 1.48.2 | Automação web |
| Kotlin Coroutines | 1.8.1 | Programação assíncrona |
| SLF4J | 2.0.13 | Interface de logging |
| Logback | 1.5.6 | Implementação de logging |
| JUnit Jupiter | 5.10.2 | Framework de testes |

---

## 7️⃣ ESTRUTURA DE ARQUIVOS (Classes e Interfaces)

```
org.example/
├── Main.kt
│   └── fun main() : Entrada da aplicação
│
├── model/
│   ├── Exame.kt
│   │   └── data class Exame : Dados de um exame
│   └── ResultadoBusca.kt
│       └── data class ResultadoBusca : Resultado de uma operação
│
├── source/
│   ├── ExameSource.kt
│   │   └── interface ExameSource : Contrato (Strategy)
│   ├── AolExameSource.kt
│   │   └── class AolExameSource : Implementação para AOL
│   ├── RweExameSource.kt
│   │   └── class RweExameSource : Implementação para RWE
│   └── LaudoExameSource.kt
│       └── class LaudoExameSource : Implementação para +Laudo
│
└── service/
    └── GerenciadorExames.kt
        └── class GerenciadorExames : Orquestrador
```

---

## 8️⃣ PADRÕES DE DESIGN APLICADOS

### Strategy Pattern
```
ExameSource (interface)
    ├── AolExameSource
    ├── RweExameSource
    └── LaudoExameSource
```
→ Cada plataforma é uma estratégia diferente

### Async/Await (Kotlin Coroutines)
```kotlin
// Executa buscas em paralelo
val resultados = gerenciador.buscarExamesEmTodasPlataformas(...)
```

---

## 9️⃣ PRÓXIMOS PASSOS

1. **Configurar credenciais** (editar Main.kt)
2. **Implementar webscraping** em cada `*ExameSource.kt`
   - Consulte `GUIA_PLAYWRIGHT.kt` para exemplos
3. **Testar com dados reais**
4. **Adicionar testes unitários** em `src/test/kotlin/`

---

## 🔟 TROUBLESHOOTING

### ❌ "JAVA_HOME is not set"
**Solução**: Siga o passo 1️⃣

### ❌ "Playwright timeout"
**Solução**: Pode ser rede lenta ou site fora. Aumentar timeout em `GUIA_PLAYWRIGHT.kt`

### ❌ "XPath/Seletor não encontrado"
**Solução**: Usar browser.dev_tools() ou Chrome DevTools (F12) para inspecionar elementos

### ❌ "Build com erro"
**Solução**:
```powershell
./gradlew clean build --refresh-dependencies
```

---

## 📞 SUPORTE

Se encontrar problemas:
1. Verifique os logs em `logs/centralanexo.log`
2. Consulte `GUIA_PLAYWRIGHT.kt` para exemplos
3. Abra Chrome DevTools (F12) para inspecionar elementos

---

**Última atualização**: Abril 2024

