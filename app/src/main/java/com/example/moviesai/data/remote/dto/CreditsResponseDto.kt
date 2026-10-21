package com.example.moviesai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreditsResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<CastDto>?
)

data class CastDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("character")
    val character: String?,
    @SerializedName("profile_path")
    val profilePath: String?
)
