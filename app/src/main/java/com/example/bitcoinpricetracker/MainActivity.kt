package com.example.bitcoinpricetracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lineChart = findViewById<LineChart>(R.id.lineChart)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis / 1000
        val endTime = System.currentTimeMillis() / 1000

        RetrofitClient.bitcoinApi.getBitcoinPrices("usd", startTime.toString(), endTime.toString(), "daily").enqueue(object : Callback<Map<String, List<List<Float>>>> {
            override fun onResponse(call: Call<Map<String, List<List<Float>>>>, response: Response<Map<String, List<List<Float>>>>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("prices") ?: return
                    val values = ArrayList<Entry>()

                    data.forEachIndexed { index, priceData ->
                        val xValue = (index + 1).toFloat()
                        val yValue = priceData[1]
                        values.add(Entry(xValue, yValue))
                    }

                    val dataSet = LineDataSet(values, "Bitcoin Price")
                    val lineData = LineData(dataSet)

                    lineChart.data = lineData
                    lineChart.invalidate()
                }
            }

            override fun onFailure(call: Call<Map<String, List<List<Float>>>>, t: Throwable) {
                Log.e("API_ERROR", "Ошибка при запросе: ${t.message}")
            }

        })
    }

    object RetrofitClient {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bitcoinApi: BitcoinApi = retrofit.create(BitcoinApi::class.java)
    }
}

