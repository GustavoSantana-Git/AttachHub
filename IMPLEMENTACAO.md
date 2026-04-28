# ✅ CHECKLIST DE IMPLEMENTAÇÃO - CentralAnexo

Siga este checklist para implementar a parte de webscraping em cada plataforma.

---

## 📋 FASE 1: SETUP INICIAL (✓ Já feito)

- [x] JDK 21 configurado
- [x] build.gradle.kts atualizado
- [x] Estrutura de pastas criada
- [x] Interfaces e classes de modelo criadas
- [x] Logging configurado
- [x] Main.kt com exemplo funcionando

---

## 🔧 FASE 2: CONFIGURAR AMBIENTE

### Na sua máquina:

- [ ] **Instalar/Verificar JDK 21**
  ```powershell
  java -version
  # Deve retornar: java version "21.x.x"
  ```

- [ ] **Build do projeto**
  ```powershell
  cd C:\Users\gusma\Documents\CentralAnexo
  ./gradlew clean build --no-daemon
  ```

- [ ] **Download de navegadores Playwright**
  ```powershell
  ./gradlew run
  # Vai baixar Chromium automaticamente (primeira vez)
  ```

- [ ] **Verificar pasta de logs**
  ```powershell
  # Deve existir: C:\Users\{usuario}\Documents\CentralAnexo\logs/
  ```

---

## 🕸️ FASE 3: IMPLEMENTAR WEBSCRAPING - AOL

### Pré-requisitos:
- [ ] Acesso a conta AOL para testes
- [ ] Chrome DevTools conhecimento (F12)
- [ ] Leitura do GUIA_PLAYWRIGHT.kt

### Tarefas:

#### 3.1: Implementar Login
**Arquivo**: `src/main/kotlin/org/example/source/AolExameSource.kt`

- [ ] Abrir navegador Playwright
- [ ] Navegar para `$urlBase/login`
- [ ] Inspecionar HTML dos campos de login (F12)
- [ ] Preencher email/usuário
- [ ] Preencher senha
- [ ] Clicar botão login
- [ ] Aguardar redirecionamento
- [ ] Verificar se login foi bem-sucedido
- [ ] Registrar em logs

**Referência**:
```kotlin
// Abrir navegador
val playwright = Playwright.create()
val browser = playwright.chromium().launch()
val page = browser.newPage()

// Navegar
page.navigate("$urlBase/login")

// Inspecionar elementos (F12 no Chrome)
// Encontrar seletor CSS do campo de email
// Encontrar seletor CSS do campo de senha
// Encontrar seletor CSS do botão

page.fill("input[seletor-identificado]", usuario)
page.fill("input[seletor-identificado]", senha)
page.click("button[seletor-identificado]")
page.waitForNavigation()
```

#### 3.2: Implementar Busca de Exames
**Arquivo**: `src/main/kotlin/org/example/source/AolExameSource.kt`

- [ ] Navegar para página de busca
- [ ] Inspecionar campo de busca (F12)
- [ ] Preencher nome do paciente
- [ ] Clicar em buscar
- [ ] Aguardar resultados carreguem
- [ ] Inspecionar tabela de resultados
- [ ] Extrair dados de cada linha
- [ ] Montar objetos `Exame`
- [ ] Retornar lista em `ResultadoBusca`

**Referência**:
```kotlin
page.navigate("$urlBase/busca")
page.fill("input[seletor]", pacienteNome)
page.click("button[seletor]")
page.waitForSelector("table[seletor]")

// Extrair dados
val exames = mutableListOf<Exame>()
val linhas = page.querySelectorAll("tr[seletor]")

for (linha in linhas) {
    val colunas = linha.querySelectorAll("td")
    // Extrair informações
    exames.add(Exame(...))
}

return ResultadoBusca(sucesso = true, exames = exames)
```

#### 3.3: Implementar Download
**Arquivo**: `src/main/kotlin/org/example/source/AolExameSource.kt`

