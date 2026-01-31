package com.example.ticket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.ticket.manager.PaymentManager
import com.example.ticket.navigation.AppNavigation
import com.example.ticket.ui.theme.TicketTheme
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    @Inject
    lateinit var paymentManager: PaymentManager // Inject the Manager

    // ... onCreate is standard ...

    fun startPayment(amount: Double) {
        val checkout = Checkout()
        // SECURE KEY USAGE
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY_ID)

        try {
            val options = JSONObject()
            options.put("name", "CineTicket")
            options.put("currency", "INR")
            options.put("amount", (amount * 100).toInt())
            options.put("theme.color", "#E91E63")

            checkout.open(this, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- REPORT RESULTS TO MANAGER ---

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        // Use LifecycleScope to talk to the Manager
        lifecycleScope.launch {
            paymentManager.onPaymentSuccess(razorpayPaymentID ?: "Unknown ID")
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        lifecycleScope.launch {
            paymentManager.onPaymentFailure(response ?: "Payment Failed")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation() // <--- Call the new Navigation function
                }
            }
        }
    }
}



