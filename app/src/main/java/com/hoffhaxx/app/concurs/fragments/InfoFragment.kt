package com.hoffhaxx.app.concurs.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoffhaxx.app.concurs.EcoCard
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.RecyclerViewAdapterInfoFragment
import com.hoffhaxx.app.concurs.misc.QuestRepository
import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.misc.web.WebClient
import kotlinx.android.synthetic.main.info_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */

private val ecoCards = arrayListOf<EcoCard>()

fun gen_ecocard(): EcoCard{
    val points: Int = (1 until 6).random()
    val string_base = listOf("wiadomosc a, musi byc dluga zeby dzialalo",
        "wiadomosc b, musi byc dluga zeby dzialalo",
        "wiadomosc c, musi byc dluga zeby dzialalo")
    val string_msg = string_base.get(Random.nextInt(string_base.size))

    return EcoCard(points, string_msg, R.drawable.ic_eco, R.drawable.ic_done)
}

class InfoFragment : Fragment() {

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
            adapter = RecyclerViewAdapterInfoFragment(ecoCards)
        }
    }

    private fun testCards() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val quests = mutableListOf(Quest("fajny opis", 10.0))
            val result = QuestRepository.addQuests(quests)
            Log.i("TEST", result.toString())
        } catch (e : WebClient.NetworkException) {
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(this@InfoFragment.context)
                    .setTitle(getString(R.string.logging_error))
                    .setMessage(getString(R.string.cannot_connect_to_server))
                    .setNeutralButton(getString(R.string.ok)) {dialog, which ->  }
                    .create()
                    .show()
            }
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
