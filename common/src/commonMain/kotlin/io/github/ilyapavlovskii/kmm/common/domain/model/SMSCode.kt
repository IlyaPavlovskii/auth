package io.github.ilyapavlovskii.kmm.common.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class SMSCode(val value: String) {

    init {
        check(SMS_PATTERN.matches(value)) {
            "Incorrect SMS code. The code must contain only digits"
        }
    }

    companion object {
        private val SMS_PATTERN = "\\d+".toRegex()
    }
}