package local.hal.st31.android.itarticlecollection90032;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ExecutorCompat;
import androidx.core.os.HandlerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/getItArticlesList.php";
    private static final String DEBUG_LOG = "ItArticleCollection";
    private List<Map<String, String>> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiveArticleList(ACCESS_URL);
    }

    @UiThread
    private void receiveArticleList(final String url){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        ArticleListBackgroundReceiver backgroundReceiver = new ArticleListBackgroundReceiver(handler, url);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundReceiver);
    }

    private class ArticleListBackgroundReceiver implements Runnable {
        private final Handler _handler;
        private final String _url;

        public ArticleListBackgroundReceiver(Handler handler, String url){
            _handler = handler;
            _url = url;
        }

        @WorkerThread
        @Override
        public void run(){
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try{
                URL url = new URL(_url);
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
            catch(SocketTimeoutException ex){
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
                    try {
                        is.close();
                    }
                    catch (IOException ex){
                        Log.e(DEBUG_LOG, "InputStream解放失敗", ex);
                    }
                }
            }
            ArticleListPostExecutor postExecutor = new ArticleListPostExecutor(result);
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

    private class ArticleListPostExecutor implements Runnable{
        private final String _result;

        public ArticleListPostExecutor(String result){
            _result = result;
        }

        @UiThread
        @Override
        public void run(){
            String status = "";
            String msg = "";
            JSONArray listJSONArray = new JSONArray();
            List<Map<String, String>> articleList = new ArrayList<>();
            try {
                JSONObject rootJSON = new JSONObject(_result);
                status = rootJSON.getString("status");
                msg = rootJSON.getString("msg");
                listJSONArray = rootJSON.getJSONArray("list");

                for(int i = 0; i < listJSONArray.length(); i ++){
                    Map<String, String> map = new HashMap<>();
                    map.put("title", listJSONArray.getJSONObject(i).getString("title"));
                    map.put("name", listJSONArray.getJSONObject(i).getString("last_name") + " " + listJSONArray.getJSONObject(i).getString("first_name"));
                    map.put("url", listJSONArray.getJSONObject(i).getString("url"));
                    articleList.add(map);
                }
            }
            catch (JSONException ex){
                Log.e(DEBUG_LOG, "JSON解析失敗", ex);
            }
            String[] from = {"title", "name"};
            int[] to = {android.R.id.text1, android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), articleList, android.R.layout.simple_list_item_2, from, to);
            ListView lvArticle = findViewById(R.id.lvArticle);
            lvArticle.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_activity_main,  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(MainActivity.this, ArticleAddActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}