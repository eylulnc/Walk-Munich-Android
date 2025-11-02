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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eylulnc.walkmunich.core.data.model.Category

@Composable
fun CategoryChips(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    val categories = remember { listOf<Category?>(null) + Category.values() }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            Button(
                onClick = { onCategorySelected(category) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                val text = when (category) {
                    null -> "For You"
                    else -> category.name.lowercase().replaceFirstChar { it.titlecase() }
                }
                Text(text = text, fontSize = 14.sp)
            }
        }
    }
}