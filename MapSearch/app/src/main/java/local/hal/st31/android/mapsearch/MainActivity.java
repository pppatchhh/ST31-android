package local.hal.st31.android.mapsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    /**
     * 緯度を表すフィールド。
     */
    private int _latitude = 0;
    /**
     * 経度を表すフィールド。
     */
    private int _longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rewriteTvLatitude();
        rewriteTvLongitude();

        SeekBar sbLatitude = findViewById(R.id.sbLatitude);
        sbLatitude.setOnSeekBarChangeListener(new OnLatitudeSeekBarChangeListener());
        SeekBar sbLongitude = findViewById(R.id.sbLongitude);
        sbLongitude.setOnSeekBarChangeListener(new OnLongitudeSeekBarChangeListener());
    }

    /**
     * 地図検索ボタンがクリックされたときの処理。
     *
     * @param view 画面部品
     */
    public void onMapSearchButtonClick(View view) {
        try {
            TextView etKeyword = findViewById(R.id.etKeyword);
            String keyword = etKeyword.getText().toString();
            keyword = URLEncoder.encode(keyword, "UTF-8");
            Uri uri = Uri.parse("geo:0,0?q=" + keyword);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        catch(UnsupportedEncodingException ex) {
            Log.e("MapSearchActivity", "keyword変換失敗", ex);
        }
    }

    /**
     * 地図表示ボタンがクリックされた時の処理。
     *
     * @param view 画面部品
     */
    public void onMapShowButtonClick(View view) {
        double latitudeD = (double) _latitude;
        double longitudeD = (double) _longitude;
        String uriStr = "geo:" + latitudeD + "," + longitudeD;
        Uri uri = Uri.parse(uriStr);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * 緯度表示TextViewをフィールドの内容に合わせて書き換える処理。
     */
    private void rewriteTvLatitude() {
        TextView tvLatitude = findViewById(R.id.tvLatitude);
        String latitudeStr = getString(R.string.tv_latitude) + _latitude;
        tvLatitude.setText(latitudeStr);
    }

    /**
     * 経度表示TextViewをフィールドの内容に合わせて書き換える処理。
     */
    private void rewriteTvLongitude() {
        TextView tvLongitude = findViewById(R.id.tvLongitude);
        String longitudeStr = getString(R.string.tv_longitude) + _longitude;
        tvLongitude.setText(longitudeStr);
    }

    /**
     * 緯度選択SeekBarをスライドした時のリスナクラス。
     */
    private class OnLatitudeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            _latitude = progress - 180;
            rewriteTvLatitude();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    /**
     * 経度選択SeekBarをスライドした時のリスナクラス。
     */
    private class OnLongitudeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            _longitude = progress - 180;
            rewriteTvLongitude();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
