package com.sy.prescription.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.cloudcommune.yhonline.imgpicker.ImagePicker;
import com.cloudcommune.yhonline.imgpicker.bean.ImageItem;
import com.cloudcommune.yhonline.imgpicker.ui.ImageGridActivity;
import com.sy.prescription.PhotoActivity;
import com.sy.prescription.R;
import com.sy.prescription.adapter.PhotoAdapter;
import com.sy.prescription.util.ResourceUtil;
import com.sy.prescription.util.ToastUtil;
import com.sy.prescription.view.widget.ActionSheetPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrescriptionFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final int MY_PERMISSIONS_WRITE = 3;// 相册
    public static final int MY_PERMISSIONS_CAMERA = 4;// 拍照
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final String TAKE_PHOTO = "take_photo";// 从相册中选择
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.et_order)
    EditText etOrder;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private PhotoAdapter mAdapter;
    private ArrayList<String> mImgPaths;

    private ArrayList<ImageItem> images = null;

    public PrescriptionFragment() {
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
    public static PrescriptionFragment newInstance(String param1, String param2) {
        PrescriptionFragment fragment = new PrescriptionFragment();
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
        final View view = inflater.inflate(R.layout.fragment_prescription, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        mImgPaths = new ArrayList<>();
        mImgPaths.add(TAKE_PHOTO);
        mAdapter = new PhotoAdapter(mImgPaths, getActivity());
        gv.setAdapter(mAdapter);
        gv.setOnItemClickListener(this);
        gv.setOnItemLongClickListener(this);
    }

    /**
     * 5.0以上全屏，将搜索框下移一个statusbar高度
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //ScreenUtil.setMargins(llSearch, 0, ScreenUtil.getStatusBarHeight(getActivity()), 0, 0);
        }
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
    }


    public void chooseHeadImage() {
        ActionSheetPanel panel = new ActionSheetPanel(getActivity());
        ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
       /* item.id = String.valueOf(PrescriptionFragment.PHOTO_REQUEST_GALLERY);
        item.title = ResourceUtil.getString(R.string.photo_pick_panel_from_gallery);
        panel.addSheetItem(item);*/

        //item = new ActionSheetPanel.ActionSheetItem();
        item.id = String.valueOf(PrescriptionFragment.PHOTO_REQUEST_TAKEPHOTO);
        item.title = ResourceUtil.getString(R.string.photo_pick_panel_from_camera);
        panel.addSheetItem(item);

        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (String.valueOf(PrescriptionFragment.PHOTO_REQUEST_GALLERY).equals(id)) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        chooseFromGallery();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PrescriptionFragment.MY_PERMISSIONS_WRITE);
                    }
                } else if (String.valueOf(PrescriptionFragment.PHOTO_REQUEST_TAKEPHOTO).equals(id)) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        chooseFromCamera();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PrescriptionFragment.MY_PERMISSIONS_CAMERA);
                    }
                }
            }
        });
        panel.showPanel();
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        startActivityForResult(intent, 100);
    }

    private void chooseFromCamera() {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, 100);
    }

    private String imageUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                imageUrl = images.get(0).path;
                if (!TextUtils.isEmpty(imageUrl)) {
                    file = new File(imageUrl);
                }
                mImgPaths.remove(mImgPaths.size() - 1);
                mImgPaths.add(imageUrl);
                if (mImgPaths.size() < 3) {
                    mImgPaths.add(TAKE_PHOTO);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.show("没有获取到图片");
            }
        }
        uploadPhoto(file);
    }

    private void uploadPhoto(final File file) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoActivity.startAct(getActivity(), mImgPaths, position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mImgPaths.get(position).equals(TAKE_PHOTO)) {
            mImgPaths.remove(position);
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }
}