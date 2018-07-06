package com.alphonso.thingword.zhibao.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alphonso.thingword.zhibao.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangChang on 2016/5/15.
 */
public class TextFragment extends Fragment {
    TextView text;
    Toolbar toolbar;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        text = (TextView) view.findViewById(R.id.text);
        toolbar = view.findViewById(R.id.text_toolbar);
        btn = view.findViewById(R.id.text_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text.setText(getArguments().getString("ARGS"));

        toolbar.setTitle(getArguments().getString("ARGS"));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("aaaaa","btn click");
            }
        });

    }

    public static TextFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        TextFragment fragment = new TextFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
