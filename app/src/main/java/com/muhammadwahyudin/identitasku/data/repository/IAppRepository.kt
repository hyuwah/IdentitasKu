package com.muhammadwahyudin.identitasku.data.repository

import androidx.lifecycle.LiveData
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType

interface IAppRepository {

        fun insert(dataType: DataType)
        fun update(dataType: DataType)
        fun delete(dataType: DataType)
        fun deleteAllDataType()
        fun getAllDataType(): LiveData<List<DataType>>

        fun insert(data: Data)
        fun update(data: Data)
        fun delete(data: Data)
        fun deleteAllData()
        fun getAllData(): LiveData<List<Data>>
        fun getAllDataByType(type: Int): LiveData<List<Data>>
        fun resetDataType()



}