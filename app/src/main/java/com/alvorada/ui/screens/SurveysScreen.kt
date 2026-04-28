package com.alvorada.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SurveysScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pesquisas Públicas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(3) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sua opinião: Reforma da Praça Central", style = MaterialTheme.typography.titleMedium)
                        Text("Você concorda com a ampliação da área verde em detrimento do estacionamento?", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = {}) { Text("Concordo") }
                            OutlinedButton(onClick = {}) { Text("Discordo") }
                        }
                    }
                }
            }
        }
    }
}
