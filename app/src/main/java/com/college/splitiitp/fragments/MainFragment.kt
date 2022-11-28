package com.college.splitiitp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.college.splitiitp.Activities.LoginActivity
import com.college.splitiitp.Adapter.MainAdapter
import com.college.splitiitp.DataFiles.MainDataFile
import com.college.splitiitp.R
import com.college.splitiitp.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var GroupsLinkModel: ArrayList<MainDataFile>
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        GroupsLinkModel = arrayListOf()
        auth = FirebaseAuth.getInstance()

        allFunctionImplementation()

        return view
    }

    private fun allFunctionImplementation() {
        if (isConnectionAvailable(requireActivity())) {
            getGroups()
            logoutbutton()
        } else {
            setupAnim()
        }
    }

    private fun logoutbutton(){
        binding.logoutbutton.setOnClickListener {
            showDialoglogout()
        }
    }

    private fun showDialoglogout() {
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Log Out")

        dialogBuilder.setMessage("Do you want to Log out")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                signout()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun signout(){
        auth.signOut()

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun emptylist(){
        binding.animationView.setAnimation(R.raw.no_data)
        binding.animationView.repeatCount = LottieDrawable.INFINITE
        binding.animationView.playAnimation()
    }

    private fun getGroups() {

        val tsLong = System.currentTimeMillis()
        val ts = tsLong / 1000

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        firestore = FirebaseFirestore.getInstance()
        val collectionReference =
            firestore.collection("Groups")

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (value.isEmpty){
                emptylist()
            }
            Log.d("DATA", value.toObjects(MainDataFile::class.java).toString())
            GroupsLinkModel.clear()
            GroupsLinkModel.addAll(value.toObjects(MainDataFile::class.java))
            recyclerView = binding.recyclerview
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
            recyclerView.adapter = MainAdapter(GroupsLinkModel)

            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupAnim() {
        binding.animationView.setAnimation(R.raw.no_internet)
        binding.animationView.repeatCount = LottieDrawable.INFINITE
        binding.animationView.playAnimation()
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