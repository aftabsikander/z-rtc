package net.zedge.ringtonecreator.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LayoutUtils {
    public static float convertDpToPixel(DisplayMetrics displayMetrics, float dp){
        float px = dp * (displayMetrics.densityDpi/160f);
        return px;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (id > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(id);
        }
        return statusBarHeight;
    }

    public static void setEditTextError(EditText editText, String errorMessage, boolean setFocus) {
        editText.setError(errorMessage);
        if (setFocus) {
            showKeyboard(editText);
        }
    }

    public static boolean validateInputField(EditText editText, String errorMessage, boolean setFocus) {
        if (TextUtils.isEmpty(editText.getText())) {
            setEditTextError(editText, errorMessage, setFocus);
            return false;
        }
        return true;
    }

    public static void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void hideKeyboard(View view) {
        view.clearFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if(view != null) {
            hideKeyboard(view);
        }
    }



    public static void setColorToProgressBar(Context context, ProgressBar progressBar, int whichColor) {
        if (progressBar.getIndeterminateDrawable() != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(
                    context.getResources().getColor(whichColor), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
