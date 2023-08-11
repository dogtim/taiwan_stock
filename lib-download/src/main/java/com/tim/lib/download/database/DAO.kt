package com.tim.lib.download.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


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
