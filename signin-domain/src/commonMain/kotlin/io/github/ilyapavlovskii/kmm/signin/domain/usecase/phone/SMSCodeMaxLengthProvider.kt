package io.github.ilyapavlovskii.kmm.signin.domain.usecase.phone

fun interface SMSCodeMaxLengthProvider {
    fun getSMSMaxCodeLength(): UInt
}
