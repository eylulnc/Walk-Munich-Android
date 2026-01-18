package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing

@Composable
fun SettingsTextDetailScreen(
    title: String,
    content: String,
    onBackClick: () -> Unit
) {
    WMTopAppBarScreen(
        title = title,
        onBack = onBackClick,
        content = { modifier ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.Medium)
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
