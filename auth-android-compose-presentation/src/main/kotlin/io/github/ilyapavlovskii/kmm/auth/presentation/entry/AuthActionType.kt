package io.github.ilyapavlovskii.kmm.auth.presentation.entry

enum class AuthActionType(
    internal val route: String,
) {
    EMAIL("auth/sign_in_with_email"),
    PHONE("auth/sign_in_with_phone"),
    ;
}
