package com.college.splitiitp

import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieDrawable
import com.college.splitiitp.databinding.ActivityMainBinding
import com.college.splitiitp.fragments.MainFragment
import com.google.firebase.firestore.DocumentId

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setFragment(MainFragment())

        setContentView(view)
    }

    private fun setFragment(fragment: Fragment) {
        val fram =  supportFragmentManager.beginTransaction()
        fram.replace(R.id.main_container, fragment)
        fram.commit()
    }
    fun switchContent(id: Int, fragment: Fragment, Docid: String) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putString("Docid", Docid)
        fragment.setArguments(args)
        ft.replace(id, fragment, fragment.toString())
        ft.addToBackStack(null)
        ft.commit()
    }
}
