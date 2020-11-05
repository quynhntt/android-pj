package com.notpad.medate.adapters;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.notpad.medate.MessageActivity;
import com.notpad.medate.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mLastText, mMatchName;
    public ImageView mMatchImage;
    public String imageUrl;
    public String mCoupleID;
    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mLastText = (TextView) itemView.findViewById(R.id.tvMatchesId);
        mMatchName = (TextView) itemView.findViewById(R.id.tvMatchesName);
        mMatchImage = (ImageView) itemView.findViewById(R.id.imgMatchesImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), MessageActivity.class);
        Bundle b = new Bundle();
        b.putString("matchID", mCoupleID);
        b.putString("matchName", mMatchName.getText().toString());
        b.putString("matchImage", imageUrl);
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }

}
