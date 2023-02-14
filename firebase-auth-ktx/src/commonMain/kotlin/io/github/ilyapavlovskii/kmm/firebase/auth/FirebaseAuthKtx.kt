package io.github.ilyapavlovskii.kmm.firebase.auth

import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.ConfirmCodeAndPasswordResetResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.CreateUserWithEmailAndPasswordResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.LogoutResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.SendPasswordResetEmailResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.SignInUserWithEmailAndPasswordResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.SignInWithEmailLinkResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.auth.VerifyPasswordResetCodeResult
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthActionCodeException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import io.github.ilyapavlovskii.kmm.common.domain.model.Password
import io.github.ilyapavlovskii.kmm.common.domain.model.URL

suspend fun FirebaseAuth.createUserWithEmailAndPassword(
    email: Email,
    password: Password,
): CreateUserWithEmailAndPasswordResult = try {
    val authResult = this.createUserWithEmailAndPassword(
        email = email.value,
        password = password.value
    )
    CreateUserWithEmailAndPasswordResult.Success(authResult)
} catch (fauce: FirebaseAuthUserCollisionException) {
    CreateUserWithEmailAndPasswordResult.Error.AlreadyExists(fauce)
} catch (faice: FirebaseAuthInvalidCredentialsException) {
    CreateUserWithEmailAndPasswordResult.Error.InvalidEmail(faice)
} catch (ex: Throwable) {
    CreateUserWithEmailAndPasswordResult.Error.Other(ex)
}

suspend fun FirebaseAuth.signInWithEmailAndPassword(
    email: Email,
    password: Password,
): SignInUserWithEmailAndPasswordResult = try {
    val authResult = this.signInWithEmailAndPassword(
        email = email.value,
        password = password.value,
    )
    SignInUserWithEmailAndPasswordResult.Success(authResult)
} catch (faiue: FirebaseAuthInvalidUserException) {
    SignInUserWithEmailAndPasswordResult.Error.NotExists(faiue)
} catch (faice: FirebaseAuthInvalidCredentialsException) {
    SignInUserWithEmailAndPasswordResult.Error.WrongPassword(faice)
} catch (ex: Throwable) {
    SignInUserWithEmailAndPasswordResult.Error.Other(ex)
}

suspend fun FirebaseAuth.sendPasswordResetEmail(
    email: Email,
    actionCodeSettings: ActionCodeSettings? = null,
): SendPasswordResetEmailResult = try {
    this.sendPasswordResetEmail(
        email = email.value,
        actionCodeSettings = actionCodeSettings,
    )
    SendPasswordResetEmailResult.Success
} catch (faiue: FirebaseAuthInvalidUserException) {
    SendPasswordResetEmailResult.Error.InvalidUser(faiue)
} catch (ex: Throwable) {
    SendPasswordResetEmailResult.Error.Other(ex)
}

suspend fun FirebaseAuth.verifyPasswordResetCode(
    email: Email,
    code: String,
): VerifyPasswordResetCodeResult = try {
    if (this.verifyPasswordResetCode(code) == email.value) {
        VerifyPasswordResetCodeResult.Success
    } else {
        VerifyPasswordResetCodeResult.Error.InvalidCode
    }
} catch (ex: Throwable) {
    VerifyPasswordResetCodeResult.Error.Other(ex)
}

suspend fun FirebaseAuth.signInWithEmailLink(
    email: Email,
    confirmationLink: URL,
): SignInWithEmailLinkResult = try {
    val result = this.signInWithEmailLink(
        email = email.value,
        link = confirmationLink.value,
    )
    SignInWithEmailLinkResult.Success(result)
} catch (faiue: FirebaseAuthInvalidUserException) {
    SignInWithEmailLinkResult.Error.InvalidUser(faiue)
} catch (faice: FirebaseAuthInvalidCredentialsException) {
    SignInWithEmailLinkResult.Error.InvalidCredentials(faice)
} catch (ex: Throwable) {
    SignInWithEmailLinkResult.Error.Other(ex)
}

suspend fun FirebaseAuth.confirmCodeAndPasswordReset(
    code: String,
    newPassword: Password,
): ConfirmCodeAndPasswordResetResult = try {
    this.confirmPasswordReset(code = code, newPassword = newPassword.value)
    ConfirmCodeAndPasswordResetResult.Success
} catch (iae: IllegalArgumentException) {
    ConfirmCodeAndPasswordResetResult.Error.IllegalCodeOrPassword(iae)
} catch (faace: FirebaseAuthActionCodeException) {
    ConfirmCodeAndPasswordResetResult.Error.CodeExpired(faace)
} catch (faiue: FirebaseAuthInvalidUserException) {
    ConfirmCodeAndPasswordResetResult.Error.InvalidCode(faiue)
} catch (ex: Throwable) {
    ConfirmCodeAndPasswordResetResult.Error.Other(ex)
}

suspend fun FirebaseAuth.logout(): LogoutResult = try {
    this.signOut()
    LogoutResult.Success
} catch (ex: Throwable) {
    LogoutResult.Error(ex)
}
