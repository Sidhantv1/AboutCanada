package com.example.aboutcanada

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutcanada.databinding.ViewItemBinding
import com.example.aboutcanada.dataclass.Row

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var arrayList = ArrayList<Row>()

    lateinit var dataListItemBinding: ViewItemBinding

    inner class ViewHolder(dataListItemBinding: ViewItemBinding) :
        RecyclerView.ViewHolder(dataListItemBinding.root) {

        fun bind() {
            itemView.apply {
                dataListItemBinding.data = arrayList[adapterPosition]
                Glide
                    .with(this)
                    .load(arrayList[adapterPosition].imageHref)
                    .placeholder(R.drawable.placeholder)
                    .into(dataListItemBinding.ivDisplayImage)
            }
        }
    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun setData(list: ArrayList<Row>) {
        this.arrayList = list
        notifyDataSetChanged()
    }
}