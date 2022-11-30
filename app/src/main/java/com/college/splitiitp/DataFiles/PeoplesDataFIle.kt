package com.college.splitiitp.DataFiles

data class PeoplesDataFIle(val peoplelist:  ArrayList<peoplefile> = ArrayList()) {

    fun getpoeplelist(): ArrayList<peoplefile>{
        return peoplelist
    }
}
