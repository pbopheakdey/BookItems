package bo.assignment.app.bookitems.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class mProgressDialog {

    public Context context;
    public ProgressDialog progressDialog;

    public mProgressDialog(Context context){
        this.context = context;
    }

    public void build(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Data loading...");
        progressDialog.setCancelable(false);
    }
    public void mShow() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void mHide() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
