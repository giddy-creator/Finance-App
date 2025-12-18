package com.example.financeapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

// --- SAME COLORS AS HOME ---
private val BackgroundGray = Color(0xFFF7F8FC)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF7A7A7A)

// --- SAMPLE DATA (daily-renew ready) ---
private data class ReportCategory(
    val name: String,
    val amount: Double,
    val color: Color
)

@Composable
fun ReportsScreen(modifier: Modifier = Modifier) {
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }

    val categories = listOf(
        ReportCategory("Food & Dining", 540.0, Color(0xFFFFC857)),
        ReportCategory("Transport", 220.0, Color(0xFF3A86FF)),
        ReportCategory("Subscriptions", 120.0, Color(0xFF8338EC)),
        ReportCategory("Lifestyle", 310.0, Color(0xFF06D6A0))
    )

    val totalSpent = categories.sumOf { it.amount }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()), // ✅ scroll added
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- PIE CHART FIRST ---
            PieChart(
                data = categories,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            // --- REPORT CARD (FROM HOME IDEA) ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Spending Report",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 18.sp
                    )

                    categories.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(it.name, color = TextPrimary)
                            Text(
                                formatter.format(it.amount),
                                color = it.color,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // --- END OF DAY SUMMARY ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Daily Summary",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        "Total spent today",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        formatter.format(totalSpent),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

// --- PIE CHART ---
@Composable
private fun PieChart(
    data: List<ReportCategory>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.amount }

    Canvas(modifier = modifier) {
        var startAngle = -90f

        data.forEach { item ->
            val sweepAngle = (item.amount / total * 360f).toFloat()

            drawArc(
                color = item.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            startAngle += sweepAngle
        }
    }
}
