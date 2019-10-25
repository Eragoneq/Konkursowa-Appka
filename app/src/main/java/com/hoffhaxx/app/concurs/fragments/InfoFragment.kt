package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.RecyclerViewAdapterInfoFragment
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import kotlinx.android.synthetic.main.info_fragment.*

/**
 * A simple [Fragment] subclass.
 */

class InfoFragment : Fragment() {

//    private fun savequest(q : Quest) = CoroutineScope(Dispatchers.IO).launch {
//        try {
//                QuestRepository.addQuests(mutableListOf(q))
//        } catch (e : WebClient.NetworkException) {
//            withContext(Dispatchers.Main) {
//            }
//        }
//    }

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
            adapter = RecyclerViewAdapterInfoFragment(SharedPreferencesRepository.quests)
        }
    }
}
