package com.example.eng_words_reminder_client.network.responses

data class ResponseGetTenWords(
    val hash: String,
    val term: String,
    val meanings: List<String>
)
