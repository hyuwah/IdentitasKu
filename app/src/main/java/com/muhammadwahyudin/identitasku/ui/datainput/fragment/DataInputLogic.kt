package com.muhammadwahyudin.identitasku.ui.datainput.fragment

import android.text.InputType
import android.util.Patterns
import com.muhammadwahyudin.identitasku.data.InputDataset
import com.muhammadwahyudin.identitasku.ui.datainput.contract.InputLogic
import com.muhammadwahyudin.identitasku.ui.datainput.contract.ValidationResult

object DataInputLogic {

    class KTP(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() && value.length == 16 ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor KTP harus 16 digit")
            }
        }
    }

    class Email(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_TEXT
                .or(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT)

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(value).matches() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Format alamat email tidak valid")
            }
        }
    }

    class Handphone(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_PHONE

        override fun attribute2Data(): Pair<String, List<String>>? {
            return "Provider" to InputDataset.PHONENUM_PROVIDERS
        }

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() && Patterns.PHONE.matcher(value).matches() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Format nomor handphone tidak valid")
            }
        }
    }

    class Address(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_TEXT
                .or(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS)

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Alamat masih kosong")
            }
        }
    }

    class BankAccount(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun attribute2Data(): Pair<String, List<String>>? {
            return "Bank" to InputDataset.BANK_LIST
        }

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor rekening masih kosong")
            }
        }
    }

    class CreditCard(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor Kartu Kredit masih kosong")
            }
        }
    }

    class BPJS(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor BPJS masih kosong")
            }
        }
    }

    class KartuKeluarga(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor Kartu Keluarga masih kosong")
            }
        }
    }

    class NPWP(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor NPWP masih kosong")
            }
        }
    }

    class PDAM(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor PDAM masih kosong")
            }
        }
    }

    class PLN(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun attribute2Data(): Pair<String, List<String>>? {
            return "Jenis" to InputDataset.PLN_TYPE
        }

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor PLN masih kosong")
            }
        }
    }

    class STNK(override val typeName: String) : InputLogic {

        override val valueInputType: Int
            get() = InputType.TYPE_CLASS_NUMBER

        override fun checkValidation(value: CharSequence): ValidationResult {
            return when {
                value.isNotEmpty() ->
                    ValidationResult.Valid(value.toString())
                else -> ValidationResult.Invalid("Nomor STNK masih kosong")
            }
        }
    }
}