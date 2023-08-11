package com.tim.lib.download

import com.google.gson.Gson
import com.tim.lib.download.network.StockData
import org.junit.Test

import org.junit.Assert.*

class ResponseTest {
    @Test
    fun addition_isCorrect() {
        //
        val json = """
            {
                "stat": "OK",
                "date": "20230811",
                "title": "112年08月 台積電 個股日本益比、殖利率及股價淨值比(以個股月查詢)",
                "fields": ["日期", "殖利率(%)", "股利年度", "本益比", "股價淨值比", "財報年/季"],
                "data": [
                    ["112年08月01日", "1.94", 111, "14.41", "4.78", "112/1"],
                    ["112年08月02日", "1.96", 111, "14.25", "4.73", "112/1"],
                    ["112年08月04日", "1.99", 111, "14.08", "4.67", "112/1"],
                    ["112年08月07日", "1.97", 111, "14.18", "4.70", "112/1"],
                    ["112年08月08日", "1.99", 111, "14.02", "4.65", "112/1"],
                    ["112年08月09日", "1.99", 111, "14.08", "4.67", "112/1"],
                    ["112年08月10日", "2.00", 111, "14.00", "4.64", "112/1"]
                ],
                "total": 7
            }
        """

        val gson = Gson()
        val item = gson.fromJson(json, StockData::class.java)

        assertEquals("OK", item.stat)
        assertEquals("20230811", item.date)
        assertEquals("112年08月 台積電 個股日本益比、殖利率及股價淨值比(以個股月查詢)", item.title)
        assertEquals(7, item.total)
    }
}