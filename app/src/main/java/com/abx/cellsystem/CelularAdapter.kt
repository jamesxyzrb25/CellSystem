package com.abx.cellsystem

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cellsystem.R
import cellsystem.databinding.ItemCelularBinding

class CelularAdapter(var celularList: MutableList<Celular>, private val listener: OnClickListener):
    RecyclerView.Adapter<CelularAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val binding = ItemCelularBinding.bind(view)

        fun setListener(celular: Celular){
            binding.tvNroCelular.setOnClickListener {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    val phone = (it as TextView).text
                    data = Uri.parse("tel:$phone")
                }
                Log.i("Valor de intent Adapter",intent.toString())

                listener.onClickCelNumber(intent)
                //startActivity(intent)

            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(celular, this@CelularAdapter)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_celular, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val celular = celularList.get(position)
        holder.setListener(celular)
        holder.binding.tvIdCelular.text = celular.id.toString()
        holder.binding.tvNroImei.text = celular.nroImei
        holder.binding.tvNroCelular.text = celular.nroCelular

    }
    override fun getItemCount(): Int {
        return celularList.size
    }


    fun add(celular:Celular){
        celularList.add(celular)
        notifyDataSetChanged()
    }
    fun remove(celular: Celular){
        celularList.remove(celular)
        notifyDataSetChanged()
    }
}