package io.github.ilyapavlovskii.kmm.common.domain.usecase

import io.github.ilyapavlovskii.kmm.common.domain.utils.elapsedRealtime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

internal class DefaultCountDownTimerUseCase : CountDownTimerUseCase {
    override fun execute(input: CountDownTimerInput): Flow<Duration> = flow {
        val endRealtime = input.duration + Duration.elapsedRealtime()
        emit(input.duration)
        while (true) {
            delay(input.interval.inWholeMilliseconds)
            val leftDuration = endRealtime - Duration.elapsedRealtime()
            if (leftDuration.isPositive()) emit(leftDuration) else break
        }
        emit(Duration.ZERO)
    }
}

