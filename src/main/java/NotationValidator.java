import java.util.List;
import java.util.ArrayList;

public class NotationValidator {
    /*
        Notation    -> Slices
                     | Slices DOT SubNotation
                     | SubNotation
        Slices      -> Slice
                     | Slice Slices
        SubNotation -> Key
                     | Key DOT SubNotation
        Key         -> IDENT Key`
        Key`        -> Slice Key`
                     | e
        Slice       -> LP INDEX RP
        IDENT        -> [a-zA-Z]+
        INDEX       -> [0-9]+
                     | *
        DOT         -> .
        LP          -> [
        RP          -> ]
     */
    public static boolean parse(List<Token<NotationTokenType>> tokens) {
        try {
            return Notation(tokens);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private static boolean Notation(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        tokens.clear();
        tokens.addAll(backup);
        if (Slices(tokens) && tokens.size() == 0) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        if (Slices(tokens) && Dot(tokens) && SubNotation(tokens) && tokens.size() == 0) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        if (SubNotation(tokens) && tokens.size() == 0) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }

    private static boolean Slices(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        tokens.clear();
        tokens.addAll(backup);
        if (Slice(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        if (Slice(tokens) && Slices(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        return true;
    }

    private static boolean Key(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        if (Pure(tokens) && KeyPrime(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }

    private static boolean KeyPrime(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        if (Slice(tokens) && KeyPrime(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        return true;
    }

    private static boolean SubNotation(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        if (Key(tokens) && Dot(tokens) && SubNotation(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        if (Key(tokens)) {
            return true;
        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }

    private static boolean Dot(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        try {
            Token<NotationTokenType> token = tokens.remove(0);
            if (token.type.equals(NotationTokenType.DOT)) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }

    private static boolean Pure(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        try {
            Token<NotationTokenType> token = tokens.remove(0);
            if (token.type.equals(NotationTokenType.IDENT)) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }

    private static boolean Slice(List<Token<NotationTokenType>> tokens) {
        List<Token<NotationTokenType>> backup = new ArrayList<>();
        backup.addAll(tokens);
        try {
            Token<NotationTokenType> token = tokens.remove(0);
            if (token.type.equals(NotationTokenType.LP)) {
                token = tokens.remove(0);
                if (token.type.equals(NotationTokenType.INDEX)) {
                    token = tokens.remove(0);
                    if (token.type.equals(NotationTokenType.RP)) {
                        return true;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        tokens.clear();
        tokens.addAll(backup);
        return false;
    }
}
