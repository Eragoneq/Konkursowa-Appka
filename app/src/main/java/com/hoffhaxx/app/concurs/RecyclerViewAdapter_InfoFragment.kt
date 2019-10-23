package com.hoffhaxx.app.concurs

import android.app.Dialog
import android.content.Intent
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hoffhaxx.app.concurs.activities.InfoPopUpActivity
import com.hoffhaxx.app.concurs.fragments.InfoFragment

class RecyclerViewAdapter_InfoFragment(private val eco_cards: ArrayList<EcoCard>):
    RecyclerView.Adapter<RecyclerViewAdapter_InfoFragment.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter_InfoFragment.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_infofragment,
            parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return eco_cards.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter_InfoFragment.ViewHolder, position: Int) {
        val ecocard : EcoCard = eco_cards[position]
        holder.textViewPoints?.text = "+" + ecocard.points.toString()
        holder.textViewInfo?.text = ecocard.info
        holder.imageViewPoints?.setImageResource(ecocard.imgPoints_id)
        holder.imageViewTick?.setImageResource(ecocard.imgTick_id)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewPoints:TextView? = null
        var textViewInfo:TextView? = null
        var imageViewPoints:ImageView? = null
        var imageViewTick:ImageView? = null

        init {
            textViewPoints = itemView.findViewById(R.id.recyclerView_textPoints)
            textViewInfo = itemView.findViewById(R.id.recyclerView_textInfo)
            imageViewPoints = itemView.findViewById(R.id.recyclerView_imagePoints)
            imageViewTick = itemView.findViewById(R.id.recyclerView_imageTick)

            itemView.setOnClickListener {
                val window = PopupWindow(itemView.context)
                val view = LayoutInflater.from(itemView.context).inflate(R.layout.info_popup, null)
                window.contentView = view

                var button_exit: Button = view.findViewById(R.id.infopopup_buttonexit)
                button_exit.setOnClickListener{
                    window.dismiss()
                }
                window.showAtLocation(itemView, Gravity.CENTER, 10, 10)
            }
        }


    }
}