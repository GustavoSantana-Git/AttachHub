package org.example.source

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue // Import correto do JUnit 5

class RweExameSourceTest {

    @Test
    fun `deve realizar login no RWE com sucesso`() = runTest {
        // 1. Instancia a classe que queremos testar
        val rweSource = RweExameSource()

        // 2. Executa o método de login
        // Como o login já busca as credenciais do seu .env, não precisamos passar nada fixo aqui se não quiser
        val resultado = rweSource.login("usuario_teste", "senha_teste")

        // 3. Verifica se o resultado foi o esperado
        println("Mensagem do sistema: ${resultado.mensagem}")

        assertTrue(resultado.sucesso, "O login deveria ter retornado sucesso=true")
    }
}