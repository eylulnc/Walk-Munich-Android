package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = TypographySizes.subtitle,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            horizontal = Spacing.Medium,
            vertical = Spacing.Small
        )
    )
}