package com.igor.langugecards.presentation.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
import com.igor.langugecards.presentation.view.fragment.MainMenuFragment;
import com.igor.langugecards.presentation.view.fragment.SetTranslateLanguagesFragment;

import static com.igor.langugecards.model.ToolbarConfiguration.HOME_BUTTON_RES;

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
        getMenuInflater().inflate(R.menu.menu_translate_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.translate_settings : {
                showFragment(SetTranslateLanguagesFragment.getInstance(), true);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(@Nullable ToolbarConfiguration configuration, boolean showHomeButton) {
        if(configuration == null) {
            configuration = ToolbarConfiguration.getDefaultToolbarConfiguration();
        }
        if(configuration.getTitle() == null) {
            configuration.setTitle(getString(R.string.app_name));
        }
        if(showHomeButton) {
            mToolbar.setNavigationIcon(HOME_BUTTON_RES);
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
