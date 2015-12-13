package exception;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * This class file is to fix occurred exceptions.
 * Normally it comes from user input.
 * Created by qiuyi on 11/13/15.
 */
public class FixException {
    /**
     * This method is to to use a AlterDialog to help user to check its input.
     * @param activity "activity"
     * @param msg msg
     */
    public void fix(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Back",null);
        builder.show();
    }
}
