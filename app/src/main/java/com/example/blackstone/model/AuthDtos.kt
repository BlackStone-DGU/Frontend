package com.example.blackstone.model

data class AffiliationReq(
    val name: String
)

data class SignupReq(
    val email: String,
    val password: String,
    val name: String,
    val affiliation: AffiliationReq
)

data class LoginReq(
    val email: String,
    val password: String
)

data class ApiMessage(
    val message: String? = null
)