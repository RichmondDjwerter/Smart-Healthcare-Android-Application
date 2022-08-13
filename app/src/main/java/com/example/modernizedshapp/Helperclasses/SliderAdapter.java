package com.example.modernizedshapp.Helperclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.modernizedshapp.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[]= {
            R.drawable.ai_therapy,
            R.drawable.disease_prediction,
            R.drawable.prediction,
            R.drawable.emergency_call,
            R.drawable.therapy
    };

    int headings[]={
            R.string.First_Slide_tittle,
            R.string.Second_Slide_tittle,
            R.string.Third_Slide_tittle,
            R.string.Fourth_Slide_tittle,
            R.string.fifth_Slide_tittle
    };

    int descriptions[]={
            R.string.First_Slide_Desc,
            R.string.Second_Slide_Desc,
            R.string.Third_Slide_Desc,
            R.string.Fourth_Slide_Desc,
            R.string.fifth_Slide_Desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout,container, false);

        //Hooks
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slide_heading);
        TextView description = view.findViewById(R.id.slider_description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        description.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
