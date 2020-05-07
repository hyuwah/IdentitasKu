package com.muhammadwahyudin.identitasku.ui.datainput.contract

sealed class ValidationResult {
    data class Valid(val value: String) : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}