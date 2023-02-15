package io.github.ilyapavlovskii.kmm.auth.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ConfirmationCode(val value: String) {
    init {
        check(PATTERN.matches(value)) {
            "Incorrect confirmation code"
        }
    }
    companion object {
        private val PATTERN = "\\d+".toRegex()

        fun createOrNull(value: String): ConfirmationCode? {
            return if(!PATTERN.matches(value)) null else ConfirmationCode(value)
        }
    }
}