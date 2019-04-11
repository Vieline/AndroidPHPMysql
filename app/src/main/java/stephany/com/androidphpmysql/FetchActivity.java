package stephany.com.androidphpmysql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class FetchActivity extends AppCompatActivity {
     @BindView(R.id.inputID)
    EditText inputID;

     @BindView(R.id.textViewTitle)
    TextView txtTitle;

     @BindView(R.id.textViewAuthor)
    TextView txtAuthor;

     @BindView(R.id.textViewYear)
    TextView txtYear;

     @BindView(R.id.textViewCost)
    TextView txtCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonSearch)
    public void fetch()
    {
        if (! new Network().isInternetAvailable())
        {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = inputID.getText().toString().trim();
        if (id.isEmpty())
            return;
        RequestParams params=new RequestParams();
        params.put("id", id);

        String url="http://http://fa187e29.ngrok.io/apis/fetch.php";

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(FetchActivity.this, "Could not communicate with the server. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject mainObject=new JSONObject(responseString);
                    String status = mainObject.getString("status");
                    if (status.equalsIgnoreCase("success"))
                    {
                        JSONObject data = mainObject.getJSONObject("data");
                        String title = data.getString("title");
                        String author = data.getString("author");
                        String year = data.getString("year");
                        String cost = data.getString("cost");

                        txtTitle.setText(title);
                        txtAuthor.setText(author);
                        txtYear.setText(year);
                        txtCost.setText(cost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
