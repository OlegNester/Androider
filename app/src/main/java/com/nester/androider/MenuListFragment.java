package com.nester.androider;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MenuListFragment extends Fragment {

    private NavItemSelectedListener navItemSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);

        NavigationView vNavigation = view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Toast.makeText(getActivity(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                navItemSelectedListener.onNavItemSelectedListener(menuItem);
                return false;
            }
        }) ;

        //Зміна шрифту методом перебору
        Menu m = vNavigation.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
        }

        return  view ;
    }

    //Зміна шрифту методом перебору
    private void applyFontToMenuItem(MenuItem mi) {
        if (getActivity() != null) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_text));
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
        }
    }

    public void setNavItemSelectedListener(NavItemSelectedListener navItemSelectedListener) {
        this.navItemSelectedListener = navItemSelectedListener;
    }
}