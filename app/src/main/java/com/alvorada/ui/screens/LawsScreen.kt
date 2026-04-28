package com.alvorada.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LawsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Rastreamento de Leis", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
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
