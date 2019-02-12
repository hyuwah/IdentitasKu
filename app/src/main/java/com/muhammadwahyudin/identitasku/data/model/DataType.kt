package com.muhammadwahyudin.identitasku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_type")
data class DataType(
    var name: String,
    @ColumnInfo(name = "is_unique") var isUnique: Boolean,
    @ColumnInfo(name = "is_custom") var isCustom: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return "$id - $name - $isUnique - $isCustom"
    }
}