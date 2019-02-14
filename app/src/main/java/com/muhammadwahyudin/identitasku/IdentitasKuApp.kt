package com.muhammadwahyudin.identitasku

import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.db.DataDao
import com.muhammadwahyudin.identitasku.data.db.DataTypeDao
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
                        doAsync {
                            val dataTypeDao = instance<AppDatabase>().dataTypeDao()
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
                            Log.d("DB", "Prepopulated DB")
                        }
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

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())


    }
}