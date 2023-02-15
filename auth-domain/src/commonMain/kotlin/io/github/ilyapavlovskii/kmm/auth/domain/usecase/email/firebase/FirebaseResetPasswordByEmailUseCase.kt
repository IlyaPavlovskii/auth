package io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.firebase

import dev.gitlive.firebase.auth.FirebaseAuth
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.SendPasswordResetEmailResult
import io.github.ilyapavlovskii.kmm.firebase.auth.sendPasswordResetEmail
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailUseCase

internal class FirebaseResetPasswordByEmailUseCase(
    private val firebaseAuth: FirebaseAuth,
) : ResetPasswordByEmailUseCase {
    override suspend fun execute(
        input: ResetPasswordByEmailInput
    ): ResetPasswordByEmailResult = when (
        firebaseAuth.sendPasswordResetEmail(email = input.email)
    ) {
        is SendPasswordResetEmailResult.Error.InvalidUser ->
            ResetPasswordByEmailResult.Error.InvalidUser

        is SendPasswordResetEmailResult.Error.Other ->
            ResetPasswordByEmailResult.Error.Failed

        SendPasswordResetEmailResult.Success ->
            ResetPasswordByEmailResult.Success
    }
}