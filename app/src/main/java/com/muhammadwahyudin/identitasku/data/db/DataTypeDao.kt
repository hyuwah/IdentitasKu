@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.muhammadwahyudin.identitasku.data.db

import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.DataType

@Dao
abstract class DataTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(dataType: DataType)

    @Update
    abstract suspend fun update(dataType: DataType)

    @Delete
    abstract suspend fun delete(dataType: DataType)

    @Query("SELECT * FROM data_type")
    abstract suspend fun getAll(): List<DataType>

    @Query("DElETE FROM data_type")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM SQLITE_SEQUENCE WHERE name = 'data_type'")
    abstract suspend fun resetAutoincrementId()

    @Query(
        """
        SELECT data_type.type_id, data_type.name, data_type.is_unique, data_type.is_custom 
        FROM data_type INNER JOIN data ON data_type.type_id=data.type_id 
        WHERE data_type.is_unique=1
    """
    )
    abstract suspend fun getAllExistingUniqueType(): List<DataType>

    @Transaction
    open suspend fun reset() {
        deleteAll()
        resetAutoincrementId()
        insert(DataType("KTP", isUnique = true, isCustom = false))
        insert(DataType("Nomor Handphone", isUnique = false, isCustom = false))
        insert(DataType("Alamat", isUnique = false, isCustom = false))
        insert(DataType("Nomor PLN", isUnique = false, isCustom = false))
        insert(DataType("Nomor PDAM", isUnique = false, isCustom = false))
        insert(DataType("Nomor NPWP", isUnique = true, isCustom = false))
        insert(DataType("Nomor Rekening Bank", isUnique = false, isCustom = false))
        insert(DataType("Nomor Kartu Keluarga", isUnique = true, isCustom = false))
        insert(DataType("Nomor STNK", isUnique = false, isCustom = false))
        insert(DataType("Nomor Kartu Kredit", isUnique = false, isCustom = false))
        insert(DataType("Nomor BPJS", isUnique = false, isCustom = false))
        insert(DataType("Alamat Email", isUnique = false, isCustom = false))
    }
}