package com.example.movieapp.models

data class TitleDetailsResponse(
    val id: Int,
    val title: String,
    val plot_overview: String,
    val type: String,
    val runtime_minutes: Int?,
    val year: Int,
    val end_year: Int?,
    val release_date: String,
    val user_rating: Float,
    val critic_score: Int?,
    val us_rating: String,
    val poster: String,
    val original_language: String,
)
