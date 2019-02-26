package com.muhammadwahyudin.identitasku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "data",
    foreignKeys = [ForeignKey(
        entity = DataType::class,
        parentColumns = arrayOf("type_id"),
        childColumns = arrayOf("type_id")
    )]
)
data class Data(
    @ColumnInfo(name = "type_id") var typeId: Int,
    var value: String,
    var attr1: String? = "",
    var attr2: String? = "",
    var attr3: String? = "",
    var attr4: String? = "",
    var attr5: String? = ""
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return "$id - $typeId - $value - $attr1 - $attr2 - $attr3 - $attr4 - $attr5"
    }
}