package local.hal.st31.android.prefmemo;

/**
 * ST31 Androidサンプル09 都道府県メモアプリ
 *
 * 都道府県メモ情報を格納するエンティティクラス。
 *
 * @author Shinzo SAITO
 */
public class Memo {
    /**
     * 主キーのID値。
     */
    private int _id;
    /**
     * 都道府県名。
     */
    private String _name;
    /**
     * メモ内容。
     */
    private String _content;

    //以下アクセサメソッド。

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }
    public String getContent() {
        return _content;
    }
    public void setContent(String content) {
        _content = content;
    }
}
