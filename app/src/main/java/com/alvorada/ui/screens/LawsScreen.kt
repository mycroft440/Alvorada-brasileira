package com.alvorada.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LawsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Painel Legislativo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            // 1. Card de Impostos Arrecadados
            item {
                TaxRevenueCard()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Card de Dívida Pública e Juros
            item {
                PublicDebtCard()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 3. Seção de Gastos por Categoria
            item {
                SpendingSection()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text("Projetos de Lei em Destaque", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(5) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Projeto de Lei nº ${1234 + index}/2024", style = MaterialTheme.typography.titleMedium)
                        Text("Ementa: Dispõe sobre a transparência em obras públicas e dá outras providências.", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        SuggestionChip(onClick = {}, label = { Text("Em tramitação") })
                    }
                }
            }
        }
    }
}

@Composable
fun TaxRevenueCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B5E20) // Verde Escuro Profundo
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Total de impostos arrecadados em 2026 até Abril",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "1.3 Trilhões",
                color = Color(0xFFFFD600), // Amarelo Vibrante
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = { /* Ver outros anos */ },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Ver outros anos >>",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PublicDebtCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB71C1C) // Vermelho Escuro
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Dívida Pública Total:",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "R$ 6.1 Trilhões",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.2f))
            
            Text(
                text = "Juros a pagar este mês:",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "R$ 52.4 Bilhões",
                color = Color(0xFFFFB74D),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SpendingSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gastos por [ 2026, Abril ]",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Filtrar",
                color = Color(0xFF1351B4),
                style = MaterialTheme.typography.labelLarge
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Maiores gastos para o menor no Brasil:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SpendingItem(
            category = "Saúde",
            value = "500 Bilhões",
            progress = 0.8f,
            color = Color(0xFF2E7D32)
        )
        
        SpendingItem(
            category = "Educação",
            value = "350 Bilhões",
            progress = 0.6f,
            color = Color(0xFF1565C0)
        )
    }
}

@Composable
fun SpendingItem(category: String, value: String, progress: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp).clickable { /* Detalhes */ }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = category, fontWeight = FontWeight.SemiBold)
            Text(text = value, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
        Text(
            text = "clique para saber mais",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
        )
    }
}
