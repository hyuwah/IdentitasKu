package com.muhammadwahyudin.identitasku.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Data)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(data: Data)

    @Delete
    fun delete(data: Data)

    @Query("DELETE FROM data WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM data")
    fun getAll(): LiveData<List<Data>>

    @Query("SELECT * FROM data JOIN data_type ON data.type_id=data_type.type_id ")
    fun getAllWithDataType(): LiveData<List<DataWithDataType>>

    @Query("SELECT * FROM data WHERE type_id == (:type)")
    fun getAllByType(type: Int): LiveData<List<Data>>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getDataById(id: Int): LiveData<Data>

    @Query("DElETE FROM data")
    fun deleteAll()

    @Transaction
    fun prepopulateData() {
        insert(Data(1, "3214011703940001"))
        insert(Data(2, "085759211234"))
        insert(Data(3, "Jl Tawakal, Jakarta Barat"))
        insert(Data(4, "868"))
        insert(Data(5, "56865858"))
        insert(Data(6, "6858"))
        insert(Data(12, "example@mail.com"))
    }
}