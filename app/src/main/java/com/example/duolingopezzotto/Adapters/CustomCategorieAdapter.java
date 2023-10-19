package com.example.duolingopezzotto.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duolingopezzotto.Activity.ParoleActivity;
import com.example.duolingopezzotto.Models.CategoriaModel;
import com.example.duolingopezzotto.R;

import java.util.ArrayList;

public class CustomCategorieAdapter extends RecyclerView.Adapter<CustomCategorieAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CategoriaModel> categorie;

    public CustomCategorieAdapter(Context context, ArrayList categorie) {
        this.context = context;
        this.categorie = categorie;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_categorie_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nomeTxt.setText(String.valueOf(categorie.get(position).getNome()));
        holder.notaTxt.setText(String.valueOf(categorie.get(position).getNota()));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParoleActivity.class);
                intent.putExtra("categoria", String.valueOf(categorie.get(position).getNome()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorie.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeTxt, notaTxt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.categoryrowLayout);
            nomeTxt = itemView.findViewById(R.id.nomeCategoriaTextView);
            notaTxt = itemView.findViewById(R.id.noteCategoriaTextView);
        }
    }
}
