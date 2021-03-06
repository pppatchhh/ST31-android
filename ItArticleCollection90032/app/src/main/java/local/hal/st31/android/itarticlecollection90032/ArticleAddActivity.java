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
import android.view.Menu;
import android.view.MenuInflater;
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_activity_article_add,  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                EditText etTitle = findViewById(R.id.etTitle);
                EditText etUrl = findViewById(R.id.etUrl);
                EditText etComment = findViewById(R.id.etComment);

                String title = etTitle.getText().toString();
                String url =  etUrl.getText().toString();
                String comment = etComment.getText().toString();

                postAccess(ACCESS_URL, title, url, comment);
        }
        return super.onOptionsItemSelected(item);
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
        private static final String LASTNAME = "??????";
        private static final String FIRSTNAME = "??????";
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
                    throw new IOException("????????????????????????:" + status);
                }
                is = con.getInputStream();
                result = is2String(is);
                success = true;
            }
            catch(SocketTimeoutException ex){
                result = getString(R.string.msg_err_timeout);
                Log.e(DEBUG_LOG, "??????????????????", ex);
            }
            catch(MalformedURLException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_LOG, "URL????????????", ex);
            }
            catch(IOException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_LOG, "????????????", ex);
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
                    Log.e(DEBUG_LOG, "InputStream????????????", ex);
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
                String status = "";
                String msg = "";
                try{
                    JSONObject rootJSON = new JSONObject(_result);
                    status = rootJSON.getString("status");
                    msg = rootJSON.getString("msg");
                }
                catch(JSONException ex){
                    message = getString(R.string.msg_err_parse);
                    Log.e(DEBUG_LOG, "JSON????????????", ex);
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