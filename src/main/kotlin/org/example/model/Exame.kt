package org.example.model

/**
 * Classe de dados que representa um exame a ser baixado
 */
data class Exame(
    val id: String,                 // ID único do exame na plataforma
    val pacienteNome: String,       // Nome do paciente
    val tipoExame: String,          // Tipo de exame (ex: "Radiografia", "Ultrassom")
    val data: String,               // Data do exame
    val plataforma: String,         // Qual plataforma (RWE, AOL, +Laudo)
    val downloadUrl: String? = null // URL para download (pode ser preenchida após navegação)
)

