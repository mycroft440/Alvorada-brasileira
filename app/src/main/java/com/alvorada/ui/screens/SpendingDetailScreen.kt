package com.alvorada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingDetailScreen(
    category: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes: $category", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1351B4),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Resumo do Gasto na Categoria
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Investido em 2026", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = if (category == "Saúde") "R$ 500 Bilhões" else "R$ 350 Bilhões",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Detalhamento de Gastos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Itens Detalhados (Exemplos para Saúde)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (category == "Saúde") {
                    item {
                        DetailItem(
                            icon = Icons.Default.LocalHospital,
                            title = "Construção: Hospital Municipal Alvorada",
                            subtitle = "Obra finalizada em Março/2026",
                            value = "R$ 200 Milhões",
                            tag = "Obras"
                        )
                    }
                    item {
                        DetailItem(
                            icon = Icons.Default.Medication,
                            title = "Aquisição de Medicamentos",
                            subtitle = "Lote de insulina e remédios de uso contínuo",
                            value = "R$ 500 Milhões",
                            tag = "Insumos"
                        )
                    }
                    item {
                        DetailItem(
                            icon = Icons.Default.HealthAndSafety,
                            title = "Manutenção de UPAs",
                            subtitle = "Equipamentos e reformas estruturais",
                            value = "R$ 50 Milhões",
                            tag = "Manutenção"
                        )
                    }
                } else {
                    item {
                        DetailItem(
                            icon = Icons.Default.ArrowBack, // Placeholder
                            title = "Item de Gasto: $category",
                            subtitle = "Detalhamento em processo de atualização",
                            value = "R$ ---",
                            tag = "Geral"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, title: String, subtitle: String, value: String, tag: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                SuggestionChip(
                    onClick = {},
                    label = { Text(tag, fontSize = 10.sp) },
                    modifier = Modifier.height(24.dp)
                )
            }
            
            Text(
                text = value,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2E7D32),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingDetailPreview() {
    MaterialTheme {
        SpendingDetailScreen("Saúde", {})
    }
}
