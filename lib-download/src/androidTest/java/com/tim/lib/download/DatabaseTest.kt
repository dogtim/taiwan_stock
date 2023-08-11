package com.tim.lib.download

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tim.lib.download.database.AppDatabase
import com.tim.lib.download.database.Company
import com.tim.lib.download.database.CompanyDao
import com.tim.lib.download.database.CompanyData
import com.tim.lib.download.database.CompanyDataDao
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var companyDao: CompanyDao
    private lateinit var companyDataDao: CompanyDataDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        companyDao = database.companyDao()
        companyDataDao = database.companyDataDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveCompanies() = runBlocking {
        val company = Company(companyName = "Company A")
        companyDao.insertCompany(company)

        val companies = companyDao.getAllCompanies()
        assertEquals(1, companies.size)
        assertEquals("Company A", companies[0].companyName)
    }

    @Test
    fun testInsertAndRetrieveCompanyData() = runBlocking {
        val company = Company(companyName = "Company B")
        companyDao.insertCompany(company)

        val companyData = CompanyData(
            companyId = company.companyId,
            dataDate = "2023-08-11",
            data1 = "Data 1"
        )
        companyDataDao.insertCompanyData(companyData)

        val retrievedData = companyDataDao.getCompanyDataByCompanyId(company.companyId)
        assertEquals(1, retrievedData.size)
        assertEquals("2023-08-11", retrievedData[0].dataDate)
        assertEquals("Data 1", retrievedData[0].data1)
    }
}