package com.muhammadwahyudin.identitasku.data.repository

import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

class AppRepository(private val dataTypeDao: DataTypeDao, private val dataDao: DataDao) :
    IAppRepository {

    override suspend fun insert(dataType: DataType) {
        dataTypeDao.insert(dataType)
    }

    override suspend fun update(dataType: DataType) {
        dataTypeDao.update(dataType)
    }

    override suspend fun delete(dataType: DataType) {
        dataTypeDao.delete(dataType)
    }

    override suspend fun deleteAllDataType() {
        dataTypeDao.deleteAll()
    }

    override suspend fun getAllDataType(): List<DataType> {
        return dataTypeDao.getAll()
    }

    override suspend fun insert(data: Data): Long {
        return dataDao.insert(data)
    }

    override suspend fun update(data: Data) {
        dataDao.update(data)
    }

    override suspend fun delete(data: Data) {
        dataDao.delete(data)
    }

    override suspend fun deleteById(id: Int) {
        dataDao.deleteById(id)
    }

    override suspend fun deleteDatasById(listOfId: List<Int>) {
        dataDao.deleteDatasById(listOfId)
    }

    override suspend fun deleteAllData() {
        dataDao.deleteAll()
    }

    override suspend fun getAllData(): List<Data> {
        return dataDao.getAll()
    }

    override suspend fun getAllDataByType(type: Int): List<Data> {
        return dataDao.getAllByType(type)
    }

    override suspend fun getDataById(id: Int): Data {
        return dataDao.getDataById(id)
    }

    override suspend fun resetDataType() {
        dataTypeDao.reset()
    }

    override suspend fun prepopulateData() {
        dataDao.prepopulateData()
    }

    override suspend fun getAllDataWithType(): List<DataWithDataType> {
        return dataDao.getAllWithDataType().asReversed()
    }

    override suspend fun getAllExistingUniqueType(): List<DataType> {
        return dataTypeDao.getAllExistingUniqueType()
    }
}