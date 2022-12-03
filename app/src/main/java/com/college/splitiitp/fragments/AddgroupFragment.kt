package com.college.splitiitp.fragments

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.college.splitiitp.MainActivity
import com.college.splitiitp.databinding.FragmentAddgroupBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddgroupFragment : Fragment() {
    private lateinit var binding: FragmentAddgroupBinding
    val peoples: ArrayList<String> = ArrayList()
    val share: HashMap<String, Long> = HashMap()
    val contribution : HashMap<String, Long> = HashMap()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddgroupBinding.inflate(inflater, container, false)
        val view = binding.root

        ImplementFun()

        return view
    }


    fun ImplementFun() {
        if (AddexpenseFragment.isConnectionAvailable(requireActivity())) {
            addingpeople()
            submittinggrp()
        } else {
        }
    }

    fun submittinggrp() {
        binding.submit.setOnClickListener {
            val reason = binding.reason.text.toString()
            addgroup(reason, peoples)
        }
    }

    fun addingpeople() {
        binding.addperson.setOnClickListener {
            val person = binding.emailAddress.text.toString()
            peoples.add(person)
            share.put(person, 0)
            contribution.put(person, 0)
            Toast.makeText(activity, "added person -> $person", Toast.LENGTH_SHORT).show()
            binding.emailAddress.text.clear()
            Log.d("hello", peoples.toString())
        }
    }

    fun addgroup(grpReason: String, poeples: ArrayList<String>) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Adding Group")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val tsLong = System.currentTimeMillis()
        val ts = tsLong

        val date: String = DateFormat.format("dd-MM-yyyy hh:mm:ss", ts).toString()

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct.displayName.toString()
            val personEmail = acct.email.toString()

            val fireStoreDatabase = FirebaseFirestore.getInstance()

            val hashMap = hashMapOf<String, Any>(
                "groupReason" to grpReason,
                "groupTime" to date,
                "personMade" to personName,
                "peoples" to poeples,
                "Share" to share,
                "Contribution" to contribution
            )

            fireStoreDatabase.collection("Groups")
                .add(hashMap)
                .addOnSuccessListener {
                    Log.d("hey", "Added document with ID ${it}")
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error adding document $exception")
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
        }
    }

    companion object {

        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                    && netInfo.isConnectedOrConnecting
                    && netInfo.isAvailable
                ) {
                    return true
                }
            }
            return false
        }
    }
}