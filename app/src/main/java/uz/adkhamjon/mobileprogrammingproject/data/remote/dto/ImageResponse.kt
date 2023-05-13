package uz.adkhamjon.mobileprogrammingproject.data.remote.dto

data class ImageResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)
