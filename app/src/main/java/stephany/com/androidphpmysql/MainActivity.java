package stephany.com.androidphpmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.inputTitle)
    EditText inputTitle;

    @BindView(R.id.inputAuthor)
    EditText inputAuthor;

    @BindView(R.id.inputYear)
    EditText inputYear;

    @BindView(R.id.inputCost)
    EditText inputCost;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progress = new ProgressDialog(this);
        progress.setMessage("saving.....");
    }

    @OnClick(R.id.buttonSave)
    public void save()
    {
        if (! new Network().isInternetAvailable())
        {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
     String title = inputTitle.getText().toString().trim();
     String author = inputAuthor.getText().toString().trim();
     String cost = inputCost.getText().toString().trim();
     String year= inputYear.getText().toString().trim();
     if (title.isEmpty() || author.isEmpty() || cost.isEmpty() || year.isEmpty())
     {
         Toast.makeText(this, "Fill in all values", Toast.LENGTH_SHORT).show();
         return;
     }
        RequestParams params=new RequestParams();
     params.put("title", title);
     params.put("author", author);
     params.put("year", year);
     params.put("cost", cost);

     String url="http://fa187e29.ngrok.io/apis/save.php";

        AsyncHttpClient client = new AsyncHttpClient();
`
        progress.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, "Failed To Send. Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, "Book Saved Successfully.", Toast.LENGTH_SHORT).show();
                inputAuthor.setText("");
                inputCost.setText("");
                inputYear.setText("");
                inputTitle.setText("");
            }
        });

    }

    @OnClick(R.id.buttonFetch)
    public void fetch()
    {
        Intent x =new Intent(this, FetchActivity.class);
        startActivity(x);
    }
}
