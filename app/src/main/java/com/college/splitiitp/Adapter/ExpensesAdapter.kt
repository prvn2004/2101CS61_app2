package com.college.splitiitp.Adapter

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.splitiitp.DataFiles.ExpensesDataFile
import com.college.splitiitp.databinding.ExpenseListItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ExpensesAdapter(
    private var LinkList: ArrayList<ExpensesDataFile>
) :

    RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>() {
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: ExpenseListItemBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensesAdapter.MyViewHolder {

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding =
            ExpenseListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ExpensesAdapter.MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: ExpensesAdapter.MyViewHolder,
        position: Int
    ) {
        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)

      val email = PreferenceManager.getDefaultSharedPreferences(holder.itemView.context).getString("Email", "");

        val sharemap= Link.getshares()
//        Log.d("hello", sharemap.toString())
        val valueOfElement = sharemap.get(email).toString()

        binding.yourShareMoney.text = "₹$valueOfElement"



    }

//    private fun fragmentJump(context: Context, Docid: String) {
//        val  mFragment = GroupFragment()
//        switchContent(R.id.main_container, mFragment, context, Docid)
//    }
//
//    private fun switchContent(id: Int, fragment: Fragment, context: Context, Docid: String) {
//        if (context is MainActivity) {
//            val enotesActivity = context as MainActivity
//            val frag: Fragment = fragment
//            enotesActivity.switchContent(id, frag, Docid)
//        }
//    }

    override fun getItemCount(): Int {
        return LinkList.size
    }

    class MyViewHolder(
        ItemViewBinding: ExpenseListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        @SuppressLint("SetTextI18n")
        fun friction(Link: ExpensesDataFile, position: Int, list: ArrayList<ExpensesDataFile>) {

            val context = itemView.getContext()
            binding.PersonName.text = Link.getPerson().toString()
            binding.reason.text = Link.getExpReason().toString()
            binding.totalBillMoney.text  = "₹" + Link.getTotalbillfor().toString()
        }
    }
}
