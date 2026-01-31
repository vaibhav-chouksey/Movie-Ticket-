package com.example.ticket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // --- STATE ---
    var name = MutableStateFlow("")
    var dob = MutableStateFlow("")
    var country = MutableStateFlow("")

    // Genre Selection State
    private val _selectedGenres = MutableStateFlow<List<String>>(emptyList())
    val selectedGenres: StateFlow<List<String>> = _selectedGenres

    // UI Status
    private val _setupState = MutableStateFlow<SetupState>(SetupState.Idle)
    val setupState: StateFlow<SetupState> = _setupState

    val availableGenres = listOf(
        "Action", "Comedy", "Horror", "Romance",
        "Sci-Fi", "Drama", "Animation", "Thriller"
    )

    // Pre-fill name if available from Auth
    init {
        auth.currentUser?.displayName?.let { name.value = it }
    }

    // --- LOGIC ---

    fun toggleGenre(genre: String) {
        val current = _selectedGenres.value.toMutableList()
        if (current.contains(genre)) {
            current.remove(genre)
        } else {
            current.add(genre)
        }
        _selectedGenres.value = current
    }

    fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val email = auth.currentUser?.email ?: ""

        if (name.value.isBlank() || dob.value.isBlank() || country.value.isBlank()) {
            _setupState.value = SetupState.Error("Please fill all fields")
            return
        }

        _setupState.value = SetupState.Loading

        val userProfile = UserProfile(
            uid = uid,
            name = name.value,
            email = email,
            dateOfBirth = dob.value,
            country = country.value,
            favoriteGenres = _selectedGenres.value
        )

        // Save to Firestore Collection "users"
        firestore.collection("users").document(uid).set(userProfile)
            .addOnSuccessListener {
                _setupState.value = SetupState.Success
            }
            .addOnFailureListener { e ->
                _setupState.value = SetupState.Error(e.message ?: "Failed to save profile")
            }
    }
}

// Simple State Helper
sealed class SetupState {
    object Idle : SetupState()
    object Loading : SetupState()
    object Success : SetupState()
    data class Error(val message: String) : SetupState()
}