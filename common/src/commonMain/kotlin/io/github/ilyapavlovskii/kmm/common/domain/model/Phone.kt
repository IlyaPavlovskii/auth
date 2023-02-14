package io.github.ilyapavlovskii.kmm.common.domain.model

sealed class Phone {

    fun toE164(): E164 = when (this) {
        is E164 -> this
        is MSISDN -> this.convertToE164()
    }

    fun toMSISDN(): MSISDN = when (this) {
        is E164 -> this.convertToMSISDN()
        is MSISDN -> this
    }

    /**
     * Phone number in [MSISDN](https://en.wikipedia.org/wiki/MSISDN) format.
     * The MSISDN format will get rid of the + sign and of any 0 before the mobile number
     * e.g.: 375299379992
     * */
    data class MSISDN(val value: String) : Phone() {
        init {
            check(MSISDN_PATTERN.matches(value)) {
                "Incorrect MSISDN format. Must contains only digits with maximum length 16"
            }
        }

        internal fun convertToE164(): E164 = E164("+$value")

        companion object {
            private val MSISDN_PATTERN = "(\\d{1,16})".toRegex()
        }
    }

    /**
     * Phone number in [E.164](https://en.wikipedia.org/wiki/E.164) format.
     *  Plan-conforming numbers are limited to a maximum of 15 digits, excluding the international call prefix.
     *  e.g.: +375299379992
     * */
    data class E164(val value: String) : Phone() {
        init {
            check(E164_PATTERN.matches(value)) {
                "Incorrect E164 format. Must contains sign plus at the beginning and only digits" +
                        " with maximum length 16"
            }
        }

        internal fun convertToMSISDN(): MSISDN = MSISDN(this.value.removePrefix("+"))

        companion object {
            private val E164_PATTERN = "([+]\\d{1,16})".toRegex()

        }
    }
}