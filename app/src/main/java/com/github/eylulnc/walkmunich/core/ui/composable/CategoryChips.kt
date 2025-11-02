package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.ui.theme.ChipGray
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun CategoryChips(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    val categories = remember { listOf<Category?>(null) + Category.values() }

    LazyRow(
        contentPadding = PaddingValues(horizontal = Spacing.Small),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            Button(
                onClick = { onCategorySelected(category) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else ChipGray,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(
                    horizontal = Spacing.Medium,
                    vertical = Spacing.ExtraSmall
                )
            ) {
                val text = when (category) {
                    null -> "All"
                    else -> category.name.lowercase().replaceFirstChar { it.titlecase() }
                }
                Text(text = text, fontSize = TypographySizes.body,)
            }
        }
    }
}
