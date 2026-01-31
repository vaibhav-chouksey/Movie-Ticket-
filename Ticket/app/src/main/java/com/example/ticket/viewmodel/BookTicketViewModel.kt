package com.example.ticket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.manager.PaymentManager
import com.example.ticket.manager.PaymentStatus
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookTicketViewModel @Inject constructor(
    private val paymentManager: PaymentManager,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // Logic for UI to observe
    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState = _bookingState.asStateFlow()

    init {
        observePaymentResult()
    }

    private fun observePaymentResult() {
        viewModelScope.launch {
            paymentManager.paymentResult.collect { status ->
                when (status) {
                    is PaymentStatus.Success -> {
                        // Payment worked! Now actually book the ticket in database
                        confirmBookingInDatabase(status.paymentId)
                    }
                    is PaymentStatus.Error -> {
                        _bookingState.value = BookingState.Error(status.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun confirmBookingInDatabase(paymentId: String) {
        _bookingState.value = BookingState.Loading

        // ... (Firestore logic to save ticket) ...

        // On Firestore Success:
        _bookingState.value = BookingState.Success
    }
}

// Simple State for UI
sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val msg: String) : BookingState()
}