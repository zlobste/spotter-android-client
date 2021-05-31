package org.zlobste.spotter.features.my_drivers.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver_item.view.*
import org.zlobste.spotter.R
import org.zlobste.spotter.base.BaseAdapter
import org.zlobste.spotter.databinding.LayoutDriverItemBinding
import org.zlobste.spotter.features.my_drivers.model.Driver

class DriversAdapter(val context: Context) :
    BaseAdapter<Driver, DriversAdapter.DriversViewHolder>() {
    var onInfoIconClicked: ((Driver) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversViewHolder =
        DriversViewHolder(
            LayoutDriverItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DriversViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.info_icon.setOnClickListener {
            onInfoIconClicked?.invoke(getItem(position))
        }
    }

    inner class DriversViewHolder(private val binding: LayoutDriverItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Driver) {
            binding.fullName.text =
                context.getString(R.string.full_name, item.lastName, item.firstName)
        }
    }
}