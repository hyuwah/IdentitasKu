package com.muhammadwahyudin.identitasku.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.muhammadwahyudin.identitasku.data.model.Data

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Data)

    @Update
    fun update(data: Data)

    @Delete
    fun delete(data: Data)

    @Query("SELECT * FROM data")
    fun getAll(): LiveData<List<Data>>

    @Query("SELECT * FROM data WHERE type_id == (:type)")
    fun getAllByType(type: Int): LiveData<List<Data>>
}