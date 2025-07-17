package com.muhammadwahyudin.identitasku.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.data.repository.AppRepository
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.utils.DbUtils
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, Constants.DB_NAME)
            .openHelperFactory(
                SupportOpenHelperFactory(
                    Hawk.get(Constants.SP_PASSWORD, "123456").encodeToByteArray()
                )
            )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val appDB = get<AppDatabase>()
                    GlobalScope.launch {
                        DbUtils.populateDataType(appDB)
                        if (BuildConfig.DEBUG) DbUtils.populateData(appDB)
                    }
                }
            })
            .build()
    }

    single { get<AppDatabase>().dataTypeDao() }
    single { get<AppDatabase>().dataDao() }
    single<IAppRepository> { AppRepository(get(), get()) }
}