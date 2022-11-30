package com.college.splitiitp.Adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.college.splitiitp.DataFiles.peoplefile
import com.college.splitiitp.MainActivity
import com.college.splitiitp.R
import com.college.splitiitp.databinding.AddExpenseListItemBinding
import com.college.splitiitp.fragments.AddgrpFragment
import com.college.splitiitp.fragments.AddgrpFragment.Companion.adapterhandler
import com.college.splitiitp.fragments.GroupFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class GrpPeopleAdd(
    private var LinkList: ArrayList<peoplefile>, val docid: String?
) :

    RecyclerView.Adapter<GrpPeopleAdd.MyViewHolder>() {
// ------------------------------------------------------------------------------------------------------------------------------------------

    var docid1: String? = docid
    private lateinit var binding: AddExpenseListItemBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrpPeopleAdd.MyViewHolder {

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding =
            AddExpenseListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return GrpPeopleAdd.MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: GrpPeopleAdd.MyViewHolder,
        position: Int
    ) {
        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)

        AddgrpFragment.adapterhandler = object : AddgrpFragment.AdapterHandler() {
            override fun updateText(personMade: String, reason: String, totalbill: Number) {
                val x = binding.amount.text.toString().toInt()
                Link.giveshare(x)

                Toast.makeText(holder.itemView.context, "hello clicked", Toast.LENGTH_SHORT).show()

                val map = HashMap<String, Any>()

                val k: ArrayList<peoplefile> = LinkList

                for (item in k) {
                    val person = item.getperson().toString()
                    val money = item.getshare().toInt()

                    map.put(person, money)
                }
                val tsLong = System.currentTimeMillis()
                editdoc(personMade, reason, totalbill, map, tsLong)
                if (docid != null) {
                    fragmentJump(holder.itemView.context, docid)
                }
            }
        }


    }
    @SuppressLint("SuspiciousIndentation")
    private fun fragmentJump(context: Context, Docid: String) {
        val  mFragment = GroupFragment()
        switchContent(R.id.main_container, mFragment, context, Docid)
    }

    private fun switchContent(id: Int, fragment: Fragment, context: Context, Docid: String) {
        if (context is MainActivity) {
            val enotesActivity = context as MainActivity
            val frag: Fragment = fragment
            enotesActivity.switchContent(id, frag, Docid)
        }
    }

    fun editdoc(
        personMade: String, reason: String, totalBill: Number,
        Share: HashMap<String, Any>, timestamp: Long
    ) {
        val fireStoreDatabase = FirebaseFirestore.getInstance()

        val hashMap = hashMapOf<String, Any>(
            "personMade" to personMade,
            "reason" to reason,
            "totalBill" to totalBill,
            "Share" to Share,
            "timestamp" to timestamp
        )

        fireStoreDatabase.collection("Groups").document(docid.toString()).collection("Expenses")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d("hey", "Added document with ID ${it}")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error adding document $exception")
            }
    }

    override fun getItemCount(): Int {
        return LinkList.size
    }

    class MyViewHolder(
        ItemViewBinding: AddExpenseListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding

        @SuppressLint("SetTextI18n")
        fun friction(Link: peoplefile, position: Int, list: ArrayList<peoplefile>) {

            val context = itemView.getContext()
            binding.PersonName.text = Link.getperson().toString()
        }
    }
}
