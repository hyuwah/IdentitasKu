package com.muhammadwahyudin.identitasku.data.repository

import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

interface IAppRepository {

    suspend fun insert(dataType: DataType)
    suspend fun update(dataType: DataType)
    suspend fun delete(dataType: DataType)
    suspend fun deleteAllDataType()
    suspend fun getAllDataType(): List<DataType>
    suspend fun resetDataType()
    suspend fun getAllExistingUniqueType(): List<DataType>

    suspend fun insert(data: Data)
    suspend fun update(data: Data)
    suspend fun delete(data: Data)
    suspend fun deleteById(id: Int)
    suspend fun deleteDatasById(listOfId: List<Int>)
    suspend fun deleteAllData()
    suspend fun getAllData(): List<Data>
    suspend fun getAllDataByType(type: Int): List<Data>
    suspend fun getAllDataWithType(): List<DataWithDataType>
    suspend fun getDataById(id: Int): Data
    suspend fun prepopulateData()
}