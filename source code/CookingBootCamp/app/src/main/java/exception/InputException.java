package exception;

import android.app.Activity;
import android.widget.Toast;


/**
 * This class file is to define different exceptions and fix them in another java file.
 * Created by qiuyi on 11/13/15.
 */
public class InputException extends Exception{

    /**
     * This method is to show the error.
     * @param activity activity
     * @param msg msg
     */
    public void show(Activity activity,String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * This method is to call another class to handle this error,
     * and in this project, an alertDialog is used to handle this.
     * @param activity activity
     * @param msg msg
     */
    public void fix(Activity activity, String msg) {
        FixException fixException = new FixException();
        fixException.fix(activity, msg);
    }

}
