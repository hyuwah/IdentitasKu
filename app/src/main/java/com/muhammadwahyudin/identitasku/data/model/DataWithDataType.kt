package com.muhammadwahyudin.identitasku.data.model

import androidx.room.ColumnInfo

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
) {
    override fun toString(): String {
        return "$id - ($typeId) $typeName - $value - $attr1 - $attr2 - $attr3 - $attr4 - $attr5 - $isUnique - $isCustom"
    }
}