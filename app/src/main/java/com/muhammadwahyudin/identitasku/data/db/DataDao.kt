package com.muhammadwahyudin.identitasku.data.db

import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.utils.DbUtils

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Data): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: Data)

    @Delete
    suspend fun delete(data: Data)

    @Query("DELETE FROM data WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Transaction
    suspend fun deleteDatasById(listOfId: List<Int>) {
        for (id in listOfId) {
            deleteById(id)
        }
    }

    @Query("SELECT * FROM data")
    suspend fun getAll(): List<Data>

    @Query("SELECT * FROM data JOIN data_type ON data.type_id=data_type.type_id ")
    suspend fun getAllWithDataType(): List<DataWithDataType>

    @Query("SELECT * FROM data WHERE type_id == (:type)")
    suspend fun getAllByType(type: Int): List<Data>

    @Query("SELECT * FROM data WHERE id = :id")
    suspend fun getDataById(id: Int): Data

    @Query("DElETE FROM data")
    suspend fun deleteAll()

    @Transaction
    suspend fun prepopulateData() {
        DbUtils.DUMMY_DATAS.forEach {
            insert(it)
        }
    }
}