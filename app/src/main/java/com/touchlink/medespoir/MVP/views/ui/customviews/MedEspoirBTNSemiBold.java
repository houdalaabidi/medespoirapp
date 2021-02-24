package com.touchlink.medespoir.MVP.views.ui.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

public class MedEspoirBTNSemiBold extends MaterialButton {
    // LOLLIPOP version implementation
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MedEspoirBTNSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MedEspoirBTNSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MedEspoirBTNSemiBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nunitosanssemibold.ttf");
            setTypeface(tf);
        }
    }
}
