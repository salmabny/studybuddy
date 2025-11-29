package com.example.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> list;

    public TaskAdapter(Context context, List<Task> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task t = list.get(position);

        holder.tvTitle.setText(t.getTitle());
        holder.tvType.setText(t.getType());
        holder.tvDate.setText(t.getDate() + " " + t.getHour()); // Combiner date et heure
        holder.tvSubject.setText(t.getSubject());
        holder.tvDesc.setText(t.getDescription());

        holder.btnEdit.setOnClickListener(v ->
                ((TaskListActivity) context).editTask(t)
        );

        holder.btnDelete.setOnClickListener(v ->
                ((TaskListActivity) context).deleteTask(t)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvType, tvDate, tvSubject, tvDesc;
        Button btnEdit, btnDelete;

        public TaskViewHolder(@NonNull View v) {
            super(v);

            tvTitle = v.findViewById(R.id.tvTitle);
            tvType = v.findViewById(R.id.tvType);
            tvDate = v.findViewById(R.id.tvDate);
            tvSubject = v.findViewById(R.id.tvSubject);
            tvDesc = v.findViewById(R.id.tvDesc);

            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
