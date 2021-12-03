package local.hal.st31.android.postsample;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ST31 Androidサンプル16 POST
 *
 * 画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
    /**
     * ログに記載するタグ用の文字列。
     */
    private static final String DEBUG_TAG = "PostSample";
    /**
     * POST先のURL。
     */
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/post2Json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 送信ボタンがクリックしたときの処理メソッド。
     *
     * @param view Viewオブジェクト。
     */
    public void sendButtonClick(View view) {
        EditText etName = findViewById(R.id.etName);
        EditText etComment = findViewById(R.id.etComment);
        TextView tvResult = findViewById(R.id.tvResult);

        tvResult.setText("");

        String name = etName.getText().toString();
        String comment = etComment.getText().toString();

        postAccess(ACCESS_URL, name, comment);
    }

    /**
     * 非同期でサーバにポストするメソッド。
     *
     * @param url ポスト先URL。
     * @param name ポストデータのうちの名前。
     * @param comment ポストデータのうちのコメント。
     */
    @UiThread
    private void postAccess(final String url, final String name, final String comment) {
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundPostAccess backgroundPostAccess = new BackgroundPostAccess(handler, url, name, comment);
        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundPostAccess);
    }

    /**
     * 非同期でサーバにポストするためのクラス。
     */
    private class BackgroundPostAccess implements Runnable {
        /**
         * ハンドラオブジェクト。
         */
        private final Handler _handler;
        /**
         * ポスト先URL。
         */
        private final String _url;
        /**
         * ポストデータのうちの名前。
         */
        private final String _name;
        /**
         * ポストデータのうちのコメント。
         */
        private final String _comment;

        /**
         * コンストラクタ。
         * 非同期でサーバにポストするのに必要な情報を取得する。
         *
         * @param handler ハンドラオブジェクト。
         * @param url ポスト先URL。
         * @param name ポストデータのうちの名前。
         * @param comment ポストデータのうちのコメント。
         */
        public BackgroundPostAccess(Handler handler, String url, String name, String comment) {
            _handler = handler;
            _url = url;
            _name = name;
            _comment = comment;
        }

        @WorkerThread
        @Override
        public void run() {
            String postData = "name=" + _name + "&comment=" + _comment;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            boolean success = false;

            try {
                URL url = new URL(_url);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if(status != 200) {
                    throw new IOException("ステータスコード:" + status);
                }
                is = con.getInputStream();

                result = is2String(is);
                success = true;
            }
            catch(SocketTimeoutException ex) {
                result = getString(R.string.msg_err_timeout);
                Log.e(DEBUG_TAG, "タイムアウト", ex);
            }
            catch(MalformedURLException ex) {
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            }
            catch(IOException ex) {
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }
            finally {
                if(con != null) {
                    con.disconnect();
                }
                try {
                    if(is != null) {
                        is.close();
                    }
                }
                catch(IOException ex) {
                    result = getString(R.string.msg_err_send);
                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                }
            }
            PostExecutor postExecutor = new PostExecutor(result, success);
            _handler.post(postExecutor);
        }

        /**
         * InputStreamオブジェクトを文字列に変換するメソッド。 変換文字コードはUTF-8。
         *
         * @param is 変換対象のInputStreamオブジェクト。
         * @return 変換された文字列。
         * @throws IOException 変換に失敗した時に発生。
         */
        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    /**
     * 非同期でポストした後にUIスレッドでその情報を表示するためのクラス。
     */
    private class PostExecutor implements Runnable {
        /**
         * 取得した文字列情報。
         * 取得に失敗した場合は、その内容を表す文字列。
         */
        private final String _result;
        /**
         * 通信に成功し、無事データを取得したかどうかを表す値。
         */
        private final boolean _success;

        /**
         * コンストラクタ。
         *
         * @param result 取得した文字列情報。
         * @param success 通信に成功し、無事データを取得したかどうかを表す値。
         */
        PostExecutor(String result, boolean success) {
            _result = result;
            _success = success;
        }

        @UiThread
        @Override
        public void run() {
            String message = _result;
            if(_success) {
                String name = "";
                String comment = "";
                try {
                    JSONObject rootJSON = new JSONObject(_result);
                    name = rootJSON.getString("name");
                    comment = rootJSON.getString("comment");
                }
                catch(JSONException ex) {
                    message = getString(R.string.msg_err_parse);
                    Log.e(DEBUG_TAG, "JSON解析失敗", ex);
                }
                message = getString(R.string.dlg_msg_name) + name + "\n" + getString(R.string.dlg_msg_comment) + comment;
            }
            TextView tvResult = findViewById(R.id.tvResult);
            tvResult.setText(message);
        }
    }
}
