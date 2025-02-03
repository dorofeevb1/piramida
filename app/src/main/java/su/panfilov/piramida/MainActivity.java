package su.panfilov.piramida;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.removeShiftMode(mBottomNav);//disable BottomNavigationView shift mode

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
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
    protected void onSaveInstanceState(Bundle outState) {
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
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView aboutText = dialog.findViewById(R.id.aboutText);

        dialog.show();
    }

    public void onLinkButtonClick(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.links_dialog);
        dialog.setTitle(R.string.help_about_links);

        // set the custom dialog components - text, image and button
        Button closeButton = dialog.findViewById(R.id.linksCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView aboutText = dialog.findViewById(R.id.linksText);

        dialog.show();
    }

    public void recordTapped(View view) {
        try {
            PyramidFragment pyramidFragment = (PyramidFragment) frag;
            pyramidFragment.recordTapped(view);
        } catch (NullPointerException e) {
            //
        }
    }

    public void doneTapped(View view) {
        try {
            PyramidFragment pyramidFragment = (PyramidFragment) frag;
            pyramidFragment.doneTapped(view);
        } catch (NullPointerException e) {
            //
        }
    }

//    public void playTapped(View view) {
//        try {
//            DiaryFragment diaryFragment = (DiaryFragment) frag;
//            diaryFragment.playTapped(view);
//        } catch (NullPointerException e) {
//            //
//        }
//    }
//
//    public void backTapped(View view) {
//        try {
//            DiaryFragment diaryFragment = (DiaryFragment) frag;
//            diaryFragment.backTapped(view);
//        } catch (NullPointerException e) {
//            //
//        }
//    }

    private void selectFragment(MenuItem item) {
        if (frag != null) {
            FragmentTransaction ftremove = getSupportFragmentManager().beginTransaction();
            ftremove.remove(frag);
            ftremove.commit();
        }

        frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.pyramidTab:
                frag = PyramidFragment.newInstance();
                break;
//            case R.id.diaryTab:
//                frag = DiaryFragment.newInstance();
//                break;

            case R.id.testTab:
                frag = HelpFragment.newInstance();
                break;
            case R.id.helpTab:
                frag = HelpFragment.newInstance();
                break;
            case R.id.contentsTab:
                frag = ContentsFragment.newInstance();
                break;
            case R.id.settingsTab:
                frag = SettingsFragment.newInstance();
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.rootLayout, frag, frag.getTag());
            ft.commit();
        }
    }
}
