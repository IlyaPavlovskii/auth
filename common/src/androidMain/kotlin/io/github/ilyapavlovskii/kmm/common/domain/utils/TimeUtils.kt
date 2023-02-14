package io.github.ilyapavlovskii.kmm.common.domain.utils

import android.os.SystemClock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual fun Duration.Companion.elapsedRealtime(): Duration =
    SystemClock.elapsedRealtime().toDuration(DurationUnit.MILLISECONDS)
