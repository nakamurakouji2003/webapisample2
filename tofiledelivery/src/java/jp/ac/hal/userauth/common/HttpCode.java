package jp.ac.hal.userauth.common;

public enum HttpCode {
    INPUT_MISTAKE("ユーザー名またはパスワードが間違っています", 450), //入力間違い
    ;

    private final String name;
    private final int code;

    private HttpCode(final String name, final int code) {
       this.name = name;
       this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }
}
