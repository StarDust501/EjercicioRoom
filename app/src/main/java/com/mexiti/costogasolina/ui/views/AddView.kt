package com.mexiti.costogasolina.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mexiti.costogasolina.R
import com.mexiti.costogasolina.ViewModel.GasolineViewModel
import com.mexiti.costogasolina.domain.calcularMonto
import com.mexiti.costogasolina.ui.components.GasolineForm
import com.mexiti.costogasolina.ui.components.MainTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Datos estructurados para mostrar en el resumen de la carga guardada.
 */
data class SavedSummary(
    val price: Double,
    val liters: Double,
    val tip: Double,
    val kilometers: Double,
    val date: String
)

/**
 * Pantalla para registrar una nueva carga de gasolina utilizando [GasolineForm].
 * Al guardar con éxito, despliega un cuadro de diálogo con un icono de verificación (palomita),
 * el desglose de lo registrado y opciones para ver el historial o agregar otra carga.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddView(
    navController: NavController,
    viewModel: GasolineViewModel,
    modifier: Modifier = Modifier
) {
    var savedSummary by remember { mutableStateOf<SavedSummary?>(null) }
    var formKey by remember { mutableIntStateOf(0) }

    // Diálogo de éxito con palomita y resumen
    if (savedSummary != null) {
        SuccessSaveDialog(
            summary = savedSummary!!,
            onGoHome = {
                savedSummary = null
                navController.popBackStack()
            },
            onAddAnother = {
                savedSummary = null
                formKey++ // Reinicia el formulario para capturar otra carga
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = stringResource(id = R.string.calcular_monto)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        key(formKey) {
            GasolineForm(
                buttonText = "Guardar Registro",
                onSubmit = { price, liters, tip, kilometers ->
                    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    viewModel.addTransaction(price, liters, tip, currentDate, kilometers)

                    // Guarda el resumen para mostrar la pantalla emergente con la palomita verde
                    savedSummary = SavedSummary(
                        price = price,
                        liters = liters,
                        tip = tip,
                        kilometers = kilometers,
                        date = currentDate
                    )
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

/**
 * Diálogo de confirmación que aparece al guardar una carga.
 * Muestra la palomita verde, resumen de montos y opciones de acción.
 */
@Composable
fun SuccessSaveDialog(
    summary: SavedSummary,
    onGoHome: () -> Unit,
    onAddAnother: () -> Unit
) {
    val total = calcularMonto(
        precio = summary.price,
        cantLitros = summary.liters,
        darPropina = summary.tip > 0,
        propina = summary.tip
    )

    AlertDialog(
        onDismissRequest = { },
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Guardado con éxito",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )
        },
        title = {
            Text(
                text = "¡Carga Guardada!",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen del registro:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Monto Total: $total",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Litros: ${summary.liters} L",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Precio/L: $${summary.price}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (summary.tip > 0) {
                            Text(
                                text = "Propina: $${summary.tip}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (summary.kilometers > 0) {
                            Text(
                                text = "Km: ${summary.kilometers} km",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onGoHome) {
                Text("Ver Historial")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onAddAnother) {
                Text("Registrar Otra Carga")
            }
        }
    )
}
