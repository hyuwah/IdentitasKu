package com.muhammadwahyudin.identitasku.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.muhammadwahyudin.identitasku.data.Constants.TYPE
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataWithDataType(
    val id: Int,
    @ColumnInfo(name = "type_id") var typeId: Int,
    var value: String,
    var attr1: String? = "",
    var attr2: String? = "",
    var attr3: String? = "",
    var attr4: String? = "",
    var attr5: String? = "",
    @ColumnInfo(name = "name") var typeName: String,
    @ColumnInfo(name = "is_unique") var isUnique: Boolean,
    @ColumnInfo(name = "is_custom") var isCustom: Boolean
) : Parcelable, MultiItemEntity {

    fun type(): TYPE = TYPE.entries.find { it.value == typeId } ?: TYPE.DEFAULT

    override val itemType: Int
        get() = when (typeId) {
            TYPE.KTP.value,
            TYPE.REK_BANK.value -> typeId
            else -> TYPE.DEFAULT.value
        }

    override fun toString(): String {
        return """
            $id - ($typeId) $typeName - $value - $attr1 - $attr2 - $attr3 - $attr4 - $attr5 - 
            $isUnique - $isCustom
        """.trimIndent()
    }
}