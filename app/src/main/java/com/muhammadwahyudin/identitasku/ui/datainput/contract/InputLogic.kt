package com.muhammadwahyudin.identitasku.ui.datainput.contract

interface InputLogic {
    fun checkValidation(value: CharSequence): ValidationResult
    val typeName: String
    val valueInputType: Int
    fun attribute2Data(): Pair<String, List<String>>? = null
}