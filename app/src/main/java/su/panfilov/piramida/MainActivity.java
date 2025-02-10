package su.panfilov.piramida;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

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

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
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
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.links_dialog);
        dialog.setTitle(R.string.help_about_links);

        // set the custom dialog components - text, image and button
        Button closeButton = dialog.findViewById(R.id.linksCloseButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        TextView aboutText = dialog.findViewById(R.id.linksText);

        dialog.show();
    }

    public void recordTapped(View view) {
        try {
            PyramidFragment pyramidFragment = (PyramidFragment) frag;
            pyramidFragment.recordTapped(view);
        } catch (NullPointerException e) {
            // Handle exception
        }
    }

    public void doneTapped(View view) {
        try {
            PyramidFragment pyramidFragment = (PyramidFragment) frag;
            pyramidFragment.doneTapped(view);
        } catch (NullPointerException e) {
            // Handle exception
        }
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
