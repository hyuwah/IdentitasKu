package com.muhammadwahyudin.identitasku.data.repository

import androidx.lifecycle.LiveData
import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import org.jetbrains.anko.doAsync

class AppRepository(private val dataTypeDao: DataTypeDao, private val dataDao: DataDao) : IAppRepository {

    override fun insert(dataType: DataType) {
        doAsync {
            dataTypeDao.insert(dataType)
        }
    }

    override fun update(dataType: DataType) {
        doAsync {
            dataTypeDao.update(dataType)
        }
    }

    override fun delete(dataType: DataType) {
        doAsync {
            dataTypeDao.delete(dataType)
        }
    }

    override fun deleteAllDataType() {
        doAsync {
            dataTypeDao.deleteAll()
        }
    }

    override fun getAllDataType(): LiveData<List<DataType>> {
        return dataTypeDao.getAll()
    }

    override fun insert(data: Data) {
        doAsync {
            dataDao.insert(data)
        }
    }

    override fun update(data: Data) {
        doAsync {
            dataDao.update(data)
        }
    }

    override fun delete(data: Data) {
        doAsync {
            dataDao.delete(data)
        }
    }

    override fun deleteAllData() {
        doAsync {
            dataDao.deleteAll()
        }
    }

    override fun getAllData(): LiveData<List<Data>> {
        return dataDao.getAll()
    }

    override fun getAllDataByType(type: Int): LiveData<List<Data>> {
        return dataDao.getAllByType(type)
    }

    override fun resetDataType() {
        doAsync { dataTypeDao.reset() }
    }

    override fun prepopulateData() {
        doAsync { dataDao.prepopulateData() }
    }

    override fun getAllDataWithType(): LiveData<List<DataWithDataType>> {
        return dataDao.getAllWithDataType()
    }

    override fun getAllExistingUniqueType(): LiveData<List<DataType>> {
        return dataTypeDao.getAllExistingUniqueType()
    }
}