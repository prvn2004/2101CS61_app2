package com.college.splitiitp.DataFiles

import com.google.firebase.firestore.DocumentId
import com.google.firebase.storage.StorageReference

data class MainDataFile(
    val personMade: String = "",
    val groupReason: String = "",
    val groupTime: String = "",
    val Contribution: HashMap<String, Long>? = HashMap(),
    val Share: HashMap<String, Long>? = HashMap(),

    @DocumentId
    val documentId: String = ""
) {
    fun getShares(): HashMap<String, Long>?{
        return Share
    }

    fun getContributions(): HashMap<String, Long>?{
        return Contribution
    }

    fun getPerson(): String {
        return personMade
    }

    fun getReason(): String {
        return groupReason
    }

    fun getTime(): String {
        return groupTime
    }
    fun getDocid(): String{
        return documentId
    }
}
