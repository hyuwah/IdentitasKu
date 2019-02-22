package com.muhammadwahyudin.identitasku

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.facebook.stetho.Stetho
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.repository.AppRepository
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModelFactory
import org.jetbrains.anko.doAsync
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*
import timber.log.Timber
import timber.log.Timber.DebugTree


class IdentitasKuApp : MultiDexApplication(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<AppDatabase>() with eagerSingleton {
            Room.databaseBuilder(this@IdentitasKuApp, AppDatabase::class.java, "identitasku-db")
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val appDB = instance<AppDatabase>()

                        populateDataType(appDB)
                        populateData(appDB)


                    }
                })
                .build()
        }
        bind<DataTypeDao>() with singleton { instance<AppDatabase>().dataTypeDao() }
        bind<DataDao>() with singleton { instance<AppDatabase>().dataDao() }
        bind<IAppRepository>() with singleton { AppRepository(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Stetho.initializeWithDefaults(this)
        }

    }

    /**
     * DB Dev Helpers
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

    fun populateData(appDB: AppDatabase) {
        Timber.d("Prepopulate Data")
        val dataDao = appDB.dataDao()
        doAsync {
            dataDao.deleteAll()
            dataDao.insert(Data(1, "931561375618"))
            dataDao.insert(Data(2, "245645657"))
            dataDao.insert(Data(3, "4526577"))
            dataDao.insert(Data(3, "75452"))
            dataDao.insert(Data(3, "464623565"))
            dataDao.insert(Data(3, "245725475"))
            dataDao.insert(Data(3, "745272457457"))
            dataDao.insert(Data(4, "4574527457425"))
            dataDao.insert(Data(5, "4257457"))
            dataDao.insert(Data(6, "24574545257"))
            dataDao.insert(Data(7, "245744575474"))
            dataDao.insert(Data(2, "2457425724574257"))
        }
        Timber.d("Prepopulated Data")
    }
}