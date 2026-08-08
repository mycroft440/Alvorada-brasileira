package com.alvorada.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.alvorada.core.model.PartyVoteSummary
import com.alvorada.core.model.ParliamentarianVote
import com.alvorada.core.model.PrivacyLevel
import com.alvorada.core.model.Proposal
import com.alvorada.core.model.RiskLevel
import com.alvorada.core.model.TimelineEvent
import com.alvorada.core.model.UserProfile
import com.alvorada.core.model.VoteChoice

@Composable
fun ShareProposalSheet(
    proposal: Proposal,
    onPublish: (String) -> Unit
) {
    var comment by remember(proposal.id) { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text("Compartilhar proposta", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            "${proposal.code} — ${proposal.title}",
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(proposal.summary, modifier = Modifier.padding(top = 5.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            minLines = 4,
            label = { Text("Sua opinião (opcional)") },
            placeholder = { Text("Comente antes de compartilhar...") }
        )
        Button(
            onClick = { onPublish(comment.trim()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 22.dp)
        ) {
            Text("Publicar no Feed")
        }
    }
}

@Composable
fun CommentsSheet(post: FeedPost) {
    val addedComments = remember(post.id) { mutableStateListOf<String>() }
    var text by remember(post.id) { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text("Comentários", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            "Publicação de ${post.userName}",
            modifier = Modifier.padding(top = 3.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DemoComment("Marina", "Concordo, mas quero conferir o texto completo antes de decidir.")
        DemoComment("Paulo", "Seria útil mostrar a fonte oficial em cada etapa da tramitação.")
        addedComments.forEach { DemoComment("Você", it) }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            minLines = 3,
            label = { Text("Novo comentário") }
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    addedComments.add(text.trim())
                    text = ""
                }
            },
            enabled = text.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 22.dp)
        ) {
            Text("Comentar")
        }
    }
}

@Composable
private fun DemoComment(name: String, comment: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(comment, modifier = Modifier.padding(top = 3.dp), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ProposalDetailSheet(
    proposal: Proposal,
    onShare: () -> Unit
) {
    val tabs = listOf("Resumo", "Tramitação", "Partidos", "Parlamentares")
    var selectedTab by remember(proposal.id) { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(proposal.code, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(proposal.title, modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            "${proposal.author} • ${proposal.theme}",
            modifier = Modifier.padding(top = 5.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        proposal.voteSummary?.let { votes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailVote(votes.yes, "SIM", Color(0xFF1F6B43))
                DetailVote(votes.no, "NÃO", Color(0xFFB43B3B))
                DetailVote(votes.abstentions, "ABST.", MaterialTheme.colorScheme.onSurface)
            }
        }
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, label ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(label, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        when (selectedTab) {
            0 -> ProposalSummary(proposal)
            1 -> TimelineList(proposal.timeline)
            2 -> PartyVotes(proposal.partyVotes)
            3 -> ParliamentarianVotes(proposal.parliamentarianVotes)
        }
        Button(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 22.dp)
        ) {
            Text("↗ Compartilhar proposta")
        }
    }
}

@Composable
private fun ProposalSummary(proposal: Proposal) {
    DetailField("Resumo", proposal.summary)
    DetailField("Autoria", proposal.author)
    DetailField("Tema", proposal.theme)
    DetailField("Fonte atual", proposal.sourceLabel)
    Text(
        "Na integração real, a fonte oficial e a data de sincronização serão mostradas de forma permanente.",
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun TimelineList(events: List<TimelineEvent>) {
    if (events.isEmpty()) {
        EmptySheetMessage("Ainda não há tramitação disponível.")
        return
    }
    events.forEachIndexed { index, event ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(12.dp),
                    shape = CircleShape,
                    color = if (event.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                ) {}
                if (index != events.lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(42.dp)
                    ) {
                        Surface(modifier = Modifier.fillMaxWidth().height(42.dp), color = MaterialTheme.colorScheme.outlineVariant) {}
                    }
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.padding(bottom = 14.dp)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                Text(event.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PartyVotes(votes: List<PartyVoteSummary>) {
    if (votes.isEmpty()) {
        EmptySheetMessage("Ainda não há votação nominal para agrupar por partido.")
        return
    }
    votes.forEach { party ->
        val total = (party.yes + party.no + party.abstentions).coerceAtLeast(1)
        val yesPercent = party.yes.toFloat() / total
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(party.party, fontWeight = FontWeight.Bold)
                Text("${(yesPercent * 100).toInt()}% Sim", fontWeight = FontWeight.SemiBold)
            }
            Text("${party.yes} Sim • ${party.no} Não • ${party.abstentions} Abstenções", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LinearProgressIndicator(progress = yesPercent, modifier = Modifier.fillMaxWidth().padding(top = 5.dp))
        }
        HorizontalDivider()
    }
}

@Composable
private fun ParliamentarianVotes(votes: List<ParliamentarianVote>) {
    if (votes.isEmpty()) {
        EmptySheetMessage("Ainda não há votos individuais disponíveis.")
        return
    }
    votes.forEach { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.SemiBold)
                Text(item.partyState, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                when (item.vote) {
                    VoteChoice.YES -> "SIM"
                    VoteChoice.NO -> "NÃO"
                    VoteChoice.ABSTENTION -> "ABSTENÇÃO"
                },
                fontWeight = FontWeight.Bold,
                color = when (item.vote) {
                    VoteChoice.YES -> Color(0xFF1F6B43)
                    VoteChoice.NO -> Color(0xFFB43B3B)
                    VoteChoice.ABSTENTION -> Color(0xFFA66B12)
                }
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun DetailVote(number: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(number.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun WorkDetailSheet(work: GovernmentWork) {
    var selectedPanel by remember(work.id) { mutableStateOf("Financeiro") }
    val panels = listOf("Financeiro", "Contrato", "Cronograma", "Evidências")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(work.agency, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(work.title, modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(work.location, modifier = Modifier.padding(top = 3.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("RADAR DE RISCO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("${work.riskScore}/100", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = riskColor(work.riskLevel))
                    }
                    Text(riskLabel(work.riskLevel), color = riskColor(work.riskLevel), fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(progress = work.riskScore / 100f, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                work.signals.forEach { signal ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text("${riskEmoji(signal.level)} ${signal.title}", fontWeight = FontWeight.SemiBold)
                            Text(signal.description, modifier = Modifier.padding(top = 2.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Text(
            "Um alerta indica algo que merece verificação. Não comprova fraude, corrupção ou crime.",
            modifier = Modifier.padding(top = 10.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            panels.take(2).forEach { panel ->
                FilterChip(selectedPanel == panel, { selectedPanel = panel }, { Text(panel) }, modifier = Modifier.weight(1f))
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            panels.drop(2).forEach { panel ->
                FilterChip(selectedPanel == panel, { selectedPanel = panel }, { Text(panel) }, modifier = Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(10.dp))
        when (selectedPanel) {
            "Financeiro" -> {
                DetailField("Orçamento inicial", work.originalBudget)
                DetailField("Valor atual", work.investment)
                DetailField("Pago até agora", work.paidAmount)
                DetailField("Comparação com referência", work.benchmarkDelta)
            }
            "Contrato" -> {
                DetailField("Contrato", work.contract)
                DetailField("Empresa / executor", work.contractor)
                DetailField("Número de licitantes", work.bidders.toString())
                DetailField("Aditivos", work.amendments.toString())
            }
            "Cronograma" -> {
                DetailField("Avanço físico", "${work.progressPercent}%")
                DetailField("Atraso estimado", "${work.delayDays} dias")
                LinearProgressIndicator(progress = work.progressPercent / 100f, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                TimelineList(work.milestones)
            }
            else -> {
                DetailField("Última atualização", work.lastUpdate)
                DetailField("Fonte atual", work.sourceLabel)
                Text(
                    "Futuro: documentos, fotos georreferenciadas, medições, empenhos, pagamentos, licitações e evidências enviadas pela comunidade com moderação.",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ProfileSheet(profile: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                    Text("AB", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(profile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(profile.handle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        SheetSection("Informações pessoais")
        PrivacyField("Onde mora", profile.cityState, PrivacyLevel.PRIVATE)
        PrivacyField("Profissão", profile.profession, PrivacyLevel.PUBLIC)
        PrivacyField("Renda mensal", profile.incomeRange, PrivacyLevel.PRIVATE)

        SheetSection("Em quem votei")
        profile.pastVotes.forEach { (office, value) ->
            PrivacyField(office, value, PrivacyLevel.PRIVATE)
        }

        SheetSection("Próxima eleição — em quem votaria")
        profile.nextElectionIntentions.forEach { (office, value) ->
            PrivacyField(office, value, PrivacyLevel.PRIVATE)
        }

        SheetSection("Satisfação com os governos")
        SatisfactionField("Governo federal", profile.federalSatisfaction)
        SatisfactionField("Governo estadual", profile.stateSatisfaction)
        SatisfactionField("Governo municipal", profile.citySatisfaction)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFFF4DC)
        ) {
            Text(
                "Voto, intenção de voto, renda e avaliações políticas são dados sensíveis e devem permanecer privados por padrão.",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF75551E)
            )
        }
    }
}

@Composable
private fun PrivacyField(label: String, value: String, initialPrivacy: PrivacyLevel) {
    var privacy by remember(label) { mutableStateOf(initialPrivacy) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, modifier = Modifier.padding(top = 3.dp), fontWeight = FontWeight.SemiBold)
            TextButton(onClick = {
                privacy = when (privacy) {
                    PrivacyLevel.PRIVATE -> PrivacyLevel.FOLLOWERS
                    PrivacyLevel.FOLLOWERS -> PrivacyLevel.PUBLIC
                    PrivacyLevel.PUBLIC -> PrivacyLevel.PRIVATE
                }
            }) {
                Text("${privacyEmoji(privacy)} ${privacy.label}")
            }
        }
    }
}

@Composable
private fun SatisfactionField(label: String, initialValue: Int) {
    var value by remember(label) { mutableStateOf(initialValue.toFloat()) }
    var privacy by remember("privacy-$label") { mutableStateOf(PrivacyLevel.PRIVATE) }
    val index = value.toInt().coerceIn(1, 5)
    val labels = listOf("", "Muito insatisfeito", "Insatisfeito", "Neutro", "Satisfeito", "Muito satisfeito")
    val emojis = listOf("", "😠", "🙁", "😐", "🙂", "😄")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
                    Text(labels[index], fontWeight = FontWeight.SemiBold)
                }
                Text(emojis[index], style = MaterialTheme.typography.titleLarge)
            }
            Slider(value = value, onValueChange = { value = it }, valueRange = 1f..5f, steps = 3)
            TextButton(onClick = {
                privacy = when (privacy) {
                    PrivacyLevel.PRIVATE -> PrivacyLevel.FOLLOWERS
                    PrivacyLevel.FOLLOWERS -> PrivacyLevel.PUBLIC
                    PrivacyLevel.PUBLIC -> PrivacyLevel.PRIVATE
                }
            }) { Text("${privacyEmoji(privacy)} ${privacy.label}") }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, modifier = Modifier.padding(top = 4.dp), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SheetSection(title: String) {
    Text(
        title.uppercase(),
        modifier = Modifier.padding(top = 18.dp, bottom = 2.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun EmptySheetMessage(text: String) {
    Text(text, modifier = Modifier.padding(vertical = 18.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private fun privacyEmoji(level: PrivacyLevel): String = when (level) {
    PrivacyLevel.PRIVATE -> "🔒"
    PrivacyLevel.FOLLOWERS -> "👥"
    PrivacyLevel.PUBLIC -> "🌐"
}

private fun riskLabel(level: RiskLevel): String = when (level) {
    RiskLevel.LOW -> "Risco baixo"
    RiskLevel.MODERATE -> "Risco moderado"
    RiskLevel.HIGH -> "Risco alto"
}

private fun riskColor(level: RiskLevel): Color = when (level) {
    RiskLevel.LOW -> Color(0xFF1F6B43)
    RiskLevel.MODERATE -> Color(0xFFA66B12)
    RiskLevel.HIGH -> Color(0xFFB43B3B)
}
