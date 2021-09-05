package local.hal.st31.android.prefmemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * ST31 Androidサンプル09 都道府県メモアプリ
 *
 * データベース上のデータを処理するクラス。
 *
 * @author Shinzo SAITO
 */
public class DataAccess {
    /**
     * 主キーによるメモ内容検索メソッド。
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 主キーに対応するMemoオブジェクト。主キーに該当するデータがない場合は、null。
     */
    public static Memo findByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT * FROM memos WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        Memo memo = null;
        if(cursor.moveToFirst()) {
            memo = new Memo();
            int idxName = cursor.getColumnIndex("name");
            String name = cursor.getString(idxName);
            int idxContent = cursor.getColumnIndex("content");
            String content = cursor.getString(idxContent);
            memo.setId(id);
            memo.setName(name);
            memo.setContent(content);
        }
        return memo;
    }
    
    /**
     * 主キーによるレコード存在チェックメソッド。
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 主キーに対応するレコードが存在するかどうかを表すbool値。存在する場合はtrue、存在しない場合はfalse。
     */
    public static boolean findRowByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT COUNT(*) AS count FROM memos WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = false;
        if(cursor.moveToFirst()) {
            int idxCount = cursor.getColumnIndex("count");
            int count = cursor.getInt(idxCount);
            if(count >= 1) {
                result = true;
            }
        }
        return result;
    }
    
    /**
     * メモ情報を更新するメソッド。
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @param name 都道府県名。
     * @param content メモ内容。
     * @return 更新件数。
     */
    public static int update(SQLiteDatabase db, long id, String name, String content) {
        String sql = "UPDATE memos SET name = ?, content = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, content);
        stmt.bindLong(3, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
    
    /**
     * メモ情報を新規登録するメソッド。
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @param name 都道府県名。
     * @param content メモ内容。
     * @return 登録したレコードの主キー値。
     */
    public static long insert(SQLiteDatabase db, long id, String name, String content) {
        String sql = "INSERT INTO memos (_id, name, content) VALUES (?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        stmt.bindString(2, name);
        stmt.bindString(3, content);
        long insertedId = stmt.executeInsert();
        return insertedId;
    }
}
