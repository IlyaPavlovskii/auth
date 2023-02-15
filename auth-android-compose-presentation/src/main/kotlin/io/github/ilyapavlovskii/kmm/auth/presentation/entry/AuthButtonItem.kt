package io.github.ilyapavlovskii.kmm.auth.presentation.entry

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class AuthButtonItem(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val action: AuthActionType,
)