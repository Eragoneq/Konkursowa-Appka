package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoffhaxx.app.concurs.EcoCard
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.RecyclerViewAdapter_InfoFragment
import kotlinx.android.synthetic.main.info_fragment.*

/**
 * A simple [Fragment] subclass.
 */

private val ecoCards = arrayListOf<EcoCard>(
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done),
    EcoCard(5, "Bedzie was pis ruchal w dupe",
        R.drawable.ic_eco, R.drawable.ic_done)
)


class InfoFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewBookmarks.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter_InfoFragment(ecoCards)
        }
    }


}
