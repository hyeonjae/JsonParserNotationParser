import java.util.List;

public class NotationValidator {
    public static boolean parse(List<Token<NotationTokenType>> tokens) {
        try {
            Token<NotationTokenType> word = tokens.remove(0);
            if (word.token.equals(NotationTokenType.IDENT)) {
                return ident(tokens);
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private static boolean ident(List<Token<NotationTokenType>> tokens) {
        if (tokens.size() < 1) {
            return true;
        }
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.DOT)) {
            return dot(tokens);
        } else if (word.token.equals(NotationTokenType.LP)) {
            return LP(tokens);
        }
        return false;
    }
    private static boolean dot(List<Token<NotationTokenType>> tokens) {
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.IDENT)) {
            return ident(tokens);
        }
        return false;
    }
    private static boolean LP(List<Token<NotationTokenType>> tokens) {
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.INDEX)) {
            return index(tokens);
        }
        return false;
    }
    private static boolean index(List<Token<NotationTokenType>> tokens) {
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.RP)) {
            return RP(tokens);
        }
        return false;
    }
    private static boolean RP(List<Token<NotationTokenType>> tokens) {
        if (tokens.size() < 1) {
            return true;
        }
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.LP)) {
            return LP(tokens);
        } else if (word.token.equals(NotationTokenType.DOT)) {
            return dot(tokens);
        }
        return false;
    }
}
