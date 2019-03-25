package com.muhammadwahyudin.identitasku.utils

import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import org.jetbrains.anko.doAsync
import timber.log.Timber

/**
 * Singleton class of Database related utilities that are used on development
 */
object DbUtils {

    val DUMMY_DATAS = ArrayList<Data>().apply {
        add(Data(1, "3214011703940001"))
        add(Data(2, "085759216603", "Iphone"))
        add(Data(2, "085759216600", "Android", "IM3"))
        add(Data(3, "Jl. Tawakal Ujung Blok A5, Tomang, Jakarta Barat", "Kostan"))
        add(Data(3, "Jl. Tawakal Ujung Blok A2, Tomang, Jakarta Barat", "Rumah"))
        add(Data(3, "Jl. Tawakal Ujung Blok A1, Tomang, Jakarta Barat"))
        add(Data(4, "4574527457425"))
        add(Data(4, "4574527457421", "Kostan"))
        add(Data(5, "4257457"))
        add(Data(6, "24574545257"))
        add(Data(7, "9001234567", "Jenius"))
        add(Data(8, "321412515123132"))
        add(Data(9, "3353123124521"))
        add(Data(10, "904212345123"))
        add(Data(11, "4213515123"))
        add(Data(12, "contoso@gmail.com"))
    }

    /**
     * Pre-populate Data Type on [appDB]
     * @param appDB App database object
     */
    fun populateDataType(appDB: AppDatabase) {
        val dataTypeDao = appDB.dataTypeDao()
        doAsync {
            dataTypeDao.deleteAll()
            dataTypeDao.insert(DataType("Nomor KTP", true, false))
            dataTypeDao.insert(DataType("Nomor Handphone", false, false))
            dataTypeDao.insert(DataType("Alamat", false, false))
            dataTypeDao.insert(DataType("Nomor PLN", false, false))
            dataTypeDao.insert(DataType("Nomor PDAM", false, false))
            dataTypeDao.insert(DataType("Nomor NPWP", true, false))
            dataTypeDao.insert(DataType("Nomor Rekening Bank", false, false))
            dataTypeDao.insert(DataType("Nomor Kartu Keluarga", true, false))
            dataTypeDao.insert(DataType("Nomor STNK", false, false))
            dataTypeDao.insert(DataType("Nomor Kartu Kredit", false, false))
            dataTypeDao.insert(DataType("Nomor BPJS", false, false))
            dataTypeDao.insert(DataType("Alamat Email", false, false))
        }
        Timber.d("Prepopulated DB")
    }

    /**
     * Pre-populate Data on [appDB]
     * @param appDB App database object
     */
    fun populateData(appDB: AppDatabase) {
        Timber.d("Prepopulate Data")
        val dataDao = appDB.dataDao()
        doAsync {
            dataDao.deleteAll()
            DUMMY_DATAS.forEach {
                dataDao.insert(it)
            }
        }
        Timber.d("Prepopulated Data")
    }
}