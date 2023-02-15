package io.github.ilyapavlovskii.kmm.auth.domain.mvi.email

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux.Effect
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux.Message
import com.chrynan.validator.EmailValidator
import com.chrynan.validator.ValidationResult
import com.chrynan.validator.Validator
import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import io.github.ilyapavlovskii.kmm.common.domain.model.Password
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.SignByEmailResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.AuthByEmailInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.AuthByEmailUseCase
import net.humans.kmm.mvi.CoroutineEffectHandler

internal class AuthorizationEmailEffectHandler(
    private val resetPasswordEffectHandler: CoroutineEffectHandler<Effect.ResetPassword, Message>,
    private val sendAuthRequestEffectHandler: CoroutineEffectHandler<Effect.SendAuthorizationRequest, Message>
) : CoroutineEffectHandler<Effect, Message> {
    override suspend fun handle(eff: Effect): Message? = when (eff) {
        is Effect.ResetPassword -> resetPasswordEffectHandler.handle(eff)
        is Effect.SendAuthorizationRequest -> sendAuthRequestEffectHandler.handle(eff)
    }
}

internal class SendAuthRequestEffectHandler(
    private val authByEmailUseCase: AuthByEmailUseCase,
    private val emailValidator: Validator<String?, String>,
    private val passwordValidator: Validator<String?, String>,
) : CoroutineEffectHandler<Effect.SendAuthorizationRequest, Message> {
    override suspend fun handle(eff: Effect.SendAuthorizationRequest): Message {
        val email = when (val result = emailValidator.validate(input = eff.email?.lowercase())) {
            is ValidationResult.Invalid -> return Message.InvalidEmailHandled(email = eff.email)
            is ValidationResult.Valid -> Email(result.value)
        }
        val password = when (val result = passwordValidator.validate(input = eff.password)) {
            is ValidationResult.Invalid -> return Message.InvalidPasswordHandled(password = eff.password)
            is ValidationResult.Valid -> Password(result.value)
        }
        val input = AuthByEmailInput(
            email = email,
            password = password
        )
        return when (authByEmailUseCase.execute(input)) {
            SignByEmailResult.Error.Failed -> Message.AuthorizationFailed
            SignByEmailResult.Error.InvalidEmail -> Message.InvalidEmailHandled(email = eff.email)
            SignByEmailResult.Error.WrongPassword -> Message.WrongPasswordHandled
            is SignByEmailResult.Success -> Message.AuthorizationSuccess(email = email)
        }
    }
}

internal class ResetPasswordEffectHandler(
    private val emailValidator: Validator<String?, String> = EmailValidator(),
    private val resetPasswordByEmailUseCase: ResetPasswordByEmailUseCase,
) : CoroutineEffectHandler<Effect.ResetPassword, Message> {

    override suspend fun handle(eff: Effect.ResetPassword): Message {
        val email = when (val result = emailValidator.validate(input = eff.email?.lowercase())) {
            is ValidationResult.Invalid -> return Message.InvalidEmailHandled(email = eff.email)
            is ValidationResult.Valid -> Email(result.value)
        }
        val input = ResetPasswordByEmailInput(email = email)
        return when (resetPasswordByEmailUseCase.execute(input)) {
            ResetPasswordByEmailResult.Error.Failed -> Message.ResetPasswordFailed
            ResetPasswordByEmailResult.Error.InvalidUser -> Message.UserNotRegistered
            ResetPasswordByEmailResult.Success -> Message.ResetPasswordSuccess(email)
        }
    }
}