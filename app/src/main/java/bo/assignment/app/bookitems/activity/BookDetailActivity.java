package bo.assignment.app.bookitems.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import bo.assignment.app.bookitems.R;
import bo.assignment.app.bookitems.utils.AppController;
import bo.assignment.app.bookitems.utils.Constance;
import bo.assignment.app.bookitems.utils.mProgressDialog;

public class BookDetailActivity extends AppCompatActivity {

    private static String TAG = BookDetailActivity.class.getSimpleName();


    private ImageView imgBook;
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvAuthor;
    private TextView tvLink;

    private Toolbar mToolbar;

    private mProgressDialog myProgressDialog;

    public static void initiate(Context context) {
        BookDetailActivity.initiate(context, "","");
    }

    public static void initiate(Context context, String id, String bookTitle) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", bookTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        init();

    }

    public void init() {
        myProgressDialog = new mProgressDialog(BookDetailActivity.this);
        myProgressDialog.build();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setTitle(getBookTitle());

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgBook = (ImageView) findViewById(R.id.imgBook);
        tvTitle = (TextView) findViewById(R.id.vTitle);
        tvAuthor = (TextView) findViewById(R.id.vAuthor);
        tvPrice = (TextView) findViewById(R.id.vPrice);
        tvLink = (TextView) findViewById(R.id.vLink);


        String urlJsonObj = Constance.urlJsonArray + "/" + getId();
        requestJsonObject(urlJsonObj);


    }

    private void requestJsonObject(String urlJsonObj) {
        myProgressDialog.mShow();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlJsonObj, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
//                    String id = response.getString("id");
                    String link = response.getString("link");
                    String price = response.getString("price");
                    String author = response.getString("author");
                    String imageUrl = response.getString("image");
                    String title = response.getString("title");


                    tvAuthor.setText(author);
                    tvLink.setText(link);
                    tvPrice.setText(price);
                    tvTitle.setText(title);
                    Glide.with(getApplicationContext()).load(imageUrl).centerCrop()
                            .placeholder(R.mipmap.ic_launcher).into(imgBook);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                myProgressDialog.mHide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                myProgressDialog.mHide();
            }
        });
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

    private String getId() {
        String id = getIntent().getStringExtra("id");
        return id;
    }

    private String getBookTitle(){
        String title = getIntent().getStringExtra("title");
        return title;
    }


}
