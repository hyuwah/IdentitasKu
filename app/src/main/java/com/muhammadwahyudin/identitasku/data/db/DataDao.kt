package com.muhammadwahyudin.identitasku.data.db

import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.utils.DbUtils

@Dao
abstract class DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: Data)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(data: Data)

    @Delete
    abstract suspend fun delete(data: Data)

    @Query("DELETE FROM data WHERE id = :id")
    abstract suspend fun deleteById(id: Int)

    @Transaction
    open suspend fun deleteDatasById(listOfId: List<Int>) {
        for (id in listOfId) {
            deleteById(id)
        }
    }

    @Query("SELECT * FROM data")
    abstract suspend fun getAll(): List<Data>

    @Query("SELECT * FROM data JOIN data_type ON data.type_id=data_type.type_id ")
    abstract suspend fun getAllWithDataType(): List<DataWithDataType>

    @Query("SELECT * FROM data WHERE type_id == (:type)")
    abstract suspend fun getAllByType(type: Int): List<Data>

    @Query("SELECT * FROM data WHERE id = :id")
    abstract suspend fun getDataById(id: Int): Data

    @Query("DElETE FROM data")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun prepopulateData() {
        DbUtils.DUMMY_DATAS.forEach {
            insert(it)
        }
    }
}