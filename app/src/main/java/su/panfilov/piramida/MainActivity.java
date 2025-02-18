package su.panfilov.piramida;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import su.panfilov.piramida.components.BottomNavigationViewHelper;
import su.panfilov.piramida.models.Diary;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    private Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.removeShiftMode(mBottomNav); // disable BottomNavigationView shift mode

        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });
        // Обработчик для кнопки setupLinkButton
        Button setupLinkButton = findViewById(R.id.setupLinkButton);
        if (setupLinkButton != null) {
            setupLinkButton.setOnClickListener(v -> openOurAppsFragment());
        }


        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }
    private void openOurAppsFragment() {
        OurAppsFragment ourAppsFragment = OurAppsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, ourAppsFragment); // Замените rootLayout на ваш контейнер для фрагментов
        transaction.addToBackStack(null); // Добавляем в стек, чтобы можно было вернуться назад
        transaction.commit();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    public void onAboutImageButtonClick(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.setTitle(R.string.help_about);

        // set the custom dialog components - text, image and button
        Button closeButton = dialog.findViewById(R.id.aboutCloseButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        TextView aboutText = dialog.findViewById(R.id.aboutText);

        dialog.show();
    }

    public void onLinkButtonClick(View view) {
        // Create an instance of OurAppsFragment
        OurAppsFragment ourAppsFragment = OurAppsFragment.newInstance();

        // Begin the fragment transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rootLayout, ourAppsFragment);
        ft.addToBackStack(null); // Optional: Add to back stack if you want to allow the user to navigate back
        ft.commit();
    }


    private void selectFragment(MenuItem item) {
        if (frag != null) {
            FragmentTransaction ftremove = getSupportFragmentManager().beginTransaction();
            ftremove.remove(frag);
            ftremove.commit();
        }

        frag = null;
        // init corresponding fragment

        int itemId = item.getItemId();
        if (itemId == R.id.pyramidTab) {
            frag = PyramidFragment.newInstance();
        } else if (itemId == R.id.testTab) {
            frag = TestFragment.newInstance();
        } else if (itemId == R.id.helpTab) {
            frag = HelpFragment.newInstance();
        } else if (itemId == R.id.contentsTab) {
            frag = ContentsFragment.newInstance();
        } else if (itemId == R.id.settingsTab) {
            frag = SettingsFragment.newInstance();
        }

        // update selected item
        mSelectedItem = itemId;

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.rootLayout, frag, frag.getTag());
            ft.commit();
        }
    }
}
