package com.example.untitled_capstone.presentation.feature.login.state

import com.example.untitled_capstone.domain.model.Address

data class AddressState(
    val address: Address? = null,
    val loading: Boolean = false,
    val error: String? = null
)