package com.muhammadwahyudin.identitasku.data

import androidx.annotation.DrawableRes
import com.muhammadwahyudin.identitasku.R

/**
 * Contain all primitive value constants
 */
object Constants {
    const val DB_NAME = "identitasku-db"
    const val SP_PASSWORD = "password"

    enum class TYPE(val value: Int, @DrawableRes val iconRes: Int) {
        DEFAULT(-1, -1),
        KTP(1, R.drawable.ic_ktp),
        HANDPHONE(2, R.drawable.ic_handphone),
        ALAMAT(3, R.drawable.ic_address),
        PLN(4, R.drawable.ic_pln),
        PDAM(5, R.drawable.ic_pdam),
        NPWP(6, R.drawable.ic_npwp),
        REK_BANK(7, R.drawable.ic_bank_account),
        KK(8, R.drawable.ic_kk),
        STNK(9, R.drawable.ic_stnk),
        CC(10, R.drawable.ic_credit_card),
        BPJS(11, R.drawable.ic_bpjs),
        EMAIL(12, R.drawable.ic_email),
    }

    const val PRIVACY_POLICY_URL = "https://sites.google.com/view/identitasku/beranda"
}