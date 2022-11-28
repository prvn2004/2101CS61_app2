package com.college.splitiitp.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.college.splitiitp.Adapter.ExpensesAdapter
import com.college.splitiitp.Adapter.MainAdapter
import com.college.splitiitp.DataFiles.ExpensesDataFile
import com.college.splitiitp.DataFiles.MainDataFile
import com.college.splitiitp.R
import com.college.splitiitp.databinding.FragmentGroupBinding
import com.college.splitiitp.fragments.MainFragment.Companion.isConnectionAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GroupFragment : Fragment() {
    var docid: String? = ""
    private lateinit var binding: FragmentGroupBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var ExpensesLinkModel: ArrayList<ExpensesDataFile>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(inflater, container, false)
        val view = binding.root
        ExpensesLinkModel = arrayListOf()

        docid = requireArguments().getString("Docid")

        ImplementFun()

        return view
    }

    fun ImplementFun() {
        if (GroupFragment.isConnectionAvailable(requireActivity())) {
            getGrpName()
            getExpenses()
        } else {
            setupAnim()
        }
    }

    private fun setupAnim() {
        binding.animationView.setAnimation(R.raw.no_internet)
        binding.animationView.repeatCount = LottieDrawable.INFINITE
        binding.animationView.playAnimation()
    }

    private fun getExpenses() {

        val tsLong = System.currentTimeMillis()
        val ts = tsLong / 1000

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        val firestore = FirebaseFirestore.getInstance()
        val collectionReference =
            firestore.collection("Groups").document(docid.toString()).collection("Expenses")

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            Log.d("DATA", value.toObjects(MainDataFile::class.java).toString())
            ExpensesLinkModel.clear()
            ExpensesLinkModel.addAll(value.toObjects(ExpensesDataFile::class.java))
            recyclerView = binding.recyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = ExpensesAdapter(ExpensesLinkModel)

            recyclerView.visibility = View.VISIBLE
        }
    }

    fun getGrpName() {
        val firestore = FirebaseFirestore.getInstance()
        val email =
            PreferenceManager.getDefaultSharedPreferences(activity).getString("Email", "")

        val collectionReference =
            firestore.collection("Groups").document(docid.toString())
        collectionReference.addSnapshotListener { value, error ->
            val grpName = value?.get("personMade").toString()
            val shares = value?.get("praveen1142004@gmail.com").toString()
            binding.GrpName.text = grpName
            binding.contributionText.text = shares

            val email =
                PreferenceManager.getDefaultSharedPreferences(activity).getString("Email", "")
            binding.shareText.text = shares

            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
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