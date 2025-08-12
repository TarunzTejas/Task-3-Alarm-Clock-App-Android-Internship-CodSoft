package com.example.alarmclockapp;

import com.example.alarmclockapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context context;
    private List<Alarm> alarmList;
    private OnAlarmToggleListener toggleListener;
    private OnAlarmDeleteListener deleteListener;

    public AlarmAdapter(Context context, List<Alarm> alarmList, OnAlarmToggleListener toggleListener, OnAlarmDeleteListener deleteListener) {
        this.context = context;
        this.alarmList = alarmList;
        this.toggleListener = toggleListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        String timeFormatted = String.format("%02d:%02d", alarm.hour, alarm.minute);
        holder.timeText.setText(timeFormatted);

        holder.alarmSwitch.setOnCheckedChangeListener(null);
        holder.alarmSwitch.setChecked(alarm.isActive);

        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.isActive = isChecked;
            toggleListener.onToggle(alarm);
        });

        holder.deleteButton.setOnClickListener(v -> {
            deleteListener.onDelete(alarm);
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        Switch alarmSwitch;
        ImageButton deleteButton;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.alarm_time_text);
            alarmSwitch = itemView.findViewById(R.id.alarm_switch);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnAlarmToggleListener {
        void onToggle(Alarm alarm);
    }

    public interface OnAlarmDeleteListener {
        void onDelete(Alarm alarm);
    }
}



