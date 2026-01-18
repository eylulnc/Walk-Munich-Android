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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsCard
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsNavigationItem
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.util.getAppVersion

@Composable
fun AboutScreenUi(
    onBackClick: () -> Unit,
    onOpenAppDescription: () -> Unit,
    onOpenDisclaimer: () -> Unit,
    onOpenAttribution: () -> Unit,
    onOpenImpressum: () -> Unit
) {
    val context = LocalContext.current
    val appVersion = remember { context.getAppVersion() }

    WMTopAppBarScreen(
        title = stringResource(R.string.settings_about_app),
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
                text = stringResource(R.string.version_info, appVersion.versionName, appVersion.versionCode),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(Spacing.Small))

            Text(
                text = stringResource(R.string.about_footer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
