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
import com.project.hostkita.models.ListPenjualanItem
import com.project.hostkita.models.OrderUserItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PenjualanAdapter(private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<PenjualanAdapter.UserViewHolder>() {

    private val penjualanList: MutableList<ListPenjualanItem> = mutableListOf()

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaProduk: TextView = itemView.findViewById(R.id.cardNamaProduk)
        private val tvSisaProduk: TextView = itemView.findViewById(R.id.cardSisaProduk)
        private val tvTanggalUpdatePenjualan: TextView = itemView.findViewById(R.id.cardTanggalUpdatePenjualan)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(penjualan: ListPenjualanItem) {

            val originalDate = penjualan.tanggalUpdatePenjualan
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val parsedDate = LocalDateTime.parse(originalDate, formatter)
            val dateOnly = parsedDate.toLocalDate().toString()

            tvTanggalUpdatePenjualan.text = "Tanggal Update: " + dateOnly
            tvNamaProduk.text = penjualan.namaProduk
            tvSisaProduk.text = "Sisa Produk: " + penjualan.sisaProduk.toString() + " pcs"

            itemView.setOnClickListener {
                onClickListener.onClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_penjualan, parent, false)
        return UserViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val penjualan = penjualanList[position]
        holder.bind(penjualan)
    }

    override fun getItemCount(): Int {
        return penjualanList.size
    }

    fun setPejualan(penjualan: List<ListPenjualanItem>) {
        penjualanList.clear()
        penjualanList.addAll(penjualan)
        notifyDataSetChanged()
    }

    fun getPenjualan(position: Int): ListPenjualanItem {
        return penjualanList[position]
    }
}