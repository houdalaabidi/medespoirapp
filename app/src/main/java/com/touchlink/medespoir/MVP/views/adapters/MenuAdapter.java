package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.touchlink.medespoir.R;

import java.util.ArrayList;

import nl.psdcompany.duonavigationdrawer.views.DuoOptionView;

public class MenuAdapter extends BaseAdapter {
    private ArrayList<String> mOptions = new ArrayList<>();
    private ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();
    private Context context ;
    private ArrayList<Integer> mOptionsDrawable= new ArrayList<Integer>();

   public  MenuAdapter(ArrayList<String> options, Context context) {
        mOptions = options;
        this.context = context ;
      // mOptionsDrawable.add(R.drawable.icon_home);
       mOptionsDrawable.add(R.drawable.medespoir_icon);
       mOptionsDrawable.add(R.drawable.icon_message);
       mOptionsDrawable.add(R.drawable.devis_icon);
       mOptionsDrawable.add(R.drawable.reclamation_icon
       );
       mOptionsDrawable.add(R.drawable.icon_tv);
       mOptionsDrawable.add(R.drawable.icon_panier);
       mOptionsDrawable.add(R.drawable.icon_settings);
      // mOptionsDrawable.add(R.drawable.icon_edit);
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setViewSelected(int position, boolean selected) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);
        final int icon = mOptionsDrawable.get(position);

        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }

        // Using the DuoOptionView's default selectors
        optionView.bind(option, this.context.getResources().getDrawable(icon));
        optionView.setPadding(-10,-10,-10,-10);


        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);

        return optionView;
    }
}
