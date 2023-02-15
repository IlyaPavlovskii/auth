package io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone

fun interface SMSCodeMaxLengthProvider {
    fun getSMSMaxCodeLength(): UInt
}
