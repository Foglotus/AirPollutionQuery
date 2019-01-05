package com.foglotus.airpollution.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.foglotus.airpollution.R;

public class Loading extends AlertDialog {
    private String tips;
    private TextView textView;
    public Loading(Context context,String tips) {
        super(context);
        this.tips = tips;
        View view = getLayoutInflater().inflate(R.layout.loading,null);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(context).getScreenHeight() * 0.1f));
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(context).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        setView(view);
        create();
        setCancelable(false);
    }

    public Loading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Loading(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void start(){
        textView = getWindow().findViewById(R.id.loading_msg);
        textView.setText(tips);
        show();
    }

    public void setTips(String tips) {
        this.tips = tips;
        if(isShowing())
            textView.setText(tips);
    }

    public void close(){
        if(isShowing())
            dismiss();
    }
}
