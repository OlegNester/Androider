package com.nester.androider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.nester.androider.adapter.DataAdapter;
import com.nester.androider.adapter.ListItem;
import com.nester.androider.adapter.RecycleClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavItemSelectedListener {
    private RecycleClickListener recycleClickListener;
    private DataAdapter dataAdapter;
    private List<ListItem> listData;
    private RecyclerView recyclerView;
    private String getCategory = "";
    private SharedPreferences preferences;
    private final String JAVA = "java";
    private final String ANDROID = "android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMenu();
        setRecycleClickListener();
        init();
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            mMenuFragment.setNavItemSelectedListener(this);
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    @Override
    public void onNavItemSelectedListener(MenuItem item) {
        //Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.id_newsIt:
                updateFavorite();
                break;
            case R.id.id_java:
                updateMainList(getResources().getStringArray(R.array.oop), JAVA);
                break;
            case R.id.id_android:
                updateMainList(getResources().getStringArray(R.array.android), ANDROID);
                break;
        }
    }

    private  void updateMainList(String[] array, String category) {
        getCategory = category;
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        String tempCategory = preferences.getString(category, "none");
        if(tempCategory != null) {
            if (tempCategory.equals("none")) {
                for (int i = 0; i < array.length; i++) {
                    stringBuilder.append("0");
                }
                Log.d("LogPositionCatrgory", category + " " + stringBuilder.toString());
                saveString(stringBuilder.toString());
            } else {

            }
        }
        List<ListItem> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            ListItem item = new ListItem();
            item.setText(array[i]);
            item.setCategory(category);
            item.setPosition(i);
            list.add(item);
        }
        dataAdapter.updateList(list, false);
    }

    private void updateFavorite() {
        List<ListItem> listFavorite = new ArrayList<>();
        List<String[]> listData = new ArrayList<>();
        listData.add(getResources().getStringArray(R.array.oop));
        listData.add(getResources().getStringArray(R.array.android));
        String[] category_array = {JAVA, ANDROID};

        for (int i = 0; i < listData.size(); i++) {
            for (int p = 0; p < listData.get(i).length; p++){
                String d = preferences.getString(category_array[i], "none");
                if (d != null)if(d.charAt(p) == '1'){
                    ListItem item = new ListItem();
                    item.setText(listData.get(i)[p]);
                    item.setPosition(p);
                    item.setCategory(category_array[i]);
                    listFavorite.add(item);
                }
            }
        }
        dataAdapter.updateList(listFavorite, true);
    }

    private void init() {
        preferences = getSharedPreferences("Category", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        String[] javaArray = getResources().getStringArray(R.array.android);
        dataAdapter = new DataAdapter(this, recycleClickListener, listData);
        updateMainList(javaArray, "android");
        recyclerView.setAdapter(dataAdapter);

        /*for (int i = 0; i < javaArray.length; i++) {
            ListItem item = new ListItem();
            item.setText(javaArray[i]);
            item.setFavorites(false);
            listData.add(item);
        }*/
    }

    private void setRecycleClickListener() {
        recycleClickListener = new RecycleClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Toast.makeText(MainActivity.this, "position = " + position, Toast.LENGTH_SHORT).show();
                String tempCategory = preferences.getString(getCategory, "none");
                if(tempCategory != null){
                    if (tempCategory.charAt(position) == '0'){
                        saveString(replaceCharAtPosition(position, '1', tempCategory));
                    }else {
                        saveString(replaceCharAtPosition(position, '0', tempCategory));
                    }
                }
            }
        };
    }

    private String replaceCharAtPosition(int position, char ch, String st) {
        char[] charArray = st.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    private void saveString(String stringToSave) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getCategory, stringToSave);
        editor.apply();
        Log.d("MyLog","Saved data fav : " + preferences.getString(getCategory,"none"));
    }

}
