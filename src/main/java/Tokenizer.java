import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer<T> {
    private List<TokenInfo<T>> tokenInfos;
    private List<Token<T>> tokens;

    public Tokenizer() {
        tokenInfos = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    public void add(String regex, T token) {
        tokenInfos.add(new TokenInfo<>(Pattern.compile("^(" + regex + ")"), token));
    }

    public void tokenize(String str) {
        String s = str;
        tokens.clear();

        while (!s.equals("")) {
            boolean match = false;

            for (TokenInfo<T> info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;

                    String tok = m.group().trim();
                    tokens.add(new Token<>(info.token, tok));

                    s = m.replaceFirst("");
                    break;
                }
            }
            if (!match) {
                throw new ParserException("Unexpected character in input: "+s);
            }
        }
    }

    public List<Token<T>> getTokens() {
        return tokens;
    }

    private class TokenInfo<T> {
        public final Pattern regex;
        public final T token;

        public TokenInfo(Pattern regex, T token) {
            super();
            this.regex = regex;
            this.token = token;
        }
    }
}
