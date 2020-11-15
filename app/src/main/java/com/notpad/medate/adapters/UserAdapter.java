package com.notpad.medate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.notpad.medate.R;
import com.notpad.medate.utils.Person;

import java.util.List;

public class UserAdapter extends ArrayAdapter<Person> {
    Context context;

    public UserAdapter(Context context, int resourceId, List<Person> people) {
        super(context, resourceId, people);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Person person = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_card, parent, false);
        }
        TextView mName = (TextView) convertView.findViewById(R.id.nameGuest);
        ImageView mImage = (ImageView) convertView.findViewById(R.id.imgGuest);

        mName.setText(person.getName());
        Glide.with(getContext()).load(person.getProfileImageUrl()).into(mImage);

        return convertView;
    }
}
