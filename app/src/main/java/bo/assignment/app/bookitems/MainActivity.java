package bo.assignment.app.bookitems;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bo.assignment.app.bookitems.activity.BookDetailActivity;
import bo.assignment.app.bookitems.adapter.CardViewAdapter;
import bo.assignment.app.bookitems.model.Book;
import bo.assignment.app.bookitems.utils.AppController;
import bo.assignment.app.bookitems.utils.Constance;
import bo.assignment.app.bookitems.utils.mProgressDialog;

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnMakeObjectRequest, btnMakeArrayRequest;

    private List<Book> bookList;
    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    private mProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myProgressDialog = new mProgressDialog(MainActivity.this);
        myProgressDialog.build();

        bookList = new ArrayList<>();

        requestJsonArray();
        init();

    }

    public void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcl);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CardViewAdapter(MainActivity.this, bookList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new CardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("tag", bookList.get(position).getId() + " : click: " + position);
                BookDetailActivity.initiate(getApplicationContext(), bookList.get(position).getId(), bookList.get(position).getTitle());
            }
        });
    }



    private void requestJsonArray() {

        myProgressDialog.mShow();
        JsonArrayRequest req = new JsonArrayRequest(Constance.urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                String id = person.getString("id");
                                String title = person.getString("title");
                                String link = person.getString("link");

                                addItemIntoList(id, title, link);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        mAdapter.notifyDataSetChanged();

                        myProgressDialog.mHide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                myProgressDialog.mHide();
            }
        });

        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(req);
    }

    private void addItemIntoList(String id, String title, String link) {
        Book b = new Book();
        b.setId(id);
        b.setLink(link);
        b.setTitle(title);
        bookList.add(b);
    }

}
