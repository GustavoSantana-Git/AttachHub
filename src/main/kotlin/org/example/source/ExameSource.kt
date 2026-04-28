package org.example.source

import org.example.model.Exame
import org.example.model.ResultadoBusca
import java.io.File

/**
 * Interface que define o contrato para implementações de busca de exames em diferentes plataformas.
 * Segue o padrão Strategy, permitindo que cada plataforma (RWE, AOL, +Laudo) tenha sua própria implementação.
 */
interface ExameSource {
    
    /**
     * Nome identificador da plataforma
     */
    val nomePlataforma: String
    
    /**
     * URL base da plataforma (será sobrescrita pelas implementações)
     */
    val urlBase: String
    
    /**
     * Realiza login na plataforma com as credenciais fornecidas
     * 
     * @param usuario Nome de usuário/email
     * @param senha Senha de acesso
     * @return ResultadoBusca indicando sucesso ou falha
     */
    suspend fun login(usuario: String, senha: String): ResultadoBusca
    
    /**
     * Busca exames de um paciente específico na plataforma
     * 
     * @param pacienteNome Nome do paciente a buscar
     * @return ResultadoBusca contendo a lista de exames encontrados
     */
    suspend fun buscarExames(pacienteNome: String): ResultadoBusca
    
    /**
     * Baixa um arquivo PDF de exame e retorna o caminho onde foi salvo
     * 
     * @param exame Objeto contendo informações do exame
     * @param diretorioDestino Diretório onde salvar o arquivo
     * @return Arquivo baixado ou null se falhar
     */
    suspend fun baixarExame(exame: Exame, diretorioDestino: File): File?
    
    /**
     * Realiza logout da plataforma
     */
    suspend fun logout(): ResultadoBusca
    
    /**
     * Limpa recursos (fecha navegador, conexões, etc)
     */
    suspend fun finalizar()
}

