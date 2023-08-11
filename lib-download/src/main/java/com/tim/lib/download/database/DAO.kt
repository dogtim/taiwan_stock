package com.tim.lib.download.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room


@Dao
interface CompanyDao {
    @Insert
    suspend fun insertCompany(company: Company)

    @Query("SELECT * FROM companies")
    suspend fun getAllCompanies(): List<Company>
}

@Dao
interface CompanyDataDao {
    @Insert
    suspend fun insertCompanyData(companyData: CompanyData)

    @Query("SELECT * FROM company_data WHERE company_id = :companyId")
    suspend fun getCompanyDataByCompanyId(companyId: Long): List<CompanyData>
}

class DataBaseManager(applicationContext: Context) {
    init {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-stock"
        ).build()
    }
}

