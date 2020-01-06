package com.igor.langugecards.presentation.view.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.igor.langugecards.R;
import com.igor.langugecards.model.ToolbarConfiguration;
import com.igor.langugecards.presentation.router.ApplicationRouter;
import com.igor.langugecards.presentation.router.ApplicationRouterImpl;
import com.igor.langugecards.presentation.router.FragmentContainer;
import com.igor.langugecards.presentation.view.fragment.CreatingCardFragment;
import com.igor.langugecards.presentation.view.fragment.MainMenuFragment;
import com.igor.langugecards.presentation.view.fragment.SetTranslateLanguagesFragment;

public class MainActivity extends AppCompatActivity
        implements FragmentContainer, LanguageSettingsListener {

    private ApplicationRouter mRouter;
    private Class mActiveFragmentClass;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRouter = new ApplicationRouterImpl(this);
        mToolbar = findViewById(R.id.toolbar_actionbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupViews();
        showHomeFragment();
    }

    public void setToolbar(@Nullable ToolbarConfiguration configuration) {
        if (configuration != null) {
            mToolbar.setTitle(configuration.getTitle());
            mToolbar.setNavigationIcon(configuration.getHomeButtonRes());
        }
    }

    @NonNull
    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        mActiveFragmentClass = fragment.getClass();
    }

    @Override
    public void showHomeFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment homeFragment = fragmentManager.findFragmentByTag(CreatingCardFragment.FRAGMENT_TAG);

        if (homeFragment == null) {
            homeFragment = CreatingCardFragment.newInstance();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment, CreatingCardFragment.FRAGMENT_TAG)
                .commit();


        mActiveFragmentClass = MainMenuFragment.class;
    }

    @Override
    public void showLanguagesMenu() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .add(R.id.fragment_container, SetTranslateLanguagesFragment.newInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mActiveFragmentClass = SetTranslateLanguagesFragment.class;
    }

    @Override
    public void popBackStack() {

    }

    public ApplicationRouter getRouter() {
        return mRouter;
    }

    private void setupViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> getRouter().goHome());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        fm.getFragments();
    }

    @Override
    public void onLanguagesChanged() {
        LanguageSettingsListener translateFragment = (LanguageSettingsListener) getSupportFragmentManager()
                .findFragmentByTag(CreatingCardFragment.FRAGMENT_TAG);

        if (translateFragment != null) {
            translateFragment.onLanguagesChanged();
        }
    }
}
