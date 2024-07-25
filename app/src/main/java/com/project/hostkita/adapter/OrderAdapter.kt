package com.project.hostkita.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.hostkita.R
import com.project.hostkita.models.OrderUserItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderAdapter(private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<OrderAdapter.UserViewHolder>() {

    private val orderList: MutableList<OrderUserItem> = mutableListOf()

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tanggalLiveTextView: TextView = itemView.findViewById(R.id.tvTanggalLive)
        private val orderImageView: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvName: TextView = itemView.findViewById(R.id.tvNamaProduk)
        private val tvJumlahProduk: TextView = itemView.findViewById(R.id.tvJumlahProduk)
        private val tvStatusLive: TextView = itemView.findViewById(R.id.tvStatusLive)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(order: OrderUserItem) {

            val originalDate = order.tanggalLive
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val parsedDate = LocalDateTime.parse(originalDate, formatter)
            val dateOnly = parsedDate.toLocalDate().toString()

            tanggalLiveTextView.text = "Tanggal Live: " + dateOnly
            tvName.text = order.namaProduk
            tvStatusLive.text = order.statusLive
            tvJumlahProduk.text = "Jumlah Produk: " + order.jumlahProduk.toString()

            Glide.with(itemView.context)
                .load(order.fotoProduk)
                .into(orderImageView)

            itemView.setOnClickListener {
                onClickListener.onClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_order, parent, false)
        return UserViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setOrder(stories: List<OrderUserItem>) {
        orderList.clear()
        orderList.addAll(stories)
        notifyDataSetChanged()
    }

    fun getOrder(position: Int): OrderUserItem {
        return orderList[position]
    }
}