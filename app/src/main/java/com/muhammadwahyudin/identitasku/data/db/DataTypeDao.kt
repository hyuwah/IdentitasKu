@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.muhammadwahyudin.identitasku.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.DataType

@Dao
interface DataTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataType: DataType)

    @Update
    fun update(dataType: DataType)

    @Delete
    fun delete(dataType: DataType)

    @Query("SELECT * FROM data_type")
    fun getAll(): LiveData<List<DataType>>

    @Query("DElETE FROM data_type")
    fun deleteAll()

    @Query("DELETE FROM SQLITE_SEQUENCE WHERE name = 'data_type'")
    fun resetAutoincrementId()

    @Query("SELECT data_type.id, data_type.name, data_type.is_unique, data_type.is_custom FROM data_type INNER JOIN data ON data_type.id=data.type_id WHERE data_type.is_unique=1")
    fun getAllExistingUniqueType(): LiveData<List<DataType>>

    @Transaction
    fun reset() {
        deleteAll()
        resetAutoincrementId()
        insert(DataType("KTP", true, false))
        insert(DataType("Nomor Handphone", false, false))
        insert(DataType("Alamat", false, false))
        insert(DataType("Nomor PLN", false, false))
        insert(DataType("Nomor PDAM", false, false))
        insert(DataType("Nomor NPWP", true, false))
        insert(DataType("Nomor Rekening Bank", false, false))
        insert(DataType("Nomor Kartu Keluarga", true, false))
        insert(DataType("Nomor STNK", false, false))
        insert(DataType("Nomor Kartu Kredit", false, false))
        insert(DataType("Nomor BPJS", false, false))
        insert(DataType("Alamat Email", false, false))
    }


}