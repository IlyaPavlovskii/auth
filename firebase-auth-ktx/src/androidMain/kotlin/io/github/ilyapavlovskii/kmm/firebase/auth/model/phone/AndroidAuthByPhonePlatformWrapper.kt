package io.github.ilyapavlovskii.kmm.firebase.auth.model.phone

import android.app.Activity
import android.util.Log
import dev.gitlive.firebase.auth.PhoneVerificationProvider
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

data class AndroidAuthByPhonePlatformWrapper(
    val activity: Activity,
    val verificationCodeTimout: Duration,
    val onCodeSent: () -> Unit,
    val smsCodeEmitter: Flow<SMSCode>,
) : AuthByPhonePlatformWrapper

fun AndroidAuthByPhonePlatformWrapper.toPhoneVerificationProvider(): PhoneVerificationProvider {
    return object : PhoneVerificationProvider {
        override val activity: Activity = this@toPhoneVerificationProvider.activity
        override val timeout: Long =
            this@toPhoneVerificationProvider.verificationCodeTimout.inWholeSeconds
        override val unit: TimeUnit = TimeUnit.SECONDS
        private val TAG = "AndroidAuthByPhonePlatformWrapper"

        override fun codeSent(triggerResend: (Unit) -> Unit) {
            this@toPhoneVerificationProvider.onCodeSent()
        }

        override suspend fun getVerificationCode(): String {
            Log.d(TAG, "getVerificationCode")
            val value = smsCodeEmitter.first().value
            Log.d(TAG, "getVerificationCode: $value")
            return value
        }
    }
}