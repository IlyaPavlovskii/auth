package io.github.ilyapavlovskii.kmm.common.domain.utils

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

expect fun Duration.Companion.elapsedRealtime(): Duration

/**
 * Rounds up duration according to DurationUnit
 * */
fun Duration.ceil(durationUnit: DurationUnit): Duration =
    kotlin.math.ceil(this.toDouble(durationUnit)).toDuration(durationUnit)