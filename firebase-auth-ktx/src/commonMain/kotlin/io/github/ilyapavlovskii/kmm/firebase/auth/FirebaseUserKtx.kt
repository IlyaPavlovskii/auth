package io.github.ilyapavlovskii.kmm.firebase.auth

import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dev.gitlive.firebase.auth.FirebaseUser
import io.github.ilyapavlovskii.kmm.common.domain.model.Password
import io.github.ilyapavlovskii.kmm.firebase.auth.model.user.UpdatePasswordResult

suspend fun FirebaseUser.updatePassword(
    password: Password,
): UpdatePasswordResult = try {
    this.updatePassword(password = password.value)
    UpdatePasswordResult.Success
} catch (farlre: FirebaseAuthRecentLoginRequiredException) {
    UpdatePasswordResult.Error.AuthorizationRequired(farlre)
} catch (faiue: FirebaseAuthInvalidUserException) {
    UpdatePasswordResult.Error.InvalidUser(faiue)
} catch (ex: Throwable) {
    UpdatePasswordResult.Error.Other(ex)
}
