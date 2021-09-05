package local.hal.st31.android.lifecyclesample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * ST31 Androidサンプル07 Activityのライフサイクル
 *
 * 第2画面表示アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class SubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCycleSample", "Sub onCreate() called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    
        Button btPrevious = findViewById(R.id.btPrevious);
        btPrevious.setOnClickListener(new ButtonClickListener());
    }
    
    @Override
    protected void onStart() {
        Log.i("LifeCycleSample", "Sub onStart() called.");
        super.onStart();
    }
    
    @Override
    protected void onRestart() {
        Log.i("LifeCycleSample", "Sub onRestart() called.");
        super.onRestart();
    }
    
    @Override
    protected void onResume() {
        Log.i("LifeCycleSample", "Sub onResume() called.");
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        Log.i("LifeCycleSample", "Sub onPause() called.");
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        Log.i("LifeCycleSample", "Sub onStop() called.");
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        Log.i("LifeCycleSample", "Sub onDestory() called.");
        super.onDestroy();
    }
    
    /**
     * ボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
