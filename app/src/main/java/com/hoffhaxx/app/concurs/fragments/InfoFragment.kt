package com.hoffhaxx.app.concurs.fragments


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoffhaxx.app.concurs.EcoCard
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.RecyclerViewAdapter_InfoFragment
import com.hoffhaxx.app.concurs.activities.InfoPopUpActivity
import kotlinx.android.synthetic.main.info_fragment.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */

private val ecoCards = arrayListOf<EcoCard>()

fun gen_ecocard(): EcoCard{
    val points: Int = (1 until 6).random()
    val string_base = listOf<String>("wiadomosc a, kurwa musi byc dluga zeby dzialalo",
        "wiadomosc b, kurwa musi byc dluga zeby dzialalo",
        "wiadomosc c, kurwa musi byc dluga zeby dzialalo")   //do wypierdolenia
    val string_msg = string_base.get(Random.nextInt(string_base.size))

    return EcoCard(points, string_msg, R.drawable.ic_eco, R.drawable.ic_done)
}

class InfoFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        for (i in 1..50){
            ecoCards.add(gen_ecocard())
        }
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewBookmarks.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter_InfoFragment(ecoCards)
        }
    }

//    public fun showDialog() {
//
//        val dialog: Dialog? = Dialog(context!!)
//        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog?.setCancelable(false)
//        dialog?.setContentView(R.layout.info_popup)
//        val body = dialog?.findViewById(R.id.infopopup_body) as ConstraintLayout
//        val btnok = dialog.findViewById(R.id.infopopup_button2) as Button
//        val btnno = dialog.findViewById(R.id.infopopup_button3) as Button
//
//        btnok.setOnClickListener {
//            dialog.dismiss()
//        }
//        btnno.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }

}
