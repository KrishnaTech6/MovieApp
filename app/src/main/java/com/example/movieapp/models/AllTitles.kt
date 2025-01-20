package com.example.movieapp.models


data class TitlesResponse(
    val titles: List<Title>,
    val total_results: Int,
    val page: Int,
    val total_pages: Int
)

data class Title(
    val id: Int,
    val title: String,
    val year: Int,
    val type: String,
    val tmdb_type: String
)
