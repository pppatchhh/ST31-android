package local.hal.st31.android.itinfosite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /**
     * リストビューに表示させるリストデータ。
     */
    private List<Map<String, String>> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _list = createList();

        ListView lvSiteList = findViewById(R.id.lvSiteList);
        String[] from = {"name", "url"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _list, android.R.layout.simple_list_item_2, from, to);
        lvSiteList.setAdapter(adapter);
        lvSiteList.setOnItemClickListener(new ListItemClickListener());
    }

    /**
     * リストビューに表示させるIT情報サイトリストデータを生成するメソッド。
     *
     * @return 生成されたIT情報サイトリストデータ。
     */
    private List<Map<String, String>> createList() {
        List<Map<String, String>> list = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("name", "CodeZine");
        map.put("url", "http://codezine.jp/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "@IT");
        map.put("url", "http://www.atmarkit.co.jp/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "EnterpriseZine");
        map.put("url", "http://www.enterprisezine.jp/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "gihyo.jp");
        map.put("url", "http://www.gihyo.jp/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "ITmediaエンタープライズ");
        map.put("url", "http://www.itmedia.co.jp/enterprise/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "Publickey");
        map.put("url", "https://www.publickey1.jp/");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "TechCrunch");
        map.put("url", "https://jp.techcrunch.com/");
        list.add(map);

        return list;
    }

    /**
     * リストがタップされた時のリスナクラス。
     */
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = _list.get(position);
            String url = item.get("url");
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
