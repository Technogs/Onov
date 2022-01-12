package com.application.onovapplication.debate.vlive.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.application.onovapplication.R;
import com.application.onovapplication.debate.vlive.ui.BaseActivity;


public class PrivacyActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        hideStatusBar(true);
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        View topLayout = findViewById(R.id.activity_privacy_title_layout);
        if (topLayout != null) {
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams)
                            topLayout.getLayoutParams();
            params.topMargin += systemBarHeight;
            topLayout.setLayoutParams(params);
        }
    }
}
