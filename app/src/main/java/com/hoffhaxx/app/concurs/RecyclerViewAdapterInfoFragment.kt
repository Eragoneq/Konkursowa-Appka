package com.hoffhaxx.app.concurs

import android.annotation.SuppressLint
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hoffhaxx.app.concurs.misc.data.Quest

class RecyclerViewAdapterInfoFragment(private val quests: MutableList<Quest>?):
    RecyclerView.Adapter<RecyclerViewAdapterInfoFragment.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_infofragment,
            parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if (quests != null){
            return quests.size
        }
        else{
            return 0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ecoCard : Quest = quests!![position]
        holder.textViewPoints?.text = "+" + ecoCard.points.toString()
        holder.textViewInfo?.text = ecoCard.description
//        holder.imageViewPoints?.setImageResource(ecoCard.imgPoints_id)
//        holder.imageViewTick?.setImageResource(ecoCard.imgTick_id)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewPoints:TextView? = null
        var textViewInfo:TextView? = null
//        var imageViewPoints:ImageView? = null
//        var imageViewTick:ImageView? = null

        init {
            textViewPoints = itemView.findViewById(R.id.recyclerView_textPoints)
            textViewInfo = itemView.findViewById(R.id.recyclerView_textInfo)
//            imageViewPoints = itemView.findViewById(R.id.recyclerView_imagePoints)
//            imageViewTick = itemView.findViewById(R.id.recyclerView_imageTick)

            itemView.setOnClickListener {
                val window = PopupWindow(itemView.context)
                window.setBackgroundDrawable(null)
                val view = LayoutInflater.from(itemView.context).inflate(R.layout.info_popup, null)
                window.contentView = view
                window.elevation = 20f
                val buttonExit: Button = view.findViewById(R.id.infopopup_buttonexit)
                val textv: TextView = view.findViewById(R.id.infopopup_text)
                textv.text = textViewInfo?.text

                buttonExit.setOnClickListener{
                    window.dismiss()
                }
                window.showAtLocation(itemView, Gravity.CENTER, 10, 10)
            }
        }


    }
}