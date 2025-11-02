package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes


@Composable
fun ExpandableSection(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = Spacing.Small, vertical = Spacing.Medium)) {
        Divider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = Spacing.Small)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = TypographySizes.subtitle,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (expanded) {
            Text(
                text = content,
                fontSize = TypographySizes.medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = Spacing.Small)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = Spacing.Small),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
