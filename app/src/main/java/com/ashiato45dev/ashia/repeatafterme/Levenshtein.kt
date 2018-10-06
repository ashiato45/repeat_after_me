package com.ashiato45dev.ashia.repeatafterme

import kotlin.math.min

fun calcLevenshteinDistance(str1: String, str2: String): Int{
    var d: Array<Array<Int>> = Array(str1.length + 1) { i1 ->
        Array(str2.length + 1) { i2 ->
            if(i2 == 0){
                i1
            }else if(i1 == 0){
                i2
            }else{
                0
            }
        }
    };

    for(i1 in 1 .. str1.length){
        for(i2 in 1 .. str2.length){
            val cost = if(str1[i1 - 1] == str2[i2 - 1]) 0 else 1
            d[i1][i2] = min(d[i1-1][i2] + 1, min(d[i1][i2-1]+1, d[i1-1][i2-1]+cost));

        }
    }

    return d[str1.length][str2.length]

}

fun normalizeString(s: String): String{
    val regex = """[^a-z,0-9]""".toRegex()

    return regex.replace(s.toLowerCase(), "")
}

fun calcNormalizedLevenshteinDistance(str1: String, str2: String): Int{
    return calcLevenshteinDistance(normalizeString(str1), normalizeString(str2))

}