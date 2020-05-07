package com.muhammadwahyudin.identitasku.ui.datainput.contract

import com.muhammadwahyudin.identitasku.data.model.Data

interface FragmentDataInputViewModel {
    fun getCurrentData(): Data?
    fun checkValueValidation(result: ValidationResult)
    fun setAttribute(
        attr1: String? = null,
        attr2: String? = null,
        attr3: String? = null,
        attr4: String? = null,
        attr5: String? = null
    )

    fun hasUnsavedData(toggle: Boolean)
}