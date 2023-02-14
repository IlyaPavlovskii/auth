package io.github.ilyapavlovskii.kmm.common.domain.utils

import platform.CoreFoundation.CFAbsoluteTimeGetCurrent
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual fun Duration.Companion.elapsedRealtime(): Duration =
    CFAbsoluteTimeGetCurrent().toDuration(DurationUnit.SECONDS)