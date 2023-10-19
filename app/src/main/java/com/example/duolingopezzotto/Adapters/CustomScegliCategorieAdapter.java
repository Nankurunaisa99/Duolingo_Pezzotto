package com.example.duolingopezzotto.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duolingopezzotto.Activity.ParoleActivity;
import com.example.duolingopezzotto.Models.CategoriaModel;
import com.example.duolingopezzotto.R;

import java.util.ArrayList;

public class CustomScegliCategorieAdapter extends RecyclerView.Adapter<CustomScegliCategorieAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CategoriaModel> categorie;
    public static ArrayList<Integer> id_categorie;

    public CustomScegliCategorieAdapter(Context context, ArrayList categorie) {
        this.context = context;
        this.categorie = categorie;
        id_categorie = new ArrayList<>();
    }


    @NonNull
    @Override
    public CustomScegliCategorieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_scegli_categorie_row, parent, false);
        return new CustomScegliCategorieAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomScegliCategorieAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nomeTxt.setText(String.valueOf(categorie.get(position).getNome()));
        holder.notaTxt.setText(String.valueOf(categorie.get(position).getNota()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) id_categorie.add(categorie.get(position).getId());
                else{
                    Integer id_remove = categorie.get(position).getId();
                    id_categorie.remove(id_remove);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorie.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeTxt, notaTxt;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.categoriaCheckBox);
            nomeTxt = itemView.findViewById(R.id.nomeCategoriaTextView2);
            notaTxt = itemView.findViewById(R.id.noteCategoriaTextView2);
        }
    }
}
