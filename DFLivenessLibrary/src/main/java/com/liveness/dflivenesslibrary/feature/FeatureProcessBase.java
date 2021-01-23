package com.liveness.dflivenesslibrary.feature;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.liveness.dflivenesslibrary.result.ResultActivity;

import static android.app.Activity.RESULT_OK;


public abstract class FeatureProcessBase {
    protected abstract Class getActivityClass();

    protected void addIntentExtra(Intent intent, Context context) {
    }

    protected Bundle generateBundleExtra(Context context) {
        return null;
    }

    protected int getResultCode() {
        return 1;
    }

    protected boolean getAntiHack() {
        return true;
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(activity, getResultActivityClass());
            intent.putExtra(ResultActivity.KEY_ANTI_HACK, getAntiHack());
            activity.startActivity(intent);
        }
    }

    protected abstract Class getResultActivityClass();

    public void gotoActivity(Activity activity) {
        Class resultCls = getActivityClass();
        if (resultCls == null) {
            Log.e("", "Target class is null!");
            return;
        }
        Intent intent = new Intent(activity, resultCls);
        addIntentExtra(intent, activity);
        Bundle bundle = generateBundleExtra(activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        int resultCode = getResultCode();
        if (resultCode != -1) {
            activity.startActivityForResult(intent, resultCode);
        } else {
            activity.startActivity(intent);
        }
    }
}
