package com.touchlink.medespoir.MVP.views.ui.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MedEspoirTVSemiBold extends AppCompatTextView {
    // LOLLIPOP version implementation
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MedEspoirTVSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MedEspoirTVSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MedEspoirTVSemiBold(Context context) {
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
