package local.hal.st31.android.saigoku33memo90032;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private class ListItemClickListender implements AdapterView.onItemClickListener {
        @override
        public void onItemClick(AdapterView<?> parent)
    }
}