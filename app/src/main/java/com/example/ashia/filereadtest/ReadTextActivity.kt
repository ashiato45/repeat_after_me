package com.example.ashia.filereadtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import java.io.File

class ReadTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_text)

        val filepath = intent.getStringExtra("filepath");

        val vp = findViewById<ViewPager>(R.id.vpMain);
        val efpa = TextFragmentPagerAdapter(supportFragmentManager, File(filepath));
        vp.adapter = efpa;

    }
}
