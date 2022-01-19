package local.hal.st31.android.itarticlecollection90032;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleDetailActivity extends AppCompatActivity {
    private int  _selectedId = 0;
    private String _articleUrl = "";
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/getOneArticle.php";
    private static  final String DEBUG_LOG = "ItArticleCollection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            _selectedId = extras.getInt("id");
        }
        recieveArticleDetail(ACCESS_URL, _selectedId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                Uri uri = Uri.parse(_articleUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @UiThread
    private void recieveArticleDetail(final String url, final int id){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        ArticleDetailBackgroundReceiver backgroundReceiver = new ArticleDetailBackgroundReceiver(handler, url, id);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundReceiver);
    }

    private class ArticleDetailBackgroundReceiver implements Runnable {
        private final Handler _handler;
        private final String _url;
        private final int _id;

        public ArticleDetailBackgroundReceiver(Handler handler, String url, int id){
            _handler = handler;
            _url = url;
            _id = id;
        }

        @WorkerThread
        @Override
        public void run(){
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try {
                URL url = new URL(_url + "?id=" + String.valueOf(_id));
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                con.setRequestMethod("GET");
                con.connect();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException();
                }
                is = con.getInputStream();
                result = is2String(is);
            }
            catch(MalformedURLException ex){
                Log.e(DEBUG_LOG, "URL変換失敗", ex);
            }
            catch (SocketTimeoutException ex){
                Log.e(DEBUG_LOG, "通信タイムアウト", ex);
            }
            catch(IOException ex){
                Log.e(DEBUG_LOG, "通信失敗", ex);
            }
            finally {
                if(con != null){
                    con.disconnect();
                }
                if(is != null){
                    try{
                        is.close();
                    }
                    catch(IOException ex){
                        Log.e(DEBUG_LOG, "InputStream解放失敗", ex);
                    }
                }
            }
            ArticleDetailPostExecutor postExecutor = new ArticleDetailPostExecutor(result);
            _handler.post(postExecutor);
        }

        private String is2String(InputStream is) throws IOException{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))){
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    private class ArticleDetailPostExecutor implements Runnable {
        private final String _result;

        public ArticleDetailPostExecutor(String result){_result = result; }

        @UiThread
        @Override
        public void run(){
            String id = "";
            String title = "";
            String url = "";
            String comment = "";
            String studentId = "";
            String seatNo = "";
            String lastName = "";
            String firstName = "";
            String createdAt =  "";
            try {
                JSONObject rootJSON = new JSONObject(_result).getJSONArray("article").getJSONObject(0);
                id = rootJSON.getString("id");
                title = rootJSON.getString("title");
                url = rootJSON.getString("url");
                comment = rootJSON.getString("comment");
                studentId = rootJSON.getString("student_id");
                seatNo = rootJSON.getString("seat_no");
                lastName = rootJSON.getString("last_name");
                firstName = rootJSON.getString("first_name");
                createdAt = rootJSON.getString("created_at");
            }
            catch(JSONException ex){
                Log.e(DEBUG_LOG, "JSON解析失敗", ex);
            }
            TextView tvTitle = findViewById(R.id.tvTitleDetail);
            TextView tvId = findViewById(R.id.tvArticleIdDetail);
            TextView tvUrl = findViewById(R.id.tvUrlDetail);
            TextView tvComment = findViewById(R.id.tvCommentDetail);
            TextView tvStudentId = findViewById(R.id.tvStudentidDetail);
            TextView tvSeatNo = findViewById(R.id.tvSeatnoDetail);
            TextView tvLastName = findViewById(R.id.tvDetailLastName);
            TextView tvFirstName = findViewById(R.id.tvFirstNameDetail);
            TextView tvCreatedAt = findViewById(R.id.tvCreatedAtDetail);

            tvTitle.setText(title);
            tvId.setText(id);
            tvUrl.setText(url);
            tvComment.setText(comment);
            tvStudentId.setText(studentId);
            tvSeatNo.setText(seatNo);
            tvLastName.setText(lastName);
            tvFirstName.setText(firstName);
            tvCreatedAt.setText(createdAt);

            _articleUrl = url;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_activity_article_detail, menu);
        return true;
    }
}