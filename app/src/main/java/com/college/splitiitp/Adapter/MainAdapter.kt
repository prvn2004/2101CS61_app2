package com.college.splitiitp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.college.splitiitp.MainActivity
import com.college.splitiitp.DataFiles.MainDataFile
import com.college.splitiitp.R
import com.college.splitiitp.databinding.GroupsListItemBinding
import com.college.splitiitp.fragments.GroupFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainAdapter(
    private var LinkList: ArrayList<MainDataFile>
) :

    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: GroupsListItemBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    var totalshare: Long = 0

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapter.MyViewHolder {

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding =
            GroupsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MainAdapter.MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: MainAdapter.MyViewHolder,
        position: Int
    ) {
        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)

        val docid = Link.getDocid().toString()



        val email = PreferenceManager.getDefaultSharedPreferences(holder.itemView.context).getString("Email", "");
        val TotalShare = Link.getShares()
        val myshare= TotalShare!!.get(email).toString()

        for (item in LinkList){
            totalshare+= item.getShares()?.get(email)!!
        }

        binding.myShare.text = "₹$myshare"

        binding.grpbutton.setOnClickListener {
            fragmentJump(it.context, docid)
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

    override fun getItemCount(): Int {
        return LinkList.size
    }

    class MyViewHolder(
        ItemViewBinding: GroupsListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: MainDataFile, position: Int, list: ArrayList<MainDataFile>) {

            val context = itemView.getContext()

            binding.owner.text = Link.getPerson()
            binding.Reason.text = Link.getReason()
            binding.Time.text = Link.getTime()

        }
    }
}
