package com.igor.langugecards.presentation.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.igor.langugecards.R
import com.igor.langugecards.database.room.AppDatabase
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import com.igor.langugecards.presentation.view.adapter.CardListAdapter
import com.igor.langugecards.presentation.viewmodel.CardListViewModel
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory

class CardListFragment : ApplicationFragment() {

    private val mCards = ArrayList<Card>()
    private val mCardAdapter = CardListAdapter(mCards)

    private lateinit var mCardInteractor: CardInteractor
    private lateinit var mRecyclerView: RecyclerView

    private val mViewModel: CardListViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory {
            CardListViewModel(mCardInteractor)
        })
                .get(CardListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        mViewModel.onScreenStart()
    }

    override fun initViews(layout: View) {
        mRecyclerView = layout.findViewById(R.id.card_list_recycler_view)
    }

    override fun setUpViews() {
        mRecyclerView.adapter = mCardAdapter
        mViewModel.mCards.observe(this, Observer { cards -> setUpCards(cards) })

    }

    override fun setToolbar() {

    }

    override fun readArguments() {
        mCardInteractor = AppDatabase
                .getInstance(requireActivity())
                .cardInteractor
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_card_list
    }

    private fun setUpCards(cards: List<Card>) {
        mCards.addAll(cards)
        mCardAdapter.notifyDataSetChanged()
    }
}