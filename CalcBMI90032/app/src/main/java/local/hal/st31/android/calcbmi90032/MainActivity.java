package local.hal.st31.android.calcbmi90032;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btCalc = findViewById(R.id.btCalc);
        btCalc.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            EditText etHeight = findViewById(R.id.etHeight);
            EditText etWeight = findViewById(R.id.etWeight);
            TextView tvAnswer = findViewById(R.id.tvAnswer);

            String strHeight = etHeight.getText().toString();
            String strWeight = etWeight.getText().toString();
            if(strHeight.equals("") || strWeight.equals("")){
                String message = "数字を入力してください。";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }else{
                double height = Double.valueOf(strHeight) / Double.valueOf(100);
                double weight = Double.valueOf(strWeight);
                if(height == 0) {
                    String message = "身長が0cmです。";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }else if(weight == 0){
                    String message = "体重が0kgです。";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }else{
                    double denomi = height * height;
                    BigDecimal bigAns = new BigDecimal(weight / denomi);
                    bigAns = bigAns.setScale(3, RoundingMode.HALF_UP);
                    String strAns = String.format("%.1f", bigAns);
                    if(bigAns.intValue() < 18.5) {
                        tvAnswer.setText("BMI: " + strAns + "やせています。体重" + String.valueOf((int)(height * height * 22)) + "kgを目指しましょう。");
                    }else if(bigAns.intValue() > 25){
                        tvAnswer.setText("BMI: " + strAns + "肥満です。体重" + String.valueOf((int)(height * height * 22)) + "kgを目指しましょう。");
                    }else{
                        tvAnswer.setText("BMI: " + strAns + " ちょうどいいです。現状を維持しましょう。");
                    }
                }
            }


        }
    }
}