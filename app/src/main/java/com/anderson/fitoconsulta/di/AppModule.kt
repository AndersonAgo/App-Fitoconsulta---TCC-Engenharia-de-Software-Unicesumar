package com.anderson.fitoconsulta.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.anderson.fitoconsulta.database.FitoconsultaDatabase
import com.anderson.fitoconsulta.repository.PlantRepository
import com.anderson.fitoconsulta.screens.detail.PlantDetailViewModel
import com.anderson.fitoconsulta.screens.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import android.util.Log
import java.io.FileOutputStream
import androidx.sqlite.db.SupportSQLiteDatabase

val appModule = module {

    single {
        val dbName = "fitoconsulta.db"
        val context = androidContext()
        val dbFile = context.getDatabasePath(dbName)

        if (!dbFile.exists()) {
            Log.d("FitoconsultaDebug", "Banco de dados não encontrado. Tentando copiar dos assets...")
            try {
                val inputStream = context.assets.open(dbName)
                val outputStream = FileOutputStream(dbFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                Log.d("FitoconsultaDebug", "Banco de dados copiado")
            } catch (e: Exception) {
                Log.e("FitoconsultaDebug", "erro ao copiar o banco de dados", e)
                throw RuntimeException("Erro ao copiar o banco de dados ja populado", e)
            }
        } else {
            Log.d("FitoconsultaDebug", "banco de dados ja existe")
        }

        val callback = object : AndroidSqliteDriver.Callback(FitoconsultaDatabase.Schema) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                Log.d("FitoconsultaDebug", "Driver onCreate chamado, mas nada foi feito poruqe o DB já está populado.")
            }
        }
        val driver = AndroidSqliteDriver(
            schema = FitoconsultaDatabase.Schema,
            context = context,
            name = dbName,
            callback = callback
        )
        FitoconsultaDatabase(driver)
    }
    single {
        val database = get<FitoconsultaDatabase>()
        database.fitoconsultaQueries
    }
    single { PlantRepository(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { params -> PlantDetailViewModel(savedStateHandle = params.get(), repository = get()) }
}