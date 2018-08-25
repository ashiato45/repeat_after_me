package com.example.ashia.filereadtest

import org.junit.Test
import kotlin.test.assertEquals


class LevenshteinTest {
    @Test
    fun TestCalcLevenshteinDistance1(){
        val res = calcLevenshteinDistance("kitten", "sitting");
        assertEquals(res, 3);
    }

    @Test
    fun TestNormalizeString1(){
        val s = "AlphAbet!"
        val t = "alphabet"
        assertEquals(normalizeString(s), t)
    }

}