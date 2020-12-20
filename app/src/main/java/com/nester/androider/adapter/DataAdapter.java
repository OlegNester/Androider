package com.nester.androider.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nester.androider.R;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {
    private Context context;
    private RecycleClickListener recycleClickListener;
    private List<ListItem> listItemArray;
    private SharedPreferences preferences;
    private boolean isFavorite;

    public DataAdapter(Context context, RecycleClickListener recycleClickListener, List<ListItem> listItemArray) {
        this.context = context;
        this.recycleClickListener = recycleClickListener;
        this.listItemArray = listItemArray;
        preferences = context.getSharedPreferences("Category", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        holder.setData(listItemArray.get(position));
    }

    @Override
    public int getItemCount() {
        return listItemArray.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private boolean isFavoriteChecked = false;
        private TextView textContentItem;
        private ImageButton buttonFavorite;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            textContentItem = itemView.findViewById(R.id.textContentItem);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
            buttonFavorite.setOnClickListener(this);
        }

        public void setData(ListItem item){
            textContentItem.setText(item.getText());
            if (!isFavoriteChecked){
                setFavorite(item, getAdapterPosition());
            }else {
                setFavoriteAll();
            }
        }

        @Override
        public void onClick(View v) {
            isFavoriteChecked = !isFavoriteChecked;
            if(isFavoriteChecked) {
                buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                buttonFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            }

            if(!isFavorite){
                recycleClickListener.onItemClicked(getAdapterPosition());
            } else {
                deleteItem();
            }
        }

        private String replaceCharAtPosition(int position, char ch, String st) {
            char[] charArray = st.toCharArray();
            charArray[position] = ch;
            return new String(charArray);
        }

        private void saveString(String stToSave) {
            ListItem item = listItemArray.get(getAdapterPosition());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(item.getCategory(), stToSave);
            editor.apply();
        }

        private void deleteItem() {
            ListItem item = listItemArray.get(getAdapterPosition());
            String dataToChange = preferences.getString(item.getCategory(), "none");
            if (dataToChange == null)return;
            String replacedData = replaceCharAtPosition(item.getPosition(), '0', dataToChange);
            saveString(replacedData);
            listItemArray.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), listItemArray.size());
        }

        private void setFavoriteAll() {
            buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }

        private void setFavorite(ListItem item, int position) {
            String favorite_data = preferences.getString(item.getCategory(), "none");
            if(favorite_data != null) {
                char[] charArray = favorite_data.toCharArray();
                switch (charArray[position]) {
                    case '0':
                        buttonFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                        isFavoriteChecked = false;
                        break;
                    case '1':
                        buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                        isFavoriteChecked = true;
                        break;
                }
            }
        }
    }

    public void updateList(List<ListItem> listArray, boolean isFavorite) {
        this.isFavorite = isFavorite;
        listItemArray.clear();
        listItemArray.addAll(listArray);
        notifyDataSetChanged();
    }
}

//@android:drawable/btn_star_big_off
//@android:drawable/btn_star_big_on
