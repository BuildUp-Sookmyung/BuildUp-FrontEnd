package com.example.buildupfrontend.retrofit.Request

data class SignUpLocalRequest(
    val username: String?,
    val password: String?,
    val profile: Profile,
    val emailAgreeYn: String
)

data class SignUpSocialRequest(
    val provider: String?,
    val profile: Profile,
    val emailAgreeYn: String
)

data class Profile(
    val nickname: String?,
    val email: String?,
    val school: String?,
    val major: String?,
    val grade: String?,
    val schoolPublicYn: String,
    val interests: List<String>
)
