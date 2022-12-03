package com.college.splitiitp.DataFiles

import java.security.cert.CertPathValidatorException

data class ExpensesDataFile(
    val personMade: String = "",
    val totalBill: Long = 0,
    val reason: String = "",
    val Share:HashMap<String, Long> = HashMap(),
    val Contribution: HashMap<String, Long> = HashMap()
) {

    fun getshares():HashMap<String, Long>{
        return Share
    }
    fun getPerson(): String {
        return personMade
    }

    fun getTotalbillfor(): Long {
        return totalBill
    }

    fun getExpReason(): String {
        return reason
    }
}
