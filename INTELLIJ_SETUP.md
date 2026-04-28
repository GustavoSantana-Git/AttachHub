# 🎯 GUIA INTELLIJ IDEA - CentralAnexo

## 1️⃣ Abrir Projeto no IntelliJ

### Opção A: Abrir diretório
1. **File** → **Open**
2. Selecionar: `C:\Users\gusma\Documents\CentralAnexo`
3. Clicar **OK**
4. Quando perguntar, selecionar **Trust Project**

### Opção B: Clonar do Git
1. **File** → **New** → **Project from Version Control**
2. URL: (se tiver repositório)
3. Clone

---

## 2️⃣ Configurar JDK

### Verificar/Configurar JDK 21

1. **File** → **Settings**
   - (ou **IntelliJ IDEA** → **Settings** no Mac)

2. Ir para: **Build, Execution, Deployment** → **Build Tools** → **Gradle**

3. Procurar **Gradle JVM**:
   - Se vazio, clicar em dropdown
   - Selecionar **java version 21** (ou similar)
   - Se não aparecer, clicar **Download JDK...**
   - Selecionar **Eclipse Temurin 21**

4. Clicar **OK**

### Verificar Project JDK

1. **File** → **Project Structure**
2. **Project**
3. Verificar **SDK** = Java 21
4. Se não, selecionar em dropdown ou clicar **+** para adicionar
5. Clicar **OK**

---

## 3️⃣ Primeiro Build

### No IntelliJ:

1. **Build** → **Rebuild Project**
   - Aguardar compilação (primeira vez: 5-10 min)
   - Deve aparecer ✅ Build completed successfully

### Se der erro:
1. **File** → **Invalidate Caches**
2. Selecionar **Invalidate and Restart**
3. Deixar reiniciar
4. **Build** → **Rebuild Project** novamente

---

## 4️⃣ Primeira Execução

### Opção A: Terminal integrado
1. **View** → **Tool Windows** → **Terminal**
2. Digitar:
```powershell
./gradlew run
```
3. Entrar com credenciais quando solicitado (ou deixar como está)

### Opção B: Executar via IDE

1. Abrir `src/main/kotlin/Main.kt`
2. Ver um símbolo ▶ verde na linha `suspend fun main()`
3. Clicar nele
4. Selecionar **Run 'MainKt'**
   - Ou pressionar `Ctrl+Shift+F10`

### Resultado esperado:
```
========== INICIANDO CENTRALANEXO ==========
3 plataforma(s) configurada(s)
  - AOL
  - RWE
  - +Laudo

>>> Buscando exames do paciente: Fulano <<<
...
```

---

## 5️⃣ Estrutura Visual no IntelliJ

### Ver árvore de pastas

À esquerda, deve aparecer:

```
CentralAnexo (projeto)
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── org/example/
│   │   │       ├── Main.kt              ← Clique para editar
│   │   │       ├── model/
│   │   │       │   ├── Exame.kt
│   │   │       │   └── ResultadoBusca.kt
│   │   │       ├── source/
│   │   │       │   ├── ExameSource.kt
│   │   │       │   ├── AolExameSource.kt     ← TODO
│   │   │       │   ├── RweExameSource.kt     ← TODO
│   │   │       │   └── LaudoExameSource.kt   ← TODO
│   │   │       └── service/
│   │   │           └── GerenciadorExames.kt
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
├── build.gradle.kts
├── settings.gradle.kts
└── (outros arquivos)
```

---

## 6️⃣ Navegar e Editar

### Ir para um arquivo
- **Ctrl+N** → Digite nome da classe
- Pressione Enter para abrir

### Ir para um símbolo
- **Ctrl+Shift+Alt+N** → Digite nome do símbolo (função, variável)

### Buscar em projeto
- **Ctrl+F** (em arquivo atual)
- **Ctrl+H** (em todo projeto - Replace)

### Ir para próxima/anterior erro
- **F2** (próximo erro)
- **Shift+F2** (erro anterior)

---

## 7️⃣ Executar Testes

### Criar teste para AolExameSource

1. Clicar direito em `AolExameSource.kt`
2. **Generate** → **Test**
3. Selecionar **JUnit 5**
4. Criar testes para cada método

```kotlin
@Test
suspend fun testLogin() {
    val aol = AolExameSource()
    val resultado = aol.login("user", "pass")
    assertTrue(resultado.sucesso)
}
```

