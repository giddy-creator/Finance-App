package com.example.financeapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import java.text.NumberFormat
import java.util.Locale

// --- COLORS (SAME AS HOME) ---
private val BackgroundGray = Color(0xFFF7F8FC)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF7A7A7A)
private val AccentOrange = Color(0xFFFD5930)
private val AccentTeal = Color(0xFF01C9A2)

// --- SAMPLE DATA ---
private data class BudgetCategory(
    val name: String,
    var allocated: Double,
    var used: Double,
    val color: Color
)

@Composable
fun BudgetsScreen(modifier: Modifier = Modifier) {
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }
    var categories by remember {
        mutableStateOf(
            listOf(
                BudgetCategory("Food & Dining", 1000.0, 650.0, Color(0xFFFFC857)),
                BudgetCategory("Transport", 500.0, 380.0, Color(0xFF3A86FF)),
                BudgetCategory("Subscriptions", 200.0, 120.0, Color(0xFF8338EC)),
                BudgetCategory("Lifestyle", 700.0, 310.0, Color(0xFF06D6A0))
            )
        )
    }

    val totalAllocated = categories.sumOf { it.allocated }
    val totalUsed = categories.sumOf { it.used }

    Surface(modifier = modifier.fillMaxSize(), color = BackgroundGray) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // --- HEADER ---
            Text(
                text = "Monthly Budget",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextPrimary
            )

            // --- TOTAL BUDGET CARD ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Total Budget", color = TextSecondary, fontSize = 14.sp)
                    Text(
                        formatter.format(totalAllocated),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text("Used: ${formatter.format(totalUsed)}", color = AccentOrange, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(
                        progress = (totalUsed / totalAllocated).toFloat(),
                        color = AccentOrange,
                        trackColor = Color(0xFFEFF0F6),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // --- CATEGORY BUDGETS ---
            categories.forEachIndexed { index, category ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(category.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text(
                                "Used: ${formatter.format(category.used)}",
                                color = category.color,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        LinearProgressIndicator(
                            progress = (category.used / category.allocated).toFloat(),
                            color = category.color,
                            trackColor = Color(0xFFF0F1F7),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // --- Editable budget input ---
                        var allocatedInput by remember { mutableStateOf(category.allocated.toString()) }
                        OutlinedTextField(
                            value = allocatedInput,
                            onValueChange = { newValue ->
                                allocatedInput = newValue
                                val parsed = newValue.toDoubleOrNull()
                                if (parsed != null) {
                                    categories = categories.toMutableList().also { it[index] = it[index].copy(allocated = parsed) }
                                }
                            },
                            label = { Text("Allocated") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // --- DAILY SUMMARY ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Daily Summary", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        "Total spent today",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        formatter.format(totalUsed),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
