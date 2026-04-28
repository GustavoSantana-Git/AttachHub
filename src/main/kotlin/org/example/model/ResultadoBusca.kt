package org.example.model

/**
 * Classe que encapsula o resultado de uma busca de exames
 */
data class ResultadoBusca(
    val sucesso: Boolean,
    val exames: List<Exame> = emptyList(),
    val mensagem: String = "",
    val erro: Exception? = null
)

