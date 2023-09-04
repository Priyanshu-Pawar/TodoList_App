package com.example.todolist.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Models.Notes;
import com.example.todolist.NotesClickListener;
import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNotes());

        holder.textView_data.setText(list.get(position).getDate());
        holder.textView_data.setSelected(true);

        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.ic_pin);
        }
        else {
            holder.imageView_pin.setImageResource(0);
        }

        int color_codes = getRandomColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_codes, null));
        }
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });
    }



    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
}

class NoteViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textView_title, textView_notes, textView_data;
    ImageView imageView_pin;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_data = itemView.findViewById(R.id.textView_data);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}