- [ ] Configurar listener para download
- [ ] Navegar para página do exame (se necessário)
- [ ] Clicar em download PDF
- [ ] Salvar arquivo em diretório de destino
- [ ] Validar arquivo foi criado
- [ ] Retornar objeto File

**Referência**:
```kotlin
val download = page.waitForDownloadFunction {
    page.click("a[data-action='download']")
}.get()

val nomeArquivo = "${exame.tipoExame}_${exame.data}.pdf"
val caminhoFinal = diretorioDestino.resolve(nomeArquivo)
download.saveAs(caminhoFinal.toPath())

return caminhoFinal.toFile()
```

#### 3.4: Testes
- [ ] Testar login com credenciais válidas
  ```powershell
  # Editar Main.kt com credenciais de teste
  ./gradlew run
  ```

- [ ] Testar login com credenciais inválidas
  - Deve retornar `ResultadoBusca(sucesso = false)`

- [ ] Testar busca com paciente existente
  - Deve retornar lista não-vazia de exames

- [ ] Testar busca com paciente inexistente
  - Deve retornar lista vazia ou erro apropriado

- [ ] Testar download
  - Arquivo deve ser criado em `C:/ExamesCentralizados/`
  - Arquivo deve ser PDF válido

---

## 🕸️ FASE 4: IMPLEMENTAR WEBSCRAPING - RWE

**Arquivo**: `src/main/kotlin/org/example/source/RweExameSource.kt`

Seguir o mesmo padrão do AOL:

- [ ] Investigar site RWE (estrutura HTML, seletores)
- [ ] Implementar `login()`
- [ ] Implementar `buscarExames()`
- [ ] Implementar `baixarExame()`
- [ ] Implementar `logout()`
- [ ] Implementar `finalizar()`
- [ ] Testar com credenciais reais
- [ ] Validar que exames são salvos corretamente

---

## 🕸️ FASE 5: IMPLEMENTAR WEBSCRAPING - +LAUDO

**Arquivo**: `src/main/kotlin/org/example/source/LaudoExameSource.kt`

Seguir o mesmo padrão:

- [ ] Investigar site +Laudo
- [ ] Implementar `login()`
- [ ] Implementar `buscarExames()`
- [ ] Implementar `baixarExame()`
- [ ] Implementar `logout()`
- [ ] Implementar `finalizar()`
- [ ] Testar com credenciais reais
- [ ] Validar que exames são salvos corretamente

---

## 🧪 FASE 6: TESTES INTEGRADOS

### Teste 1: Buscar um paciente em uma plataforma
```powershell
./gradlew run
# Esperado: Encontrar e baixar exames de "Fulano"
```

- [ ] Verificar logs
- [ ] Verificar arquivos em `C:/ExamesCentralizados/Fulano/`
- [ ] Verificar que PDFs foram criados

### Teste 2: Buscar um paciente em todas as plataformas
```kotlin
// Main.kt já faz isso!
```

- [ ] Aguardar ~5 segundos (execução paralela)
- [ ] Verificar resultados de todas as plataformas
- [ ] Verificar PDFs de todas as plataformas

### Teste 3: Buscar múltiplos pacientes
```kotlin
// Usar código do EXEMPLOS_AVANCADOS.kt
val pacientes = listOf("Fulano", "Ciclano", "Beltrano")
buscarMultiplosPacientes(gerenciador, pacientes, credenciais)
```

- [ ] Verificar que todos os pacientes foram processados
- [ ] Verificar que diretórios foram criados para cada um
- [ ] Verificar logs de progresso

### Teste 4: Tratamento de erros
- [ ] Credenciais inválidas: Deve retornar erro apropriado
- [ ] Paciente inexistente: Deve retornar lista vazia ou aviso
- [ ] Site fora do ar: Deve capturar exceção e registrar
- [ ] Timeout de conexão: Deve respeitar timeout configurado

