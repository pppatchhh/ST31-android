package local.hal.st31.android.saigoku33memo90032;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DataAccess {
    public static Memo findByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT * FROM temples WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        Memo memo = null;
        if(cursor.moveToFirst()) {
            memo = new Memo();
            int idxName = cursor.getColumnIndex("name");
            String name = cursor.getString(idxName);
            int idxHonzon = cursor.getColumnIndex("honzon");
            String honzon = cursor.getString(idxHonzon);
            int idxShushi = cursor.getColumnIndex("shushi");
            String shushi = cursor.getString(idxShushi);
            int idxAddress = cursor.getColumnIndex("address");
            String address = cursor.getString(idxAddress);
            int idxUrl = cursor.getColumnIndex("url");
            String url = cursor.getString(idxUrl);
            int idxNote = cursor.getColumnIndex("note");
            String note = cursor.getString(idxNote);

            memo.setId(id);
            memo.setName(name);
            memo.setHonzon(honzon);
            memo.setShushi(shushi);
            memo.setAddress(address);
            memo.setUrl(url);
            memo.setNote(note);
        }
        return memo;
    }

    public static boolean findRowByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT COUNT(*) as count FROM temples WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = false;
        if(cursor.moveToFirst()){
            int idxCount = cursor.getColumnIndex("count");
            int count = cursor.getInt(idxCount);
            if(count >= 1){
                result = true;
            }
        }
        return result;
    }

    public static int update(SQLiteDatabase db, long id, String name, String honzon, String shushi, String address, String url, String note){
        String sql = "UPDATE temples SET name = ?, honzon = ?, shushi = ?, address = ?, url = ?, note = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, honzon);
        stmt.bindString(3, shushi);
        stmt.bindString(4, address);
        stmt.bindString(5, url);
        stmt.bindString(6, note);
        stmt.bindLong(7, id);

        int result = stmt.executeUpdateDelete();
        return result;
    }

    public static long insert(SQLiteDatabase db, long id, String name, String honzon, String shushi, String address, String url, String note) {
        String sql = "INSERT INTO temples (_id, name, honzon, shushi, address, url, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        stmt.bindString(2, name);
        stmt.bindString(3, honzon);
        stmt.bindString(4, shushi);
        stmt.bindString(5, address);
        stmt.bindString(6, url);
        stmt.bindString(7, note);
        long insertedId = stmt.executeInsert();
        return insertedId;
    }
}
