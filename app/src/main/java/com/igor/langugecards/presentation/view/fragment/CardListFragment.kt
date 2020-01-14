package com.igor.langugecards.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.igor.langugecards.presentation.router.ApplicationRouter
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
    private lateinit var learnButton: Button

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
        val binding = DataBindingUtil.inflate<FragmentCardListDataBinding>(
            inflater,
            layoutRes,
            container,
            false
        )

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
        learnButton = layout.findViewById(R.id.learn_card_button)
    }

    override fun setUpViews() {
        recyclerView.adapter = cardAdapter
        val callback = CustomItemTouchHelper(cardAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        viewModel.cards.observe(this, Observer { cards -> setUpCards(cards) })
        learnButton.setOnClickListener { router.showLearningScreen() }
    }

    override fun setToolbar() {
        val configuration = ToolbarConfiguration(HomeButton.ARROW, requireActivity().getString(R.string.card_list_fragment_title), ApplicationRouter::showTranslateScreen)
        (requireActivity() as MainActivity).setToolbar(configuration)
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
        viewModel.removeCard(itemPosition)
        cards.removeAt(itemPosition)
        cardAdapter.notifyItemRemoved(itemPosition)
    }

    private fun setUpCards(cardList: List<Card>) {
        if(cards.isNullOrEmpty()) {
            cards.addAll(cardList)
            cardAdapter.notifyDataSetChanged()
        }
    }
}