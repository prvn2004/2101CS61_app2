package com.college.splitiitp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.splitiitp.Adapter.ExpensePeopleShow
import com.college.splitiitp.DataFiles.peoplefile
import com.college.splitiitp.databinding.FragmentAddexpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddexpenseFragment : Fragment() {
    private lateinit var binding: FragmentAddexpenseBinding
    var docid: String? = ""
    lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private var PeopleLinkModel: ArrayList<peoplefile> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddexpenseBinding.inflate(inflater, container, false)
        val view = binding.root
        docid = requireArguments().getString("docid")

        ImplementFun()

        return view
    }

    fun ImplementFun() {
        if (AddexpenseFragment.isConnectionAvailable(requireActivity())) {
            getExpenses()
            updateexpense()
        } else {
        }
    }

    private fun getExpenses() {

        val tsLong = System.currentTimeMillis()
        val ts = tsLong / 1000

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        val firestore = FirebaseFirestore.getInstance()
        val collectionReference =
            firestore.collection("Groups").document(docid.toString())

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val peoplelist: ArrayList<String>? = value.get("peoples") as ArrayList<String>?
            if (peoplelist != null) {
                for (item in peoplelist){
                    val firstpeople = peoplefile(people = item, share = 0)
                    PeopleLinkModel.add(firstpeople)
                }
            }
            Log.d("hey1", PeopleLinkModel.toString())
            recyclerView = binding.recyclerviewmain
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = ExpensePeopleShow(PeopleLinkModel, docid)

            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateexpense(){
        binding.submit.setOnClickListener {
            val personMade = binding.paidbyname.text.toString()
            val totalbill = binding.amount.text.toString().toInt()
            val reason = binding.whatfor.text.toString()
            if (adapterhandler != null) {
                adapterhandler!!.updateText(personMade, reason, totalbill);
            }
        }
    }

    abstract class AdapterHandler {
        open fun updateText(personMade: String,  reason: String, totalbill: Number) {}
    }


    companion object {
        var adapterhandler: AdapterHandler? = null

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