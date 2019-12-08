package com.igor.langugecards.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.igor.langugecards.R;
import com.igor.langugecards.model.Card;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private final List<Card> mCards;

    public CardListAdapter(@NonNull List<Card> cards) {
        mCards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_list, parent, false);
        return new CardViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = mCards.get(position);

        holder.setTheme(card.getTheme());
        holder.setWords(card.getNativeWord(),
                card.getTranslatedWord());
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    static final class CardViewHolder extends RecyclerView.ViewHolder {

        private final TextView mThemeTextView;
        private final TextView mNativeWordTextView;
        private final TextView mTranslatedWordTextView;

        private CardViewHolder(@NonNull View itemView) {
            super(itemView);

            mThemeTextView = itemView.findViewById(R.id.card_item_theme);
            mNativeWordTextView = itemView.findViewById(R.id.card_item_native_word);
            mTranslatedWordTextView = itemView.findViewById(R.id.card_item_translated_word);
        }

        private void setTheme(@NonNull String theme) {
            mThemeTextView.setText(theme);
        }

        private void setWords(@NonNull String nativeWord,
                              @NonNull String translatedWord) {
            mNativeWordTextView.setText(nativeWord);
            mTranslatedWordTextView.setText(translatedWord);
        }
    }
}
