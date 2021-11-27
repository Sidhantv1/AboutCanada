package com.example.aboutcanada.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutcanada.R
import com.example.aboutcanada.databinding.ViewItemBinding
import com.example.aboutcanada.dataclass.Row

/**
 * Main Adapter to display the facts data
 */
class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    // variable used for adding data to the arraylist of type Row
    var arrayList = ArrayList<Row>()

    // variable for the data binding of the MainAdapter's layout
    lateinit var dataListItemBinding: ViewItemBinding

    /**
     * View Holder
     */
    inner class ViewHolder(dataListItemBinding: ViewItemBinding) :
        RecyclerView.ViewHolder(dataListItemBinding.root) {

        fun bind() {
            itemView.apply {
                // setting data on the data binding object
                dataListItemBinding.data = arrayList[adapterPosition]
                // Used Glide to set image on the view
                Glide
                    .with(this)
                    .load(arrayList[adapterPosition].imageHref)
                    .placeholder(R.drawable.placeholder)
                    .into(dataListItemBinding.ivDisplayImage)
            }
        }
    }

    /**
     * Create View Holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataListItemBinding: ViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.view_item,
            parent,
            false
        )
        this.dataListItemBinding = dataListItemBinding
        return ViewHolder(dataListItemBinding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * Bind View Holder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    /**
     * Set Data fetched from the api response to the adapter
     */
    fun setData(list: ArrayList<Row>) {
        this.arrayList = list
        notifyDataSetChanged()
    }
}