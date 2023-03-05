package com.example.buildupfrontend.retrofit

data class SignUpProfile(
    val nickname: String,
    val email: String,
    val school: String,
    val major: String,
    val grade: String,
    val schoolPublicYn: String,
    val interests: List<String>,
)