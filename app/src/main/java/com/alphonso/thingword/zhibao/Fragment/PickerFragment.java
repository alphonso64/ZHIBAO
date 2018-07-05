package com.alphonso.thingword.zhibao.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.alphonso.thingword.zhibao.PlantType;
import com.alphonso.thingword.zhibao.R;
import com.alphonso.thingword.zhibao.View.CameraActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by WangChang on 2016/5/15.
 */
public class PickerFragment extends Fragment {
    WheelPicker wheelPicker;
    WheelPicker wheelPicker_detail;
    List<String> ls_type;
    List<List<String>> ls_detail;
    PlantType plantType;
    Button btn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picker, container, false);
        wheelPicker = (WheelPicker) view.findViewById(R.id.wheelpicker);
        wheelPicker_detail = (WheelPicker) view.findViewById(R.id.wheelpicker2);
        btn = (Button) view.findViewById(R.id.button_camera);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Resources res =getResources();
        ls_type = Arrays.asList(res.getStringArray(R.array.类别));
        List<String> s1 =Arrays.asList(res.getStringArray(R.array.粮油));
        List<String> s2 =Arrays.asList(res.getStringArray(R.array.蔬菜));
        List<String> s3 =Arrays.asList(res.getStringArray(R.array.水果));
        List<String> s4 =Arrays.asList(res.getStringArray(R.array.经济作物));
        List<String> s5 =Arrays.asList(res.getStringArray(R.array.林木));

        ls_detail = new ArrayList<List<String>>();
        ls_detail.add(s1);
        ls_detail.add(s2);
        ls_detail.add(s3);
        ls_detail.add(s4);
        ls_detail.add(s5);

        plantType = new PlantType();
        plantType.setType_index(0);
        plantType.setType(ls_type.get(0));
        plantType.setDetail_index(0);
        plantType.setDetail(s1.get(0));

        wheelPicker.setData(ls_type);
        wheelPicker_detail.setData(s1);

        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {

                wheelPicker_detail.setData(ls_detail.get(position));
                wheelPicker_detail.setSelectedItemPosition(0);
                plantType.setType_index(position);
                plantType.setType(ls_type.get(position));
                plantType.setDetail_index(0);
                plantType.setDetail(ls_detail.get(position).get(0));

            }
        });

        wheelPicker_detail.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                plantType.setDetail_index(position);
                plantType.setDetail(ls_detail.get(plantType.getType_index()).get(position));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("type", plantType.getType());
                editor.putInt("type_index",plantType.getType_index());
                editor.putString("detail", plantType.getDetail());
                editor.putInt("detail_index",plantType.getDetail_index());
                editor.commit();

                Intent intent = new Intent(getActivity(), CameraActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    public static PickerFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
