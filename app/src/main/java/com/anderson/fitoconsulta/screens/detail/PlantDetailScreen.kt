package com.anderson.fitoconsulta.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    viewModel: PlantDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.plant?.nome ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        val plant = uiState.plant
        if (plant != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val context = LocalContext.current
                val imageResName = plant.image_res_name
                val resourceId = remember(imageResName) {
                    if (imageResName.isNullOrBlank()) 0 else
                        context.resources.getIdentifier(
                            imageResName,
                            "drawable",
                            context.packageName
                        )
                }

                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = "Foto de ${plant.nome}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(16.dp))

                DetailSection("Descrição", plant.descricao)
                DetailSection("Parte Utilizada", plant.parte_utilizada)
                DetailSection("Preparo", plant.preparo)
                DetailSection("Indicações", plant.indicacoes)
                DetailSection("Riscos e Contraindicações", plant.riscos_contraindicaoes)
                DetailSection("Referências", plant.referencias)
            }
        }
    }
}

@Composable
fun DetailSection(title: String, content: String?) {
    if (!content.isNullOrBlank()) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = content,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}