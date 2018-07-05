package com.alphonso.thingword.zhibao.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.alphonso.thingword.zhibao.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangChang on 2016/5/15.
 */
public class TextFragment extends Fragment {
    TextView text;
    WheelPicker wheelPicker;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        text = (TextView) view.findViewById(R.id.text);
        wheelPicker = (WheelPicker) view.findViewById(R.id.wheelpicker);
        btn = (Button)view.findViewById(R.id.button);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Integer> data = new ArrayList<>();
        for (int i = 1000; i < 3000; i++)
            data.add(i);
        wheelPicker.setData(data);
        text.setText(getArguments().getString("ARGS"));


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> data = new ArrayList<>();
                for (int i = 5000; i <7000; i++)
                    data.add(i);
                wheelPicker.setData(data);
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
