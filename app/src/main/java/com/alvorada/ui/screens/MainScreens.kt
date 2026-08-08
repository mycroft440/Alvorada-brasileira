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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.alvorada.core.model.FeedPost
import com.alvorada.core.model.GovernmentWork
import com.alvorada.core.model.LegislativeHouse
import com.alvorada.core.model.Proposal
import com.alvorada.core.model.ProposalStatus
import com.alvorada.core.model.RiskLevel
import com.alvorada.core.model.WorkStatus

private val ApprovedGreen = Color(0xFF1F6B43)
private val ApprovedBackground = Color(0xFFE8F4EC)
private val RejectedRed = Color(0xFFB43B3B)
private val RejectedBackground = Color(0xFFFAE9E9)
private val ProgressAmber = Color(0xFFA66B12)
private val ProgressBackground = Color(0xFFFFF1D7)

@Composable
fun FeedScreen(
    contentPadding: PaddingValues,
    posts: List<FeedPost>,
    proposals: List<Proposal>,
    onProfileClick: () -> Unit,
    onProposalClick: (Proposal) -> Unit,
    onCommentClick: (FeedPost) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val proposalMap = remember(proposals) { proposals.associateBy { it.id } }
    val filteredPosts = posts.filter { post ->
        val proposal = proposalMap[post.proposalId]
        query.isBlank() || listOf(post.userName, post.comment, proposal?.code, proposal?.title)
            .filterNotNull()
            .any { it.contains(query, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            MainHeader(
                title = "Alvorada Brasileira",
                subtitle = "Feed de propostas compartilhadas",
                onProfileClick = onProfileClick
            )
            DemoNotice("O Feed mostra somente propostas que usuários compartilharam a partir da aba Propostas.")
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar no feed..."
            )
            SectionTitle("Feed")
        }

        if (filteredPosts.isEmpty()) {
            item { EmptyState("Nenhuma proposta compartilhada encontrada.") }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                val proposal = proposalMap[post.proposalId] ?: return@items
                FeedPostCard(
                    post = post,
                    proposal = proposal,
                    onProposalClick = { onProposalClick(proposal) },
                    onCommentClick = { onCommentClick(post) }
                )
            }
        }
    }
}

@Composable
fun ProposalsScreen(
    contentPadding: PaddingValues,
    proposals: List<Proposal>,
    onProfileClick: () -> Unit,
    onProposalClick: (Proposal) -> Unit,
    onShareClick: (Proposal) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ProposalStatus.APPROVED) }
    val filtered = proposals.filter {
        it.status == status && (
            query.isBlank() ||
                it.code.contains(query, true) ||
                it.title.contains(query, true) ||
                it.summary.contains(query, true) ||
                it.theme.contains(query, true)
            )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            MainHeader(
                title = "Propostas",
                subtitle = "Projetos e votações legislativas",
                onProfileClick = onProfileClick
            )
            DemoNotice("Base visual com dados fictícios. Depois, esta camada será alimentada pelo backend Alvorada com dados oficiais.")
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar PL, tema, Câmara, Senado..."
            )
            StatusSelector(status = status, onStatusChange = { status = it })
            SectionTitle(
                when (status) {
                    ProposalStatus.APPROVED -> "Propostas aprovadas"
                    ProposalStatus.REJECTED -> "Propostas rejeitadas"
                    ProposalStatus.IN_PROGRESS -> "Propostas em progresso"
                }
            )
        }

        if (filtered.isEmpty()) {
            item { EmptyState("Nenhuma proposta encontrada.") }
        } else {
            items(filtered, key = { it.id }) { proposal ->
                ProposalCard(
                    proposal = proposal,
                    onClick = { onProposalClick(proposal) },
                    onShare = { onShareClick(proposal) }
                )
            }
        }
    }
}

private enum class WorkFilter { ALL, HIGH_RISK, ONGOING, COMPLETED, PLANNED }

@Composable
fun WorksScreen(
    contentPadding: PaddingValues,
    works: List<GovernmentWork>,
    onProfileClick: () -> Unit,
    onWorkClick: (GovernmentWork) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(WorkFilter.ALL) }
    val filtered = works.filter { work ->
        val filterMatches = when (filter) {
            WorkFilter.ALL -> true
            WorkFilter.HIGH_RISK -> work.riskLevel == RiskLevel.HIGH
            WorkFilter.ONGOING -> work.status == WorkStatus.ONGOING
            WorkFilter.COMPLETED -> work.status == WorkStatus.COMPLETED
            WorkFilter.PLANNED -> work.status == WorkStatus.PLANNED
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
            MainHeader(
                title = "Obras do Gov",
                subtitle = "Fiscalização, gastos e indícios de irregularidade",
                onProfileClick = onProfileClick
            )
            DemoNotice("O índice de risco aponta anomalias para auditoria. Ele nunca deve ser tratado como prova automática de fraude ou corrupção.")
            AuditDashboard(works)
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar obra, cidade, órgão ou empresa..."
            )
            WorkFilterSelector(filter = filter, onFilterChange = { filter = it })
            SectionTitle("Radar de obras públicas")
        }

        if (filtered.isEmpty()) {
            item { EmptyState("Nenhuma obra encontrada.") }
        } else {
            items(filtered, key = { it.id }) { work ->
                WorkCard(work = work, onClick = { onWorkClick(work) })
            }
        }
    }
}

