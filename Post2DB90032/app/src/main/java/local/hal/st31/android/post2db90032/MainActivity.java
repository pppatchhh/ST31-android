package local.hal.st31.android.post2db90032;

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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/post2DB.php ";
    private static final String DEBUG_TAG = "Post2DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendButtonClick(View view){
        EditText etLastname = findViewById(R.id.etLastname);
        EditText etFirstname = findViewById(R.id.etFirstname);
        EditText etStudentid = findViewById(R.id.etStudentid);
        EditText etSeatno = findViewById(R.id.etSeatno);
        EditText etMessage = findViewById(R.id.etMessage);
        TextView tvResult = findViewById(R.id.tvResult);

        tvResult.setText("");

        String lastname = etLastname.getText().toString();
        String firstname = etFirstname.getText().toString();
        String studentid = etStudentid.getText().toString();
        String seatno = etSeatno.getText().toString();
        String message = etMessage.getText().toString();

        postAccess(ACCESS_URL, lastname, firstname, studentid, seatno, message);
    }

    @UiThread
    private void postAccess(final String url, final String lastname, final String firstname, final String studentid, final String seatno, final String message){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundPostAccess backgroundPostAccess = new BackgroundPostAccess(handler, url, lastname, firstname, studentid, seatno, message);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundPostAccess);
    }

    private class BackgroundPostAccess implements Runnable {
        private final Handler _handler;
        private final String _url;
        private final String _lastname;
        private final String _firstname;
        private final String _studentid;
        private final String _seatno;
        private final String _message;

        public BackgroundPostAccess(Handler handler, String url, String lastname, String firstname, String studentid, String seatno, String message){
            _handler = handler;
            _url = url;
            _lastname = lastname;
            _firstname = firstname;
            _studentid = studentid;
            _seatno = seatno;
            _message = message;
        }

        @WorkerThread
        @Override
        public void run() {
            String postData = "lastname=" + _lastname + "&firstname=" + _firstname + "&studentid=" + _studentid + "&seatno=" + _seatno + "&message=" + _message;
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
                if(status != 200){
                    throw new IOException("ステータスコード：" + status);
                }
                is = con.getInputStream();

                result = is2String(is);
                success = true;
            }
            catch(SocketTimeoutException ex){
                result = getString(R.string.msg_err_timeout);
                Log.e(DEBUG_TAG, "タイムアウト", ex);
            }
            catch(MalformedURLException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            }
            catch (IOException ex){
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "通信失敗", ex);
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
                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                }
            }
            PostExecutor postExecutor = new PostExecutor(result, success);
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

        private class PostExecutor implements Runnable{
            private final String _result;
            private final boolean _success;

            PostExecutor(String result, boolean success){
                _result = result;
                _success = success;
            }

            @UiThread
            @Override
            public void run(){
                String message = _result;
                if(_success){
                    String name = "";
                    String studentid = "";
                    String seatno = "";
                    String status = "";
                    String msg = "";
                    String serialno = "";
                    String timestamp = "";
                    try {
                        JSONObject rootJSON = new JSONObject(_result);
                        name = rootJSON.getString("name");
                        studentid = rootJSON.getString("studentid");
                        seatno = rootJSON.getString("seatno");
                        status = rootJSON.getString("status");
                        msg = rootJSON.getString("msg");
                        serialno = rootJSON.getString("serialno");
                        timestamp = rootJSON.getString("timestamp");
                    }
                    catch(JSONException ex){
                        message = getString(R.string.msg_err_parse);
                        Log.e(DEBUG_TAG, "JSON解析失敗", ex);
                    }
                    message = getString(R.string.dlg_msg_name) + name + "\n"
                            + getString(R.string.dlg_msg_studentid) + studentid + "\n"
                            + getString(R.string.dlg_msg_seatno) + seatno + "\n"
                            + getString(R.string.dlg_msg_msg) + msg;
                }
                TextView tvResult = findViewById(R.id.tvResult);
                tvResult.setText(message);
            }
        }

    }
}