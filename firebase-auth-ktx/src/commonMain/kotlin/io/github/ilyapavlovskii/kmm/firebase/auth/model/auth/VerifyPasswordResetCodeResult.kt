package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

sealed class VerifyPasswordResetCodeResult {
    object Success : VerifyPasswordResetCodeResult()
    sealed class Error : VerifyPasswordResetCodeResult() {
        object InvalidCode : Error()
        data class Other(val throwable: Throwable) : Error()
    }
}
