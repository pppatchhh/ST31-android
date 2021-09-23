package local.hal.st31.android.saigoku33memo90032;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "saigoku33memo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE temples(");
        sb.append("_id INTEGER PRIMARY KEY, ");
        sb.append("name TEXT NOT NULL, ");
        sb.append("honzon TEXT, ");
        sb.append("shushi TEXT, ");
        sb.append("address TEXT, ");
        sb.append("url TEXT, ");
        sb.append("note TEXT");
        sb.append(");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
