package com.college.splitiitp.DataFiles

import java.security.cert.CertPathValidatorException

data class ExpensesDataFile(
    val personMade: String = "",
    val totalBill: String = "",
    val reason: String = "",
    val Share:HashMap<String, Long> = HashMap()
) {
    fun getshares():HashMap<String, Long>{
        return Share
    }
    fun getPerson(): String {
        return personMade
    }

    fun getTotalbillfor(): String {
        return totalBill
    }

    fun getExpReason(): String {
        return reason
    }
}
