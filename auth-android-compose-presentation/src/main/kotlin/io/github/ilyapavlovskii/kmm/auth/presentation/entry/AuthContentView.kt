package io.github.ilyapavlovskii.kmm.auth.presentation.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
internal fun AuthContentView(
    viewState: AuthScreenViewState,
    onButtonCLick: (AuthActionType) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.align(alignment = Alignment.Center)) {
            items(viewState.items.toList()) { item ->
                AuthButton(
                    item = item,
                    onButtonCLick = onButtonCLick,
                )
            }
        }
    }
}

@Composable
private fun AuthButton(
    item: AuthButtonItem,
    onButtonCLick: (AuthActionType) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(36.dp))
            .background(color = MaterialTheme.colorScheme.onSurface)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.primary,
                ),
                onClick = { onButtonCLick(item.action) },
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageVector = ImageVector.vectorResource(id = item.iconRes),
            tint = MaterialTheme.colorScheme.background,
            contentDescription = null,
        )
        Text(
            text = stringResource(id = item.textRes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.background,
        )
    }
}