---

## 📊 FASE 7: MELHORIAS

### Performance
- [ ] Medir tempo de execução (deve ser ~5 segundos para 3 plataformas)
- [ ] Adicionar cache de resultados (EXEMPLOS_AVANCADOS.kt)
- [ ] Otimizar seletores CSS (evitar querySelectorAll lento)

### Robustez
- [ ] Adicionar retry logic (EXEMPLOS_AVANCADOS.kt)
- [ ] Adicionar timeout configurável
- [ ] Melhorar mensagens de erro
- [ ] Adicionar validação de arquivos baixados

### Funcionalidade
- [ ] Adicionar filtro de tipos de exame
- [ ] Adicionar filtro por data
- [ ] Persistir resultados em arquivo JSON
- [ ] Adicionar notificações por email

---

## 🔒 FASE 8: SEGURANÇA

- [ ] Remover credenciais hardcoded do código
  ```kotlin
  // ❌ Não fazer isso:
  val credenciais = mapOf("AOL" to ("usuario" to "senha"))
  
  // ✅ Fazer isso:
  val usuario = System.getenv("AOL_USER")
  val senha = System.getenv("AOL_PASSWORD")
  ```

- [ ] Usar arquivo de configuração externo
- [ ] Adicionar validação de entrada
- [ ] Validar URLs antes de navegar
- [ ] Sanitizar nomes de arquivos

---

## 📝 FASE 9: DOCUMENTAÇÃO

- [ ] Adicionar comentários em cada método
- [ ] Documentar parâmetros e retornos
- [ ] Adicionar exemplos de uso
- [ ] Atualizar README.md com status
- [ ] Criar guia de troubleshooting

---

## 🚀 FASE 10: IMPLANTAÇÃO

- [ ] Testes em ambiente de produção
- [ ] Monitoramento de logs
- [ ] Backup de arquivos
- [ ] Agendamento de execuções (Cron/Task Scheduler)
- [ ] Alertas de erro

---

## 📈 CHECKLIST FINAL

Quando tudo estiver pronto:

- [ ] Todas as 3 plataformas funcionando
- [ ] Testes passando
- [ ] Documentação completa
- [ ] Código comentado
- [ ] Segurança validada
- [ ] Performance aceita
- [ ] Logs sendo gerados corretamente
- [ ] Diretório de saída estruturado

---

## 🎯 TEMPO ESTIMADO

| Fase | Tempo |
|------|-------|
| Setup | 30 min |
| AOL | 2-3 horas |
| RWE | 1-2 horas |
| +Laudo | 1-2 horas |
| Testes | 1 hora |
| Melhorias | 2-3 horas |
| **Total** | **8-12 horas** |

---

## 💬 DÚVIDAS FREQUENTES

### P: Como inspecionar elementos HTML?
**R**: Abrir site no Chrome → F12 → Inspecionar elemento

### P: Como encontrar seletores CSS?
**R**: F12 → Elements → Clicar na lupa → Clicar no elemento → Ver seletor no painel

### P: Tá lento demais?
**R**: Pode ser rede ou site lento. Tentar aumentar timeout em `Page.WaitForNavigationOptions()`

### P: Arquivo não está sendo baixado?
**R**: Checar se seletor está correto. Usar screenshot() para debug

### P: Preciso adicionar nova plataforma?
**R**: Criar classe `NovaPlataformaSource` que implementa `ExameSource` e adicionar em `Main.kt`

---

## 📞 SUPORTE

Se travado:
1. Verificar logs em `logs/centralanexo.log`
2. Consultar `GUIA_PLAYWRIGHT.kt`
3. Usar screenshots para debug (exemplo 10)
4. Aumentar verbosidade de logs em `logback.xml`

---

**Data de Criação**: Abril 2024  
**Versão**: 1.0  
**Status**: ✅ Pronto para implementação

