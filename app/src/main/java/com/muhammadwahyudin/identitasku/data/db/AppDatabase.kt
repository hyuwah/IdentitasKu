package com.muhammadwahyudin.identitasku.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType

@Database(entities = [Data::class, DataType::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataTypeDao(): DataTypeDao
    abstract fun dataDao(): DataDao
}