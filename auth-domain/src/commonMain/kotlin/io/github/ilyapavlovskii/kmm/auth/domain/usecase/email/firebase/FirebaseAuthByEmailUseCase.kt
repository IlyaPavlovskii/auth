package io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.firebase

import dev.gitlive.firebase.auth.FirebaseAuth
import io.github.ilyapavlovskii.kmm.firebase.auth.createUserWithEmailAndPassword
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.CreateUserWithEmailAndPasswordResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.SignInUserWithEmailAndPasswordResult
import io.github.ilyapavlovskii.kmm.firebase.auth.signInWithEmailAndPassword
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.AuthByEmailInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.SignByEmailResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.AuthByEmailUseCase

internal class FirebaseAuthByEmailUseCase(
    private val firebaseAuth: FirebaseAuth,
) : AuthByEmailUseCase {
    override suspend fun execute(input: AuthByEmailInput): SignByEmailResult = signIn(input)

    private suspend fun signIn(input: AuthByEmailInput): SignByEmailResult {
        val result = firebaseAuth.signInWithEmailAndPassword(
            email = input.email,
            password = input.password
        )
        return when (result) {
            is SignInUserWithEmailAndPasswordResult.Error.NotExists -> createAnAccount(input)
            is SignInUserWithEmailAndPasswordResult.Error.Other -> SignByEmailResult.Error.Failed
            is SignInUserWithEmailAndPasswordResult.Error.WrongPassword ->
                SignByEmailResult.Error.WrongPassword

            is SignInUserWithEmailAndPasswordResult.Success -> {
                // handle result.authResult
                SignByEmailResult.Success(input.email)
            }
        }
    }

    private suspend fun createAnAccount(input: AuthByEmailInput): SignByEmailResult {
        val result = firebaseAuth.createUserWithEmailAndPassword(
            email = input.email,
            password = input.password
        )
        return when (result) {
            is CreateUserWithEmailAndPasswordResult.Error.AlreadyExists ->
                SignByEmailResult.Error.Failed

            is CreateUserWithEmailAndPasswordResult.Error.InvalidEmail ->
                SignByEmailResult.Error.InvalidEmail

            is CreateUserWithEmailAndPasswordResult.Error.Other ->
                SignByEmailResult.Error.Failed

            is CreateUserWithEmailAndPasswordResult.Success -> {
                // handle result.authResult
                SignByEmailResult.Success(input.email)
            }
        }
    }
}