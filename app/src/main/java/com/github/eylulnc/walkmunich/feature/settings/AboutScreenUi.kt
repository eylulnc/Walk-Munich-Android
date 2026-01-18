package com.github.eylulnc.walkmunich.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsCard
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsNavigationItem
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing

@Composable
fun AboutScreenUi(
    onBackClick: () -> Unit,
    onOpenAppDescription: () -> Unit,
    onOpenDisclaimer: () -> Unit,
    onOpenAttribution: () -> Unit,
    onOpenImpressum: () -> Unit
) {
    WMTopAppBarScreen(
        title = stringResource(R.string.settings_about),
        onBack = onBackClick
    ) { modifier ->

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(Spacing.Medium)
        ) {

            SettingsCard {

                SettingsNavigationItem(
                    icon = Icons.Outlined.Info,
                    title = stringResource(R.string.about_app_description_title),
                    hasChevron = true,
                    onClick = onOpenAppDescription
                )

                HorizontalDivider()

                SettingsNavigationItem(
                    icon = Icons.Outlined.Info,
                    title = stringResource(R.string.about_disclaimer_title),
                    hasChevron = true,
                    onClick = onOpenDisclaimer
                )

                HorizontalDivider()

                SettingsNavigationItem(
                    icon = Icons.Outlined.Info,
                    title = stringResource(R.string.about_attribution_title),
                    hasChevron = true,
                    onClick = onOpenAttribution
                )

                HorizontalDivider()

                SettingsNavigationItem(
                    icon = Icons.Outlined.Info,
                    title = stringResource(R.string.about_impressum_title),
                    hasChevron = true,
                    onClick = onOpenImpressum
                )
            }

            Spacer(Modifier.height(Spacing.Large))

            Text(
                text = "VERSION 2.4.1 (104)",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(Spacing.Small))

            Text(
                text = "Made with passion for minimalist design enthusiasts.\n© 2024 All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
