package local.hal.st31.android.itarticlecollection90032;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentManager;

import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleAddActivity extends AppCompatActivity {
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/insertItArticle.php";
    private static final String DEBUG_LOG = "ItArticleCollection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendButtonClick(View view){
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etUrl = findViewById(R.id.etUrl);
        EditText etComment = findViewById(R.id.etComment);

        String title = etTitle.getText().toString();
        String url =  etUrl.getText().toString();
        String comment = etUrl.getText().toString();

        postAccess(ACCESS_URL, title, url, comment);
    }

    @UiThread
    private void postAccess(final String accessUrl, final String title, final String articleUrl, final String comment){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundPostAccess backgroundPostAccess = new BackgroundPostAccess(handler, accessUrl, title, articleUrl, comment);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundPostAccess);
    }

    private class BackgroundPostAccess implements Runnable {
        private final Handler _handler;
        private final String _accessUrl;
        private final String _title;
        private final String _articleUrl;
        private final String _comment;
        private static final String LASTNAME = "山根";
        private static final String FIRSTNAME = "功大";
        private static final String STUDENTID = "90032";
        private static final String SEATNO = "44";


        public BackgroundPostAccess(Handler handler, String accessUrl, String title, String articleUrl, String comment){
            _handler = handler;
            _accessUrl = accessUrl;
            _title = title;
            _articleUrl = articleUrl;
            _comment = comment;
        }

        @WorkerThread
        @Override
        public void run(){
            String postData = "title=" + _title
                    + "&url=" + _articleUrl
                    + "&comment=" + _comment
                    + "&lastname=" + LASTNAME
                    + "&firstname=" + FIRSTNAME
                    + "&studentid=" + STUDENTID
                    + "&seatno=" + SEATNO;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            boolean success = false;

            try {
                URL url = new URL(_accessUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException("ステータスコード:" + status);
                }
                is = con.getInputStream();
                result = is2String(is);
                success = true;
            }
            catch(SocketTimeoutException ex){
                result = getString(R.string.msg_err_timeout);
                Log.e(DEBUG_LOG, "タイムアウト", ex);
            }
            catch(MalformedURLException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_LOG, "URL変換失敗", ex);
            }
            catch(IOException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_LOG, "通信失敗", ex);
            }
            finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(is != null){
                        is.close();
                    }
                }
                catch (IOException ex){
                    result = getString(R.string.msg_err_send);
                    Log.e(DEBUG_LOG, "InputStream解放失敗", ex);
                }
            }
            PostExecutor postExecutor = new PostExecutor(result,  success);
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

    private class PostExecutor implements Runnable{
        private final String _result;
        private final boolean _success;

        PostExecutor(String result, boolean success){
            _result = result;
            _success = success;
        }

        @UiThread
        @Override
        public void  run(){
            String message = _result;
            if(_success){
                String title = "";
                String url = "";
                String comment = "";
                String name = "";
                String studentid = "";
                String seatno = "";
                String status = "";
                String msg = "";
                String timestamp = "";
                try{
                    JSONObject rootJSON = new JSONObject(_result);
                    title = rootJSON.getString("title");
                    url = rootJSON.getString("url");
                    comment = rootJSON.getString("comment");
                    name = rootJSON.getString("name");
                    studentid = rootJSON.getString("studentid");
                    seatno = rootJSON.getString("seatno");
                    status = rootJSON.getString("status");
                    msg = rootJSON.getString("msg");
                    timestamp = rootJSON.getString("timestamp");
                }
                catch(JSONException ex){
                    message = getString(R.string.msg_err_parse);
                    Log.e(DEBUG_LOG, "JSON解析失敗", ex);
                }
                if(Integer.parseInt(status) == 1){
                    finish();
                } else {
                    ErrorInfoDialog dialog = new ErrorInfoDialog();
                    Bundle extras = new Bundle();
                    extras.putString("msg", msg);
                    dialog.setArguments(extras);
                    FragmentManager manager = getSupportFragmentManager();
                    dialog.show(manager, "ErrorInfoDialog");
                }
            }
        }
    }
}