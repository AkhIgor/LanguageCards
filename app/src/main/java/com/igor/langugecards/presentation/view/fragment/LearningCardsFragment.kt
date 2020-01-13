package com.igor.langugecards.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.igor.langugecards.R
import com.igor.langugecards.database.room.AppDatabase
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.databinding.LearningCardsDataBinding
import com.igor.langugecards.model.HomeButton
import com.igor.langugecards.model.ToolbarConfiguration
import com.igor.langugecards.presentation.router.ApplicationRouter
import com.igor.langugecards.presentation.view.activity.MainActivity
import com.igor.langugecards.presentation.view.custom.LanguageCardView
import com.igor.langugecards.presentation.viewmodel.LearningCardsViewModel
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable

class LearningCardsFragment : ApplicationFragment() {

    private lateinit var cardInteractor: CardInteractor
    private lateinit var cardView: LanguageCardView

    private val viewModel: LearningCardsViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory {
            LearningCardsViewModel(
                    cardInteractor,
                    CompositeDisposable()
            )
        })
                .get(LearningCardsViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance(): LearningCardsFragment {
            return LearningCardsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<LearningCardsDataBinding>(
                inflater,
                layoutRes,
                container,
                false
        )

        readArguments()

        binding.setVariable(com.igor.langugecards.BR.viewModel, viewModel)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.onScreenStart()

        return binding.root
    }

    override fun initViews(layout: View) {
        cardView = layout.findViewById(R.id.card)
    }

    override fun setUpViews() {
        cardView.setViewListener(viewModel)
    }

    override fun setToolbar() {
        val configuration = ToolbarConfiguration(HomeButton.CROSS, requireActivity().getString(R.string.learning_fragment_title), ApplicationRouter::showAllCards)
        (requireActivity() as MainActivity).setToolbar(configuration)
    }

    override fun readArguments() {
        cardInteractor = AppDatabase
                .getInstance(requireActivity())
                .cardInteractor
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_learning_cards
    }
}