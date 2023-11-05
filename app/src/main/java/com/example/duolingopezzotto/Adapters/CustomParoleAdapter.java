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

import com.example.duolingopezzotto.Activity.ModificaParolaActivity;
import com.example.duolingopezzotto.SQLiteDB.Models.ParolaModel;
import com.example.duolingopezzotto.R;

import java.util.ArrayList;

public class CustomParoleAdapter extends RecyclerView.Adapter<CustomParoleAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<ParolaModel> parole;

    public CustomParoleAdapter(Context context, ArrayList categorie) {
        this.context = context;
        this.parole = categorie;
    }


    @NonNull
    @Override
    public CustomParoleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_parola_row, parent, false);
        return new CustomParoleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomParoleAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itaTxt.setText(String.valueOf(parole.get(position).getItaliano()));
        holder.spTxt.setText(String.valueOf(parole.get(position).getSpagnolo()));
        holder.engTxt.setText(String.valueOf(parole.get(position).getInglese()));
        holder.levelTxt.setText(String.valueOf(parole.get(position).getLevel()));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModificaParolaActivity.class);
                intent.putExtra("italiano", String.valueOf(parole.get(position).getItaliano()));
                intent.putExtra("spagnolo", String.valueOf(parole.get(position).getSpagnolo()));
                intent.putExtra("inglese", String.valueOf(parole.get(position).getInglese()));
                intent.putExtra("id", parole.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parole.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itaTxt, spTxt, engTxt, levelTxt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.parolarowLayout);
            itaTxt = itemView.findViewById(R.id.nomeParolaItalianaTextView);
            spTxt = itemView.findViewById(R.id.nomeParolaSpagnolaTextView);
            engTxt = itemView.findViewById(R.id.nomeParolaIngleseTextView);
            levelTxt = itemView.findViewById(R.id.levelTextView);
        }
    }
}
