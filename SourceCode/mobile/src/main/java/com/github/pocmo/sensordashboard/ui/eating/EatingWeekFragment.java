package com.github.pocmo.sensordashboard.ui.eating;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.pocmo.sensordashboard.R;
import com.github.pocmo.sensordashboard.Server.EatingWeekData;
import com.github.pocmo.sensordashboard.Server.ServerDownload;
import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.vo.Graph;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraphVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.handstudio.android.hzgrapherlib.graphview.CurveGraphView;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraph;

public class EatingWeekFragment  extends Fragment {
    private ViewGroup eatingWeekGraphView;
    private View rootView;
    private ArrayList<HashMap<String, String>> week;
    private EatingWeekData weekData =  new EatingWeekData();
    private static final String TAG_DATE = "eating_date";
    private static final String TAG_COUNT = "eating_count";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.eating_week_fragment, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eatingWeekGraphView = (ViewGroup) rootView.findViewById(R.id.eatingWeekGraphView);

        //   setCurveGraph();
    }

    private void setCurveGraph() {
        //all setting
        CurveGraphVO vo = makeCurveGraphAllSetting();

        //default setting
//		CurveGraphVO vo = makeCurveGraphDefaultSetting();

        eatingWeekGraphView.addView(new CurveGraphView(this.getActivity(), vo));
    }

    /**
     * make simple Curve graph
     * @return
     */
    private CurveGraphVO makeCurveGraphDefaultSetting() {

        String[] legendArr = {"1", "2", "3", "4", "5"};
        float[] graph1 = {0, 0, 0, 0, 0};

        List<CurveGraph> arrGraph = new ArrayList<CurveGraph>();
        arrGraph.add(new CurveGraph("EATING", 0xaa66ff33, graph1));

        CurveGraphVO vo = new CurveGraphVO(legendArr, arrGraph);
        return vo;
    }

    /**
     * make Curve graph using options
     * @return
     */
    private CurveGraphVO makeCurveGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom 	= CurveGraphVO.DEFAULT_PADDING;
        int paddingTop 		= CurveGraphVO.DEFAULT_PADDING;
        int paddingLeft 	= CurveGraphVO.DEFAULT_PADDING;
        int paddingRight 	= CurveGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop 		= CurveGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight 	= CurveGraphVO.DEFAULT_MARGIN_RIGHT;

        //max value
        int maxValue 		= CurveGraphVO.DEFAULT_MAX_VALUE;

        //increment
        int increment 		= CurveGraphVO.DEFAULT_INCREMENT;

        TextView weekEating = (TextView) this.getActivity().findViewById(R.id.weekEating);

        week = weekData.getDataList();

        int size = week.size();
        String[] legendArr = new String[size];
        float[] graph1 = new float[size];

        for (int i = 0; i < size; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //String[] t = dateFormat.format(System.currentTimeMillis()).split("-");

            String arr[] = week.get(i).get(TAG_DATE).split("-");
            legendArr[i] = Integer.parseInt(arr[2]) + "";
            Log.d("week time", legendArr[i]);

            graph1[i] = Float.parseFloat(week.get(i).get(TAG_COUNT)) * (float)12;
            Log.d("week count",graph1[i]+"");


            if (maxValue < graph1[i])
                maxValue = (int) graph1[i];
        }


        List<CurveGraph> arrGraph = new ArrayList<CurveGraph>();
        arrGraph.add(new CurveGraph("eating", 0xaabaaca1, graph1, R.drawable.rice));


        CurveGraphVO vo = new CurveGraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, maxValue, increment, legendArr, arrGraph);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        vo.setGraphNameBox(new GraphNameBox());
        //set draw graph region
//		vo.setDrawRegion(true);

        //use icon
//		arrGraph.add(new CurveGraph("EATING",0xaa66ff33, graph1, R.drawable.rice));
//		arrGraph.add(new Graph(0xaa00ffff, graph2, R.drawable.icon2));
//		arrGraph.add(new Graph(0xaaff0066, graph3, R.drawable.icon3));

//		CurveGraphVO vo = new CurveGraphVO(
//				paddingBottom, paddingTop, paddingLeft, paddingRight,
//				marginTop, marginRight, maxValue, increment, legendArr, arrGraph, R.drawable.bg);
        return vo;
    }




    @Override
    public void onResume() {
        super.onResume();

        weekData = new EatingWeekData();
        setCurveGraph();
    }
}