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
            dataTypeDao.insert(DataType("Nomor KTP", true, isCustom = false))
            dataTypeDao.insert(DataType("Nomor Handphone", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Alamat", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor PLN", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor PDAM", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor NPWP", isUnique = true, isCustom = false))
            dataTypeDao.insert(DataType("Nomor Rekening Bank", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor Kartu Keluarga", isUnique = true, isCustom = false))
            dataTypeDao.insert(DataType("Nomor STNK", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor Kartu Kredit", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Nomor BPJS", isUnique = false, isCustom = false))
            dataTypeDao.insert(DataType("Alamat Email", isUnique = false, isCustom = false))
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