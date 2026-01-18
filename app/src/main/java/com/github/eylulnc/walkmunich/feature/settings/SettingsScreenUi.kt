package com.github.eylulnc.walkmunich.feature.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsNavigationItem
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsSectionHeader
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenUi(
    viewModel: SettingsViewModel = koinViewModel(),
    onOpenAppDescription: () -> Unit,
    onOpenDisclaimer: () -> Unit,
    onOpenAttribution: () -> Unit,
    onOpenImpressum: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    WMTopAppBarScreen(
        title = stringResource(R.string.settings_title),
        content = { modifier ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = Spacing.Large)
            ) {

                SettingsSectionHeader(stringResource(R.string.settings_preferences))

                DarkModeSwitch(
                    isDarkMode = isDarkTheme,
                    onDarkThemeToggle = viewModel::setDarkTheme
                )

                Spacer(Modifier.height(Spacing.Large))

                SettingsSectionHeader(stringResource(R.string.settings_about))

                SettingsNavigationItem(
                    title = stringResource(R.string.about_app_description_title),
                    onClick = onOpenAppDescription
                )

                SettingsNavigationItem(
                    title = stringResource(R.string.about_disclaimer_title),
                    onClick = onOpenDisclaimer
                )

                SettingsNavigationItem(
                    title = stringResource(R.string.about_attribution_title),
                    onClick = onOpenAttribution
                )

                SettingsNavigationItem(
                    title = stringResource(R.string.about_impressum_title),
                    onClick = onOpenImpressum
                )
            }
        }
    )
}

@Composable
fun DarkModeSwitch(
    isDarkMode: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.dark_mode))
            Switch(
                checked = isDarkMode,
                onCheckedChange = onDarkThemeToggle
            )
        }
    }
}