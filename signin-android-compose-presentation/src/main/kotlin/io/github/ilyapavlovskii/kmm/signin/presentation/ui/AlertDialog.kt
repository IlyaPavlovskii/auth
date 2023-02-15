package io.github.ilyapavlovskii.kmm.signin.presentation.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.kmm.signin.presentation.R

@Composable
internal fun AlertDialog(
    @DrawableRes iconRes: Int = R.drawable.baseline_info_24,
    @StringRes titleRes: Int = R.string.dialog__info_title,
    @StringRes dismissTextRes: Int = R.string.dialog__info_cancel_button,
    @StringRes messageRes: Int,
    arguments: List<Any>,
    onCancel: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            onCancel()
                        },
                    )
                    .padding(all = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = stringResource(id = dismissTextRes),
            )
        },
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(id = titleRes),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        text = {
            Text(
                text = stringResource(id = messageRes, formatArgs = arguments.toTypedArray()),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        shape = shape,
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        iconContentColor = MaterialTheme.colorScheme.primary,
    )
}
