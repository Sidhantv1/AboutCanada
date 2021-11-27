package com.example.aboutcanada.dataclass

/**
 * Data class of Facts Response Model of the API
 */
data class FactsDataClassResponseModel(
    val rows: List<Row>,
    val title: String
)