public class Token<T> {
    public final T token;
    public final String sequence;

    public Token(T token, String sequence) {
        this.token = token;
        this.sequence = sequence;
    }
}
