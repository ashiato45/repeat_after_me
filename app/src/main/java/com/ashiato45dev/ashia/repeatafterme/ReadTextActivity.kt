package com.ashiato45dev.ashia.repeatafterme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import java.io.File

class ReadTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_text)


        val filepath = intent.getStringExtra("filepath");
        val file =  File(filepath);
        this.title = file.name;

        val vp = findViewById<ViewPager>(R.id.vpMain);
        val efpa = TextFragmentPagerAdapter(supportFragmentManager, file);
        vp.adapter = efpa;

    }
}