@Composable
private fun MainHeader(
    title: String,
    subtitle: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun DemoNotice(text: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        color = Color(0xFFFFF8DF),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = "⚠️ $text",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF765A12)
        )
    }
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text(placeholder) },
        leadingIcon = { Text("🔎") }
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StatusSelector(status: ProposalStatus, onStatusChange: (ProposalStatus) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ProposalStatus.values().forEach { item ->
            val label = when (item) {
                ProposalStatus.APPROVED -> "✓ Aprovadas"
                ProposalStatus.REJECTED -> "✕ Rejeitadas"
                ProposalStatus.IN_PROGRESS -> "◷ Em progresso"
            }
            FilterChip(
                selected = status == item,
                onClick = { onStatusChange(item) },
                label = { Text(label) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FeedPostCard(
    post: FeedPost,
    proposal: Proposal,
    onProposalClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    var reaction by remember(post.id) { mutableStateOf<String?>(null) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                        Text(post.userInitials, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(post.userName, fontWeight = FontWeight.Bold)
                    Text(post.publishedLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (post.comment.isNotBlank()) {
                Text(post.comment, modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.bodyMedium)
            }
            ProposalPreview(proposal = proposal, onClick = onProposalClick)
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                "💬 ${formatCount(post.commentsCount)} comentários   ↗ ${formatCount(post.sharesCount)} compartilhamentos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { reaction = if (reaction == "like") null else "like" }) {
                    Text("👍 ${formatCount(post.likesCount)}", color = if (reaction == "like") ApprovedGreen else MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = { reaction = if (reaction == "dislike") null else "dislike" }) {
                    Text("👎 ${formatCount(post.dislikesCount)}", color = if (reaction == "dislike") RejectedRed else MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = onCommentClick) { Text("💬 Comentar") }
            }
        }
    }
}

@Composable
private fun ProposalPreview(proposal: Proposal, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Column(Modifier.padding(13.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("🏛 ${houseLabel(proposal.house)} • ${proposal.updatedLabel}", style = MaterialTheme.typography.bodySmall)
                StatusBadge(proposal.status)
            }
            Text("${proposal.code} — ${proposal.title}", modifier = Modifier.padding(top = 8.dp), fontWeight = FontWeight.Bold)
            Text(proposal.summary, modifier = Modifier.padding(top = 5.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            proposal.voteSummary?.let { VoteSummaryRow(it.yes, it.no, it.abstentions) }
        }
    }
}

@Composable
private fun ProposalCard(proposal: Proposal, onClick: () -> Unit, onShare: () -> Unit) {
    var reaction by remember(proposal.id) { mutableStateOf<String?>(null) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🏛 ${houseLabel(proposal.house)} • ${proposal.updatedLabel}", style = MaterialTheme.typography.bodySmall)
                StatusBadge(proposal.status)
            }
            Text("${proposal.code} — ${proposal.title}", modifier = Modifier.padding(top = 10.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(proposal.summary, modifier = Modifier.padding(top = 6.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                AssistChip(onClick = {}, label = { Text(proposal.theme) })
            }
            proposal.voteSummary?.let { VoteSummaryRow(it.yes, it.no, it.abstentions) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            Text("💬 ${formatCount(proposal.commentsCount)}   ↗ ${formatCount(proposal.sharesCount)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onClick) { Text("Ver detalhes") }
                TextButton(onClick = { reaction = if (reaction == "like") null else "like" }) {
                    Text("👍 ${formatCount(proposal.likesCount)}", color = if (reaction == "like") ApprovedGreen else MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = onShare) { Text("↗ Compartilhar") }
            }
        }
    }
}

@Composable
private fun VoteSummaryRow(yes: Int, no: Int, abstentions: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        VoteNumber(yes, "SIM", ApprovedGreen)
        VoteNumber(no, "NÃO", RejectedRed)
        VoteNumber(abstentions, "ABST.", MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun VoteNumber(number: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(number.toString(), fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StatusBadge(status: ProposalStatus) {
    val background: Color
    val foreground: Color
    val label: String
    when (status) {
        ProposalStatus.APPROVED -> {
            background = ApprovedBackground
            foreground = ApprovedGreen
            label = "✓ Aprovado"
        }
        ProposalStatus.REJECTED -> {
            background = RejectedBackground
            foreground = RejectedRed
            label = "✕ Rejeitado"
        }
        ProposalStatus.IN_PROGRESS -> {
            background = ProgressBackground
            foreground = ProgressAmber
            label = "◷ Em progresso"
        }
    }
    Surface(color = background, shape = RoundedCornerShape(50)) {
        Text(label, modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall, color = foreground, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AuditDashboard(works: List<GovernmentWork>) {
    val highRisk = works.count { it.riskLevel == RiskLevel.HIGH }
    val alerts = works.sumOf { it.signals.size }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1C2A21)
    ) {
        Column(Modifier.padding(15.dp)) {
            Text("🛡️ Central de Fiscalização", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(
                "Cruza custos, contratos, pagamentos, concorrência e andamento físico para priorizar conferências.",
                modifier = Modifier.padding(top = 5.dp),
                color = Color(0xFFD4E0D7),
                style = MaterialTheme.typography.bodySmall
            )
            Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DarkKpi(works.size.toString(), "obras", Modifier.weight(1f))
                DarkKpi(highRisk.toString(), "risco alto", Modifier.weight(1f))
                DarkKpi(alerts.toString(), "alertas", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DarkKpi(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.White.copy(alpha = 0.08f), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
            Text(label, color = Color(0xFFD4E0D7), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun WorkFilterSelector(filter: WorkFilter, onFilterChange: (WorkFilter) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(filter == WorkFilter.ALL, { onFilterChange(WorkFilter.ALL) }, { Text("Todas") })
            FilterChip(filter == WorkFilter.HIGH_RISK, { onFilterChange(WorkFilter.HIGH_RISK) }, { Text("⚠ Risco alto") })
            FilterChip(filter == WorkFilter.ONGOING, { onFilterChange(WorkFilter.ONGOING) }, { Text("Em andamento") })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(filter == WorkFilter.COMPLETED, { onFilterChange(WorkFilter.COMPLETED) }, { Text("Concluídas") })
            FilterChip(filter == WorkFilter.PLANNED, { onFilterChange(WorkFilter.PLANNED) }, { Text("Planejadas") })
        }
    }
}

@Composable
private fun WorkCard(work: GovernmentWork, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(work.agency, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                RiskBadge(work.riskScore, work.riskLevel)
            }
            Text(work.title, modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${work.location} • ${work.investment}", modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.bodySmall)
            Text("Risco de irregularidade", modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = work.riskScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(top = 4.dp)
            )
            work.signals.take(3).forEach { signal ->
                Text(
                    text = "${riskEmoji(signal.level)} ${signal.title}",
                    modifier = Modifier.padding(top = 5.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text("Avanço físico: ${work.progressPercent}%", modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.labelMedium)
            LinearProgressIndicator(
                progress = work.progressPercent / 100f,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
            Text("Atualizado: ${work.lastUpdate}  •  Auditar obra ›", modifier = Modifier.padding(top = 9.dp), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RiskBadge(score: Int, level: RiskLevel) {
    val background = when (level) {
        RiskLevel.LOW -> ApprovedBackground
        RiskLevel.MODERATE -> ProgressBackground
        RiskLevel.HIGH -> RejectedBackground
    }
    val foreground = when (level) {
        RiskLevel.LOW -> ApprovedGreen
        RiskLevel.MODERATE -> ProgressAmber
        RiskLevel.HIGH -> RejectedRed
    }
    val text = when (level) {
        RiskLevel.LOW -> "Baixo"
        RiskLevel.MODERATE -> "Moderado"
        RiskLevel.HIGH -> "Alto"
    }
    Surface(color = background, shape = RoundedCornerShape(50)) {
        Text("⚠ $text • $score/100", modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), color = foreground, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun houseLabel(house: LegislativeHouse): String = when (house) {
    LegislativeHouse.CAMERA -> "Câmara"
    LegislativeHouse.SENADO -> "Senado"
    LegislativeHouse.CONGRESSO -> "Congresso"
}

internal fun riskEmoji(level: RiskLevel): String = when (level) {
    RiskLevel.LOW -> "🟢"
    RiskLevel.MODERATE -> "🟠"
    RiskLevel.HIGH -> "🔴"
}

internal fun formatCount(number: Int): String = when {
    number >= 1_000_000 -> String.format("%.1f mi", number / 1_000_000f).replace('.', ',')
    number >= 1_000 -> String.format("%.1f mil", number / 1_000f).replace('.', ',')
    else -> number.toString()
}
