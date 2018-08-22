package com.example.ashia.filereadtest

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import java.io.File

class TextFragmentPagerAdapter(fm: FragmentManager?, file: File) : FragmentStatePagerAdapter(fm) {
    protected var texts: List<String> = file.readLines()

    init{
    }

    override fun getItem(position: Int): Fragment {
        return TextFragment.newInstance(texts[position]);
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return texts.size;
    }
}