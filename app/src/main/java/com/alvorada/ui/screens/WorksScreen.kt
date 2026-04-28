package com.alvorada.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorksScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Fiscalização de Obras", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(5) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Construção da Escola Municipal Alvorada", style = MaterialTheme.typography.titleMedium)
                        LinearProgressIndicator(
                            progress = 0.6f + (index * 0.05f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Progresso: ${(60 + index * 5)}%", style = MaterialTheme.typography.bodySmall)
                            Text("Investimento: R$ 2.400.000,00", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
