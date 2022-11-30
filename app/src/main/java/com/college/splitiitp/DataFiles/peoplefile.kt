package com.college.splitiitp.DataFiles

import android.text.Editable

data class peoplefile(val people: String, var share: Number){

    fun getperson(): String{
        return people
    }
    fun giveshare (money: Number){
        share=money
    }
    fun getshare(): Number{
        return share
    }
}
