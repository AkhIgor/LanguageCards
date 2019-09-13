package com.igor.langugecards.presentation.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

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
import com.igor.langugecards.presentation.view.fragment.MainMenuFragment;
import com.igor.langugecards.presentation.view.fragment.SetTranslateLanguagesFragment;

public class MainActivity extends AppCompatActivity
        implements FragmentContainer {

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

    private void showMenu() {
        showFragment(MainMenuFragment.getInstance(), true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupViews();
        showMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_translate_main, menu);
        showFragment(SetTranslateLanguagesFragment.getInstance(), false);
        return true;
    }


    public void setToolbar(@NonNull ToolbarConfiguration configuration) {
        if(configuration.getHomeButton() != ToolbarConfiguration.NO_HOME_BUTTON) {
            mToolbar.setNavigationIcon(R.drawable.ic_cross_24dp);
        } else {
            mToolbar.setNavigationIcon(null);
        }
        mToolbar.setTitle(configuration.getTitle());
        mToolbar.setSubtitle(configuration.getSubtitle());
    }

    @NonNull
    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction().replace(R.id.fragment_container, fragment);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
            mActiveFragmentClass = fragment.getClass();
        }
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
}
