package io.github.ilyapavlovskii.kmm.common.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface CountDownTimerUseCase {
    fun execute(input: CountDownTimerInput): Flow<Duration>
}

data class CountDownTimerInput(
    val duration: Duration,
    val interval: Duration,
) {
    init {
        check(duration.isPositive()) { "Duration must be greater than Duration.ZERO" }
        check(interval.isPositive()) { "Interval must be greater than Duration.ZERO" }
        check(interval.isFinite()) { "Interval must be finite" }
        check(interval <= duration) { "Interval can't be greater than duration" }
    }
}

fun CountDownTimerUseCase.execute(
    duration: Duration,
    interval: Duration,
): Flow<Duration> = this.execute(
    CountDownTimerInput(
        duration = duration,
        interval = interval,
    )
)