5. Executar:
   - Clicar ▶ verde ao lado do test
   - Ou **Ctrl+Shift+F10**

---

## 🐛 Debug (Breakpoints)

### Adicionar breakpoint

1. Clicar na margem esquerda do código (linha desejada)
2. Deve aparecer um ponto vermelho

### Executar em debug
1. Clicar ▶ na margem (junto ao código)
2. Selecionar **Debug 'MainKt'**
   - Ou pressionar `Ctrl+Shift+D`

### Controlar execução
- **F8**: Próxima linha
- **F7**: Entrar em função
- **Shift+F8**: Sair de função
- **F9**: Continuar até próximo breakpoint

### Ver variáveis
- Hover sobre variável = mostra valor
- **Alt+9**: Abre aba de debug com valores

---

## 📝 Editar Código

### Auto-complete
- Pressionar **Ctrl+Space**
- IntelliJ sugere

### Auto-import
- Clicar em classe não importada
- Pressionar **Alt+Enter**
- Selecionar **Import class**

### Refatorar
- Selecionar código
- **Ctrl+Alt+M**: Extrair para método
- **Ctrl+Alt+V**: Extrair para variável
- **Shift+F6**: Renomear símbolo

### Formatar código
- **Ctrl+Alt+L**: Formatar arquivo
- **Ctrl+Shift+Alt+L**: Abrir dialog de formatação

---

## 🔍 Inspecionar Código

### Tipo de variável
- Hover sobre variável = mostra tipo

### Referências
- Clicar direito em símbolo
- **Find** → **Show Usages**
- Mostra todas as usagens

### Hierarquia de classes
- Clicar direito em classe
- **Analyze** → **Run Inspection by Name**
- Procurar por "hierarchy"

---

## 📚 Recursos do IntelliJ

### Documentação
- Clicar em classe/função
- Pressionar **Ctrl+Q**
- Abre documentação inline

### Intentions
- Posicionar cursor em código
- Pressionar **Alt+Enter**
- Sugere ações rápidas

### Análise
- **Analyze** → **Run Inspection**
- Procura por problemas de código

---

## 🎨 Configurar Tema/Layout

### Tema
- **File** → **Settings**
- **Appearance & Behavior** → **Appearance**
- **Theme**: Selecionar (Dark, Light, Darcula, etc)

### Tamanho de fonte
- Mesmo lugar
- **Font**: Selecionar tamanho

### Layout
- **View** → **Appearance**
- **Tool Windows** → Posicionar onde quer

---

## 💡 Dicas IntelliJ

1. **Atalho principal**: `Ctrl+Shift+A` - Procurar ação

2. **Recarregar Gradle**: `Ctrl+Shift+O`

3. **Limpar cache**: 
   - **File** → **Invalidate Caches and Restart**

4. **Ver histórico de edições**: 
   - **Local History** → **Show History**
   - Clicar direito em arquivo

5. **Duplicar linha**: `Ctrl+D`

6. **Deletar linha**: `Ctrl+Y`

7. **Mover linha**: `Ctrl+Shift+Up/Down`

---

## 🆘 Problemas Comuns

### "Unresolved reference"
**Solução**: 
- Clicar em erro
- Pressionar `Alt+Enter`
- Selecionar `Import class`

### Gradle não sincroniza
**Solução**:
- **File** → **Sync Now**
- Ou clicar no ícone de sincronização

### Não acha classe
**Solução**:
- **File** → **Invalidate Caches and Restart**

### Terminal PowerShell não roda gradlew
**Solução**:
- Usar Command Prompt (cmd.exe) ou PowerShell with `.\gradlew`

---

## ✅ Checklist IntelliJ Setup

- [ ] Projeto aberto no IntelliJ
- [ ] JDK 21 configurado
- [ ] Build bem-sucedido (✅)
- [ ] Estrutura de pastas visível
- [ ] Main.kt aberto
- [ ] Primeira execução funcionou
- [ ] Debug funcionando
- [ ] Conhece os atalhos principais

---

## 🎯 Próximos Passos

1. Abrir `src/main/kotlin/org/example/source/AolExameSource.kt`
2. Procurar por `// TODO:`
3. Implementar usando Playwright (veja GUIA_PLAYWRIGHT.kt)
4. Testar com ▶ verde
5. Repetir para RWE e +Laudo

---

**Boa programação! 🚀**

