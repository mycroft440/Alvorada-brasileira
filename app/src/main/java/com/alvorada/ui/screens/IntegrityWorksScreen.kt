package com.alvorada.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvorada.core.model.GovernmentWork
import com.alvorada.core.model.RiskLevel
import com.alvorada.core.model.WorkStatus

private enum class IntegrityWorkFilter { ALL, POSSIBLE_FRAUD, ONGOING, COMPLETED }

private data class WorkLocation(
    val state: String,
    val city: String
)

@Composable
fun IntegrityWorksScreen(
    contentPadding: PaddingValues,
    works: List<GovernmentWork>,
    onProfileClick: () -> Unit,
    onWorkClick: (GovernmentWork) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(IntegrityWorkFilter.ALL) }

    val filtered = works.filter { work ->
        val filterMatches = when (filter) {
            IntegrityWorkFilter.ALL -> true
            IntegrityWorkFilter.POSSIBLE_FRAUD -> work.riskLevel == RiskLevel.HIGH
            IntegrityWorkFilter.ONGOING -> work.status == WorkStatus.ONGOING
            IntegrityWorkFilter.COMPLETED -> work.status == WorkStatus.COMPLETED
        }
        filterMatches && (
            query.isBlank() ||
                work.title.contains(query, true) ||
                work.agency.contains(query, true) ||
                work.location.contains(query, true) ||
                work.contractor.contains(query, true)
            )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            IntegrityHeader(onProfileClick = onProfileClick)
            IntegrityNotice()
            IntegritySummary(works = works)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar obra, cidade, órgão ou empresa...") },
                leadingIcon = { Text("🔎") }
            )
            IntegrityFilterSelector(filter = filter, onFilterChange = { filter = it })
            Text(
                text = "OBRAS PRIORIZADAS PARA FISCALIZAÇÃO",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (filtered.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhuma obra encontrada.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(filtered, key = { it.id }) { work ->
                IntegrityWorkCard(work = work, onClick = { onWorkClick(work) })
            }
        }
    }
}

@Composable
private fun IntegrityHeader(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Obras do Gov", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "Obras com maior risco aparecem primeiro",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(12.dp))
        Surface(
            modifier = Modifier
                .size(44.dp)
                .clickable(onClick = onProfileClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("JG", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun IntegrityNotice() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        color = Color(0xFFFFF8DF),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = "⚠️ 'Possível Fraude!' significa que existem sinais automáticos que justificam auditoria. Não é confirmação de crime.",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF765A12)
        )
    }
}

@Composable
private fun IntegritySummary(works: List<GovernmentWork>) {
    val possibleFraud = works.count { it.riskLevel == RiskLevel.HIGH }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${works.size} obras analisadas", fontWeight = FontWeight.SemiBold)
            Text("$possibleFraud com risco alto", color = Color(0xFFB43B3B), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun IntegrityFilterSelector(
    filter: IntegrityWorkFilter,
    onFilterChange: (IntegrityWorkFilter) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(
                selected = filter == IntegrityWorkFilter.ALL,
                onClick = { onFilterChange(IntegrityWorkFilter.ALL) },
                label = { Text("Todas") }
            )
            FilterChip(
                selected = filter == IntegrityWorkFilter.POSSIBLE_FRAUD,
                onClick = { onFilterChange(IntegrityWorkFilter.POSSIBLE_FRAUD) },
                label = { Text("⚠ Possível fraude") }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(
                selected = filter == IntegrityWorkFilter.ONGOING,
                onClick = { onFilterChange(IntegrityWorkFilter.ONGOING) },
                label = { Text("Em andamento") }
            )
            FilterChip(
                selected = filter == IntegrityWorkFilter.COMPLETED,
                onClick = { onFilterChange(IntegrityWorkFilter.COMPLETED) },
                label = { Text("Concluídas") }
            )
        }
    }
}

@Composable
private fun IntegrityWorkCard(work: GovernmentWork, onClick: () -> Unit) {
    val location = parseWorkLocation(work.location)
    val situation = situationLabel(work.riskLevel)
    val situationColor = when (work.riskLevel) {
        RiskLevel.HIGH -> Color(0xFFB43B3B)
        RiskLevel.MODERATE -> Color(0xFFA66B12)
        RiskLevel.LOW -> Color(0xFF1F6B43)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(work.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                work.agency,
                modifier = Modifier.padding(top = 3.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))
            WorkCardField("Valor da obra", work.investment)
            WorkCardField("Situação", situation, valueColor = situationColor, emphasize = true)
            WorkCardField("Estado", location.state)
            WorkCardField("Cidade", location.city)

            Text(
                "Risco calculado: ${work.riskScore}/100",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            LinearProgressIndicator(
                progress = work.riskScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            )

            if (work.signals.isNotEmpty()) {
                Text(
                    "Motivos para a sinalização:",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                work.signals.take(3).forEach { signal ->
                    Text(
                        text = "• ${signal.title}",
                        modifier = Modifier.padding(top = 5.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                "Auditar obra ›",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WorkCardField(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    emphasize: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(12.dp))
        Text(
            value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasize) FontWeight.Bold else FontWeight.Normal,
            color = valueColor
        )
    }
}

private fun situationLabel(level: RiskLevel): String = when (level) {
    RiskLevel.HIGH -> "Possível Fraude!"
    RiskLevel.MODERATE -> "Indícios a verificar"
    RiskLevel.LOW -> "Sem indícios relevantes"
}

private fun parseWorkLocation(rawLocation: String): WorkLocation {
    val location = rawLocation.trim()
    if (location.isBlank()) return WorkLocation("Não informado", "Não informada")

    val commaParts = location.split(",", limit = 2).map { it.trim() }
    if (commaParts.size == 2 && commaParts.all { it.isNotBlank() }) {
        return WorkLocation(state = commaParts[1], city = commaParts[0])
    }

    val dashParts = location.split(" - ", limit = 2).map { it.trim() }
    if (dashParts.size == 2 && dashParts.all { it.isNotBlank() }) {
        return WorkLocation(state = dashParts[1], city = dashParts[0])
    }

    return WorkLocation(state = location, city = "Não informada")
}
