package com.igor.langugecards.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.igor.langugecards.presentation.router.ApplicationRouter;
import com.igor.langugecards.presentation.view.activity.MainActivity;

public abstract class ApplicationFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setUpViews();
    }


    protected abstract void initViews(@NonNull View layout);

    protected abstract void setUpViews();

    protected abstract void setToolbar();

    protected abstract void readArguments();

    @LayoutRes
    protected abstract int getLayoutRes();

    @NonNull
    protected final ApplicationRouter getRouter() {
        return ((MainActivity) getActivity()).getRouter();
    }
}
