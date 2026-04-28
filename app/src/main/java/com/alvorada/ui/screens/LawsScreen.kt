package com.alvorada.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LawsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Painel Legislativo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            // 1. Card de Impostos Arrecadados
            item {
                TaxRevenueCard()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text("Projetos em Destaque", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
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
