public class Token<T> {
    public final T type;
    public final String value;

    public Token(T type, String value) {
        this.type = type;
        this.value = value;
    }
}
