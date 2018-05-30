package com.renyu.keyboarddemo;

import android.app.Application;
import android.content.Intent;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by admin-2 on 2018/4/19.
 */

public class SampleApplication  extends TinkerApplication {

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.renyu.keyboarddemo.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

}
