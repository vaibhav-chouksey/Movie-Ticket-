package com.example.ticket.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BookTicketScreen(onPaymentSuccess: () -> Unit, onBackClick: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select Seats & Pay")

            Button(onClick = onPaymentSuccess) {
                Text("Confirm Payment")
            }
            Button(onClick = onBackClick) {
                Text("Cancel")
            }
        }
    }
}