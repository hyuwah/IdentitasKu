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

    @Transaction
    fun reset() {
        deleteAll()
        resetAutoincrementId()
    }


}