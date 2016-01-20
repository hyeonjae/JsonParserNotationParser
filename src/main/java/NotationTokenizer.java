import java.util.List;

public class NotationTokenizer {
    Tokenizer<NotationTokenType> tokenizer;

    public NotationTokenizer() {
        tokenizer = new Tokenizer<>();
        tokenizer.add("[a-zA-Z]+", NotationTokenType.IDENT);
        tokenizer.add("\\.", NotationTokenType.DOT);
        tokenizer.add("\\[", NotationTokenType.LP);
        tokenizer.add("[0-9]+|\\*", NotationTokenType.INDEX);
        tokenizer.add("\\]", NotationTokenType.RP);
    }

    private void tokenize(String notation) {
        tokenizer.tokenize(notation);
    }

    private List<Token<NotationTokenType>> getTokens() {
        return tokenizer.getTokens();
    }

    public List<Token<NotationTokenType>> getTokens(String notation) {
        tokenize(notation);
        return getTokens();
    }
}
