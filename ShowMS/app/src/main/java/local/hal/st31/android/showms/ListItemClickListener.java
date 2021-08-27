package local.hal.st31.android.showms;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * リストが選択されたときの処理が記述されたメンバクラス。
 */
class ListItemClickListener implements AdapterView.OnItemClickListener {
    private final MainActivity mainActivity;

    public ListItemClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = (String) parent.getItemAtPosition(position);

        Intent intent = new Intent(mainActivity, ShowMsActivity.class);
        intent.putExtra("selectedPictNo", position);
        intent.putExtra("selectedPictName", name);
        mainActivity.startActivity(intent);
    }
}
