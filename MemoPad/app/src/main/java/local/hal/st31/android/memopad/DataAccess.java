package local.hal.st31.android.memopad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.Timestamp;

/**
 * ST31 Androidサンプル11 メモ帳アプリ
 *
 * データベース上のデータを処理するクラス。
 *
 * @author Shinzo SAITO
 */
public class DataAccess {
    /**
     * 全データ検索メソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @return 検索結果のCursorオブジェクト。
     */
    public static Cursor findAll(SQLiteDatabase db) {
        String sql = "SELECT * FROM memos ORDER BY updated_at DESC";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 主キーによる検索。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 主キーに対応するデータを格納したMemoオブジェクト。対応するデータが存在しない場合はnull。
     */
    public static Memo findByPK(SQLiteDatabase db, long id) {
        String sql = "SELECT * FROM memos WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        Memo result = null;
        if(cursor.moveToFirst()) {
            int idxTitle = cursor.getColumnIndex("title");
            int idxContent = cursor.getColumnIndex("content");
            int idxUpdateAt = cursor.getColumnIndex("updated_at");
            String title = cursor.getString(idxTitle);
            String content = cursor.getString(idxContent);
            String updateAtStr = cursor.getString(idxUpdateAt);

            Timestamp updateAt = Timestamp.valueOf(updateAtStr);

            result = new Memo();
            result.setId(id);
            result.setTitle(title);
            result.setContent(content);
            result.setUpdatedAt(updateAt);
        }
        return result;
    }

    /**
     * メモ情報を更新するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @param title タイトル。
     * @param content メモ内容。
     * @return 更新件数。
     */
    public static int update(SQLiteDatabase db, long id, String title, String content) {
        String sql = "UPDATE memos SET title = ?, content = ?, updated_at = datetime('now') WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, title);
        stmt.bindString(2, content);
        stmt.bindLong(3, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }

    /**
     * メモ情報を新規登録するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param title メモタイトル。
     * @param content メモ内容。
     * @return 登録されたレコードの主キー値。
     */
    public static long insert(SQLiteDatabase db, String title, String content) {
        String sql = "INSERT INTO memos (title, content, updated_at) VALUES (?, ?, datetime('now'))";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, title);
        stmt.bindString(2, content);
        long id = stmt.executeInsert();
        return id;
    }

    /**
     * メモ情報を削除するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 削除件数。
     */
    public static int delete(SQLiteDatabase db, long id) {
        String sql = "DELETE FROM memos WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}
