package local.hal.st31.android.memopad;

import java.sql.Timestamp;

/**
 * ST31 Androidサンプル11 メモ帳アプリ
 *
 * メモ情報を格納するエンティティクラス。
 *
 * @author Shinzo SAITO
 */
public class Memo {
    /**
     * 主キーのID値。
     */
    private long _id;
    /**
     * タイトル。
     */
    private String _title;
    /**
     * メモ内容。
     */
    private String _content;
    /**
     * 更新日時。
     */
    private Timestamp _updatedAt;

    //以下アクセサメソッド。

    public long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }
    public String getTitle() {
        return _title;
    }
    public void setTitle(String title) {
        _title = title;
    }
    public String getContent() {
        return _content;
    }
    public void setContent(String content) {
        _content = content;
    }
    public Timestamp getUpdatedAt() {
        return _updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        _updatedAt = updatedAt;
    }
}
