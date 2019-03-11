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
            dataDao.insert(Data(1, "3214011703940001"))
            dataDao.insert(Data(2, "085759216600"))
            dataDao.insert(Data(3, "Jl. Tawakal Ujung Blok A5, Tomang, Jakarta Barat"))
            dataDao.insert(Data(3, "Jl. Tawakal Ujung Blok A2, Tomang, Jakarta Barat"))
            dataDao.insert(Data(3, "Jl. Tawakal Ujung Blok A1, Tomang, Jakarta Barat"))
            dataDao.insert(Data(3, "Jl. Tawakal Ujung Blok A7, Tomang, Jakarta Barat"))
            dataDao.insert(Data(3, "Jl. Tawakal Ujung Blok A4, Tomang, Jakarta Barat"))
            dataDao.insert(Data(4, "4574527457425"))
            dataDao.insert(Data(5, "4257457"))
            dataDao.insert(Data(6, "24574545257"))
            dataDao.insert(Data(7, "9001234567", "Jenius"))
            dataDao.insert(Data(2, "contoso@gmail.com"))
        }
        Timber.d("Prepopulated Data")
    }
}