package com.muhammadwahyudin.identitasku.data

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.muhammadwahyudin.identitasku.R

/**
 * Contain all primitive value constants
 */
object Constants {
    const val DB_NAME = "identitasku-db"
    const val SP_PASSWORD = "password"

    enum class TYPE(
        val value: Int,
        @DrawableRes val iconRes: Int,
        @IdRes val viewId: Int
    ) {
        DEFAULT(-1, -1, -1),
        KTP(1, R.drawable.ic_ktp, R.id.cb_filter_ktp),
        HANDPHONE(2, R.drawable.ic_handphone, R.id.cb_filter_hp),
        ALAMAT(3, R.drawable.ic_address, R.id.cb_filter_address),
        PLN(4, R.drawable.ic_pln, R.id.cb_filter_pln),
        PDAM(5, R.drawable.ic_pdam, R.id.cb_filter_pdam),
        NPWP(6, R.drawable.ic_npwp, R.id.cb_filter_npwp),
        REK_BANK(7, R.drawable.ic_bank_account, R.id.cb_filter_bank_account),
        KK(8, R.drawable.ic_kk, R.id.cb_filter_kk),
        STNK(9, R.drawable.ic_stnk, R.id.cb_filter_stnk),
        CC(10, R.drawable.ic_credit_card, R.id.cb_filter_cc),
        BPJS(11, R.drawable.ic_bpjs, R.id.cb_filter_bpjs),
        EMAIL(12, R.drawable.ic_email, R.id.cb_filter_email),
    }

    const val PRIVACY_POLICY_URL = "https://sites.google.com/view/identitasku/beranda"
}