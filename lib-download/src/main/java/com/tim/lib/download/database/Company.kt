package com.tim.lib.download.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "companies")
data class Company(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "company_id")
    val companyId: Long = 0,

    @ColumnInfo(name = "company_name")
    val companyName: String
)

@Entity(tableName = "company_data")
data class CompanyData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id")
    val dataId: Long = 0,

    @ColumnInfo(name = "company_id")
    val companyId: Long,

    @ColumnInfo(name = "data_date")
    val dataDate: String,

    @ColumnInfo(name = "data1")
    val data1: String,
)

@Database(entities = [Company::class, CompanyData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun companyDataDao(): CompanyDataDao
}
