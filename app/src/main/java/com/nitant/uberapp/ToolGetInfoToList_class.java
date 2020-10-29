package com.nitant.uberapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nitant.uberapp.R;
import com.nitant.uberapp.ToolAddInfo_class;

import java.util.List;

public class ToolGetInfoToList_class extends ArrayAdapter<ToolAddInfo_class> {

    private Activity context;
    private List<ToolAddInfo_class> toolList;

    public ToolGetInfoToList_class(Activity context, List<ToolAddInfo_class> toolList){

        super(context, R.layout.list_layout,toolList);
        this.context = context;
        this.toolList = toolList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater  = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView EquipmentNameTextView = (TextView)listViewItem.findViewById(R.id.EquipmentNameTextView);
        TextView RegistrationNumberTextView = (TextView)listViewItem.findViewById(R.id.RegistrationNumberTextView);
        TextView FromDateTextView = (TextView)listViewItem.findViewById(R.id.FromDateTextView);
        TextView ToDateTextView = (TextView)listViewItem.findViewById(R.id.ToDateTextView);

        ToolAddInfo_class tool = toolList.get(position);

        EquipmentNameTextView.setText(tool.getEquipmentName());
        RegistrationNumberTextView.setText(tool.getRegisterDetails());
        FromDateTextView.setText(tool.getDate1());
        ToDateTextView.setText(tool.getDate2());

        return listViewItem;
    }
}
