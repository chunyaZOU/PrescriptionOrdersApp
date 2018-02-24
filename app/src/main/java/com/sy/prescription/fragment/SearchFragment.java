package com.sy.prescription.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.adapter.OrderAdapter;
import com.sy.prescription.model.OrderInfo;
import com.sy.prescription.view.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener, XRecyclerView.LoadingListener {


    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_card_num)
    EditText tvCardNum;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_success)
    TextView tvSuccess;
    @BindView(R.id.lv_orders)
    XRecyclerView lvOrders;

    private boolean isStartDate = true;

    private OrderAdapter mAdapter;
    private List<OrderInfo> mOrderList;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        mOrderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.cardNum = "100000" + i;
            orderInfo.isSuccess = i % 2 == 0;
            orderInfo.commission = i * 10;
            mOrderList.add(orderInfo);
        }

        lvOrders.hasMore(true);
        lvOrders.setLoadingMoreEnabled(true);
        lvOrders.setPullRefreshEnabled(true);
        lvOrders.setLoadingListener(this);
        lvOrders.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mAdapter = new OrderAdapter(getActivity(), mOrderList);
        lvOrders.setAdapter(mAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.tv_start_date)
    public void onTvStartDateClicked() {
        isStartDate = true;
        showDate();
    }


    @OnClick(R.id.tv_end_date)
    public void onTvEndDateClicked() {
        isStartDate = false;
        showDate();
    }

    private void showDate() {
        //获取当前年月日
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);//当前年
        int month = calendar.get(Calendar.MONTH);//当前月
        int day = calendar.get(Calendar.DAY_OF_MONTH);//当前日
        //new一个日期选择对话框的对象,并设置默认显示时间为当前的年月日时间.
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), mdateListener, year, month, day);
        dialog.show();
    }

    /**
     * 日期选择的回调监听
     */
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
            // TODO: 2017/8/17 这里有选择后的日期回调,根据具体要求写不同的代码,我就直接打印了
            //Log.i(TAG, "年" +years+ "月" +monthOfYear+ "日"+dayOfMonth);//这里月份是从0开始的,所以monthOfYear的值是0时就是1月.以此类推,加1就是实际月份了.
            String date = years + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            if (isStartDate) {
                tvStartDate.setText(date);
            } else {
                tvEndDate.setText(date);
            }
        }
    };

    @OnClick(R.id.tv_search)
    public void onTvSearchClicked() {

    }

    @OnClick(R.id.tv_success)
    public void onTvSuccessClicked() {
        if (mAdapter.getmSuccessFlag() == OrderAdapter.FLAG_ALL || mAdapter.getmSuccessFlag() == OrderAdapter.FLAG_N) {
            mAdapter.setmSuccessFlag(OrderAdapter.FLAG_Y);
            tvSuccess.setText("是否成交(是)");
        } else if (mAdapter.getmSuccessFlag() == OrderAdapter.FLAG_Y) {
            mAdapter.setmSuccessFlag(OrderAdapter.FLAG_N);
            tvSuccess.setText("是否成交(否)");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mAdapter.setmSuccessFlag(OrderAdapter.FLAG_ALL);
        tvSuccess.setText("是否成交");
        mAdapter.notifyDataSetChanged();
        lvOrders.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        lvOrders.loadMoreComplete();
    }
}
