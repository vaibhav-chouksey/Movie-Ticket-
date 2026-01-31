package com.example.ticket.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

// SEALED CLASS: Defines strict states for payment
sealed class PaymentStatus {
    object Idle : PaymentStatus()
    data class Success(val paymentId: String) : PaymentStatus()
    data class Error(val message: String) : PaymentStatus()
}

@Singleton
class PaymentManager @Inject constructor() {

    // A "Hot Flow" that emits events to anyone listening (ViewModel)
    private val _paymentResult = MutableSharedFlow<PaymentStatus>(replay = 0)
    val paymentResult = _paymentResult.asSharedFlow()

    // Called by MainActivity when payment succeeds
    suspend fun onPaymentSuccess(paymentId: String) {
        _paymentResult.emit(PaymentStatus.Success(paymentId))
    }

    // Called by MainActivity when payment fails
    suspend fun onPaymentFailure(error: String) {
        _paymentResult.emit(PaymentStatus.Error(error))
    }
}