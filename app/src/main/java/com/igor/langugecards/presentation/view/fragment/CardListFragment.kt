package com.igor.langugecards.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.igor.langugecards.R
import com.igor.langugecards.database.room.AppDatabase
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.databinding.FragmentCardListDataBinding
import com.igor.langugecards.model.Card
import com.igor.langugecards.model.HomeButton
import com.igor.langugecards.model.ToolbarConfiguration
import com.igor.langugecards.presentation.view.activity.MainActivity
import com.igor.langugecards.presentation.view.adapter.CardListAdapter
import com.igor.langugecards.presentation.view.adapter.animation.CustomItemTouchHelper
import com.igor.langugecards.presentation.view.adapter.animation.ItemSwipeListener
import com.igor.langugecards.presentation.viewmodel.CardListViewModel
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory


class CardListFragment : ApplicationFragment(), ItemSwipeListener {

    private val cards: MutableList<Card> = ArrayList()
    private val cardAdapter = CardListAdapter(this, cards)

    private lateinit var cardInteractor: CardInteractor
    private lateinit var recyclerView: RecyclerView

    private val viewModel: CardListViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory {
            CardListViewModel(cardInteractor)
        })
                .get(CardListViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCardListDataBinding>(inflater,
                layoutRes,
                container,
                false)

        readArguments()

        binding.setVariable(com.igor.langugecards.BR.viewModel, viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onStart() {
        super.onStart()

        viewModel.onScreenStart()
    }

    override fun initViews(layout: View) {
        recyclerView = layout.findViewById(R.id.card_list_recycler_view)
    }

    override fun setUpViews() {
        recyclerView.adapter = cardAdapter
        val callback = CustomItemTouchHelper(cardAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        viewModel.mCards.observe(this, Observer { cards -> setUpCards(cards) })

    }

    override fun setToolbar() {
//        val configuration = ToolbarConfiguration()
//        val translateFrom = activity!!.getString(R.string.translate_from)
//        val languageFrom = ""

//        configuration.title = "$translateFrom $languageFrom"
//        val translateInto = activity!!.getString(R.string.translate_into)
//        val languageInto = ""
//        configuration.subtitle = "$translateInto $languageInto"
        (activity as MainActivity).setToolbar(ToolbarConfiguration(HomeButton.CROSS, requireActivity().getString(R.string.app_name)))
    }

    override fun readArguments() {
        cardInteractor = AppDatabase
                .getInstance(requireActivity())
                .cardInteractor
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_card_list
    }

    override fun onItemSwipe(itemPosition: Int) {
        viewModel.removeCard(cards[itemPosition])
        cards.removeAt(itemPosition)
        cardAdapter.notifyItemRemoved(itemPosition)
    }

    private fun setUpCards(cardList: List<Card>) {
        cards.addAll(cardList)
        cardAdapter.notifyDataSetChanged()
    }
}