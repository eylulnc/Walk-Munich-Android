package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(
            start = Spacing.Medium,
            end = Spacing.Medium,
            top = Spacing.Large,
            bottom = Spacing.ExtraSmall
        )
    )
}