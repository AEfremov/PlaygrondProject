package ru.efremov.playgrondproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var pieChartView: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pieChartView = findViewById(R.id.pie_chart)

        val data = PieData()
        data.add("Sid", 18.0, "#4286f4")
        data.add("Nick", 4.0, "#44a837")
        data.add("Jack", 6.0, "#a5a5a5")
        data.add("Dave", 10.0, "#8e4f1c")

        pieChartView.setData(data)
    }
}