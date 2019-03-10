package com.muhammadwahyudin.identitasku

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.facebook.stetho.Stetho
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.repository.AppRepository
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModelFactory
import com.muhammadwahyudin.identitasku.utils.Commons
import com.orhanobut.hawk.Hawk
import org.jetbrains.anko.doAsync
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*
import timber.log.Timber
import timber.log.Timber.DebugTree


class IdentitasKuApp : MultiDexApplication(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<AppDatabase>() with eagerSingleton {
            Room.databaseBuilder(this@IdentitasKuApp, AppDatabase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration()
                .openHelperFactory(SafeHelperFactory(Commons.getDeviceId(applicationContext).toCharArray()))
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
        Hawk.init(this).build()
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