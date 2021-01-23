package com.liveness.dflivenesslibrary.result.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dfsdk.liveness.DFLivenessSDK;
import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.result.fragment.view.CircleImageView;


public abstract class LivenessResultBaseFragment extends ResultFragmentBase {

    private CircleImageView mCivDetectImage;

    @Override
    public int getTitleString() {
        return R.string.string_activity_bottom_liveness;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_result_liveness_fragment_main;
    }

    @Override
    protected void initialize() {
        mCivDetectImage = mRootView.findViewById(R.id.id_ci_image);
//        civ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showResultImage(true, 0);
//            }
//        });
        if (mResult != null) {
            DFLivenessSDK.DFLivenessImageResult[] imageResults = mResult.getLivenessImageResults();
            if (imageResults != null && imageResults.length > 0) {
                byte[] jpeg = imageResults[0].detectImage;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                //TODO
                //活体检测的人脸图片
                Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);
                mCivDetectImage.setImageBitmap(bitmap);//显示人脸图片
            }

            updateAntiHackResult(mResult);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCivDetectImage != null) {
            mCivDetectImage.releaseResource();
        }
    }
}
