package com.liveness.dflivenesslibrary.feature;

import android.content.Context;
import android.content.Intent;

import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.liveness.DFSilentLivenessActivity;
import com.liveness.dflivenesslibrary.result.DFSilentResultActivity;


public class SilentLivenessProcess extends FeatureProcessBase {
    @Override
    protected Class getActivityClass() {
        return DFSilentLivenessActivity.class;
    }

    @Override
    protected void addIntentExtra(Intent intent, Context context) {
        intent.putExtra(DFSilentLivenessActivity.KEY_ANTI_HACK, getAntiHack());
        intent.putExtra(DFSilentLivenessActivity.KEY_DETECT_IMAGE_RESULT, true);
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_HAS_FACE, context.getString(R.string.string_liveness_has_face_and_holdstill_hint));
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_NO_FACE, context.getString(R.string.string_liveness_no_face_hint));
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_FACE_NOT_VALID, context.getString(R.string.liveness_face_not_valid_hint));
    }

    @Override
    protected Class getResultActivityClass() {
        return DFSilentResultActivity.class;
    }
}
