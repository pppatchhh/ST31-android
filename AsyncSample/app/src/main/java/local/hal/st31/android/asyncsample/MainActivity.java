package local.hal.st31.android.asyncsample;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 処理開始ボタンが押された時のイベント処理メソッド。
     * @param view 画面部品。
     */
    public void onStartButtonClick(View view) {
        asyncExecute();
    }

    /**
     * 処理経過を表示するTextViewに引数のメッセージ文字列を追記するメソッド。
     *
     * @param msg メッセージ文字列。
     */
    @UiThread
    private void addMsg(String msg) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        String msgCurrent = tvMsg.getText().toString();
        if(!msgCurrent.equals("")) {
            msgCurrent += "\n";
        }
        msgCurrent += msg;
        tvMsg.setText(msgCurrent);
    }

    /**
     * 処理経過を表示するTextViewをクリアするメソッド。
     */
    @UiThread
    private void clearMsg() {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText("");
    }

    /**
     * バックグラウンド処理を開始するメソッド。
     */
    @UiThread
    private void asyncExecute() {
        Calendar now = Calendar.getInstance();
        Timestamp nowTimestamp = new Timestamp(now.getTimeInMillis());
        clearMsg();
        addMsg("asyncExecuteメソッドを開始します。: " + nowTimestamp);
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundTask backgroundTask = new BackgroundTask(handler);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundTask);
        now = Calendar.getInstance();
        nowTimestamp = new Timestamp(now.getTimeInMillis());
        addMsg("asyncExecuteメソッドが終了しました。: " + nowTimestamp);
    }

    /**
     * バックグラウンド処理用クラス。
     */
    private class BackgroundTask implements Runnable {
        /**
         * UIスレッドを表すハンドラオブジェクト。
         */
        private final Handler _handler;

        /**
         * コンストラクタ。
         *
         * @param handler UIスレッドを表すハンドラオブジェクト。
         */
        public BackgroundTask(Handler handler) {
            _handler = handler;
        }

        @WorkerThread
        @Override
        public void run() {
            Calendar now = Calendar.getInstance();
            Timestamp nowTimestamp = new Timestamp(now.getTimeInMillis());
            StringBuffer sb = new StringBuffer();
            sb.append("ワーカースレッド内処理開始:");
            sb.append(nowTimestamp);
            sb.append("\n");
            int height = (int) Math.round(Math.random() * 9 + 1);
            int width = (int) Math.round(Math.random() * 9 + 1);
            int area = height * width;
            sb.append("バックグラウンド中での結果: 縦が");
            sb.append(height);
            sb.append("で横が");
            sb.append(width);
            sb.append("の面積は");
            sb.append(area);
            sb.append("です。\n");
            now = Calendar.getInstance();
            nowTimestamp = new Timestamp(now.getTimeInMillis());
            sb.append("ワーカースレッド内処理終了:");
            sb.append(nowTimestamp);
            String result = sb.toString();
            PostExecutor postExecutor = new PostExecutor(result);
            _handler.post(postExecutor);
        }
    }

    /**
     * バックグラウンドスレッドの終了後にUIスレッドで行う処理用クラス。
     */
    private class PostExecutor implements Runnable {
        /**
         * 処理結果を表す文字列。
         */
        private String _result;

        /**
         * コンストラクタ。
         *
         * @param result 処理結果文字列。
         */
        public PostExecutor(String result) {
            _result = result;
        }

        @UiThread
        @Override
        public void run() {
            addMsg(_result);
            Calendar now = Calendar.getInstance();
            Timestamp nowTimestamp = new Timestamp(now.getTimeInMillis());
            addMsg("PostExecutor内の処理が終了しました。: " + nowTimestamp);
        }
    }
}
