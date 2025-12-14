package com.example.financeapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.FinanceAppTheme
import java.text.NumberFormat
import java.util.Locale

// Colors
private val BackgroundGray = Color(0xFFF7F8FC)
private val AccentOrange = Color(0xFFFD5930)
private val AccentTeal = Color(0xFF01C9A2)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF7A7A7A)

// Data classes
data class TransactionUi(
    val id: String,
    val merchant: String,
    val category: String,
    val amount: Double,
    val isExpense: Boolean,
    val date: String // <-- Changed from LocalDate to String
)

data class QuickActionUi(
    val label: String,
    val icon: ImageVector
)

data class SpendingCategoryUi(
    val name: String,
    val percent: Float,
    val amount: Double,
    val color: Color
)

data class HomeUiState(
    val userName: String,
    val notifications: Int,
    val balance: Double,
    val income: Double,
    val expenses: Double,
    val budgetUsedPercent: Float,
    val quickActions: List<QuickActionUi>,
    val categories: List<SpendingCategoryUi>,
    val transactions: List<TransactionUi>
) {
    companion object {
        fun preview() = HomeUiState(
            userName = "Gideon",
            notifications = 3,
            balance = 8425.65,
            income = 6130.0,
            expenses = 2480.0,
            budgetUsedPercent = 0.63f,
            quickActions = listOf(
                QuickActionUi("Send", Icons.Rounded.Send),
                QuickActionUi("Receive", Icons.Rounded.Download),
                QuickActionUi("Scanner", Icons.Rounded.QrCodeScanner),
                QuickActionUi("Bills", Icons.Rounded.ReceiptLong)
            ),
            categories = listOf(
                SpendingCategoryUi("Food & Dining", 0.72f, 540.0, Color(0xFFFFC857)),
                SpendingCategoryUi("Transport", 0.45f, 220.0, Color(0xFF3A86FF)),
                SpendingCategoryUi("Subscriptions", 0.30f, 120.0, Color(0xFF8338EC)),
                SpendingCategoryUi("Lifestyle", 0.58f, 310.0, Color(0xFF06D6A0))
            ),
            transactions = listOf(
                TransactionUi("1", "Hotel", "Food & Drink", 12.40, true, "Nov 17"),
                TransactionUi("2", "Movie", "Subscription", 15.99, true, "Nov 16"),
                TransactionUi("3", "Salary", "Income", 3100.00, false, "Nov 15"),
                TransactionUi("4", "Uber", "Transport", 8.20, true, "Nov 14")
            )
        )
    }
}

// --- Composables ---
@Composable
fun Home(
    modifier: Modifier = Modifier,
    state: HomeUiState = HomeUiState.preview()
) {
    val currencyFormatter = rememberCurrencyFormatter()
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundGray),
        color = BackgroundGray
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { HomeHeader(state.userName, state.notifications) }
            item { BalanceSummaryCard(state.balance, state.income, state.expenses, currencyFormatter) }
            item { QuickActionsRow(state.quickActions) }
            item { SpendingProgressCard(state.budgetUsedPercent, state.income, state.expenses, currencyFormatter) }
            item { SpendingBreakdown(state.categories, currencyFormatter) }
            item { TransactionsSection(state.transactions, currencyFormatter) }
        }
    }
}

// --- Header ---
@Composable
private fun HomeHeader(name: String, notifications: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Welcome back,", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text(name, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
        }
        Box {
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color.White, shape = CircleShape)
            ) {
                Icon(Icons.Rounded.Bolt, contentDescription = "Notifications", tint = AccentOrange)
            }
            if (notifications > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(18.dp)
                        .background(AccentOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(notifications.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Balance Card ---
@Composable
private fun BalanceSummaryCard(balance: Double, income: Double, expenses: Double, formatter: NumberFormat) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B))),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Total Balance", color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodyMedium)
                        Text(formatter.format(balance), color = Color.White, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.1f)) {
                        Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.padding(12.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    BalanceStat(
                        label = "Income",
                        amount = formatter.format(income),
                        icon = Icons.Rounded.ArrowDownward,
                        iconTint = AccentTeal,
                        modifier = Modifier.weight(1f)
                    )
                    BalanceStat(
                        label = "Expenses",
                        amount = formatter.format(expenses),
                        icon = Icons.Rounded.ArrowUpward,
                        iconTint = AccentOrange,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceStat(
    label: String,
    amount: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.padding(8.dp))
            }
            Column {
                Text(text = label, color = Color(0xFFCBD5F5), fontSize = 12.sp)
                Text(text = amount, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// --- Quick Actions Row ---
@Composable
private fun QuickActionsRow(actions: List<QuickActionUi>) {
    Column {
        Text("Quick actions", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(actions) { action -> QuickActionChip(action) }
        }
    }
}

@Composable
private fun QuickActionChip(action: QuickActionUi) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.width(90.dp).padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = AccentOrange.copy(alpha = 0.12f)
            ) {
                Icon(action.icon, contentDescription = action.label, tint = AccentOrange, modifier = Modifier.padding(12.dp))
            }
            Text(action.label, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
        }
    }
}

// --- Spending Progress Card ---
@Composable
private fun SpendingProgressCard(percent: Float, income: Double, expenses: Double, formatter: NumberFormat) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Monthly summary", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                SummaryTile("Income", formatter.format(income), AccentTeal)
                SummaryTile("Expenses", formatter.format(expenses), AccentOrange)
            }
            Text("Budget used ${(percent * 100).toInt()}%", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(progress = percent, color = AccentOrange, trackColor = Color(0xFFEFF0F6), modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SummaryTile(label: String, value: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Text(value, color = color, fontWeight = FontWeight.SemiBold)
    }
}

// --- Spending Breakdown ---
@Composable
private fun SpendingBreakdown(categories: List<SpendingCategoryUi>, formatter: NumberFormat) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Spending breakdown", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            categories.forEach { category ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(category.name, color = TextPrimary)
                        Text(formatter.format(category.amount), color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                    LinearProgressIndicator(progress = category.percent, color = category.color, trackColor = Color(0xFFF0F1F7), modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

// --- Transactions Section ---
@Composable
private fun TransactionsSection(transactions: List<TransactionUi>, formatter: NumberFormat) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Recent transactions", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text("See all", color = AccentOrange, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            }
            transactions.forEach { TransactionRow(it, formatter) }
        }
    }
}

@Composable
private fun TransactionRow(transaction: TransactionUi, formatter: NumberFormat) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = AccentOrange.copy(alpha = 0.12f)) {
                Canvas(modifier = Modifier.padding(16.dp)) { drawCircle(color = AccentOrange.copy(alpha = 0.25f)) }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(transaction.merchant, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text("${transaction.category} • ${transaction.date}", color = TextSecondary, fontSize = 12.sp)
            }
        }
        Text(
            text = (if (transaction.isExpense) "-" else "+") + formatter.format(transaction.amount).removePrefix("-"),
            color = if (transaction.isExpense) TextPrimary else AccentTeal,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// --- Currency Formatter ---
@Composable
private fun rememberCurrencyFormatter(): NumberFormat {
    val locale = Locale.getDefault()
    return remember(locale) { NumberFormat.getCurrencyInstance(locale) }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HomePreview() {
    FinanceAppTheme { Home() }

}