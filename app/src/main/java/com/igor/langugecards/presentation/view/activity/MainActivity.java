package com.igor.langugecards.presentation.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

    public void setToolbar(@NonNull ToolbarConfiguration configuration) {
        mToolbar.setTitle(configuration.getTitle());
        mToolbar.setNavigationIcon(configuration.getHomeButtonRes());
        mToolbar.setNavigationOnClickListener(v -> configuration.getActionOnButton().accept(mRouter));
    }

    @Override
    public void showFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .commit();
        mActiveFragmentClass = fragment.getClass();
    }

    @Override
    public void showHomeFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment initialFragment = fragmentManager.findFragmentByTag(CreatingCardFragment.FRAGMENT_TAG);

        if (initialFragment == null) {
            initialFragment = CreatingCardFragment.newInstance();
        }

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out)
                .replace(R.id.fragment_container, initialFragment, CreatingCardFragment.FRAGMENT_TAG)
                .commit();


        mActiveFragmentClass = CreatingCardFragment.class;
    }

    @Override
    public void showLanguagesMenu() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .add(R.id.fragment_container, SetTranslateLanguagesFragment.newInstance());
        fragmentTransaction.addToBackStack(null)
                .commit();
        mActiveFragmentClass = SetTranslateLanguagesFragment.class;
    }

    public ApplicationRouter getRouter() {
        return mRouter;
    }

    private void setupViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> getRouter().showTranslateScreen());
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
