package com.igor.langugecards.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.igor.langugecards.R
import com.igor.langugecards.database.room.AppDatabase
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.databinding.FragmentCardListDataBinding
import com.igor.langugecards.model.Card
import com.igor.langugecards.model.ToolbarConfiguration
import com.igor.langugecards.presentation.view.activity.MainActivity
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCardListDataBinding>(inflater,
                layoutRes,
                container,
                false)

        readArguments()

        binding.setVariable(com.igor.langugecards.BR.viewModel, mViewModel)
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.getRoot()

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
        val configuration = ToolbarConfiguration()
        val translateFrom = activity!!.getString(R.string.translate_from)
        val languageFrom = ""
        configuration.title = "$translateFrom $languageFrom"
        val translateInto = activity!!.getString(R.string.translate_into)
        val languageInto = ""
        configuration.subtitle = "$translateInto $languageInto"
        (activity as MainActivity).setToolbar(configuration, true)
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