package com.wallpaper.fullhd.clocktest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var switcher: Switch
    private lateinit var clockLayout1: LinearLayout
    private lateinit var clockLayout2: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switcher = findViewById(R.id.switcher)
        clockLayout1 = findViewById(R.id.clock_layout_1)
        clockLayout2 = findViewById(R.id.clock_layout_2)

        val clockView = findViewById<ClockView>(R.id.clock_view2)
        clockView.setHourHandColor(Color.RED)
        clockView.setBorderColor(Color.RED)

        switcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                clockLayout1.visibility = View.GONE
                clockLayout2.visibility = View.VISIBLE
            } else {
                clockLayout1.visibility = View.VISIBLE
                clockLayout2.visibility = View.GONE
            }
        }
    }
}