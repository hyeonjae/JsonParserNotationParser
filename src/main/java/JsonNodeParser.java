import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonNodeParser {
    NotationTokenizer tokenizer;
    private final String DOT = ".";
    private final String ALL = "*";
    private final int PATH = 1;
    private final int INDEX = 2;

    private JsonNode root;

    private String left;
    private JsonNode sub;

    public JsonNodeParser(JsonNode root) {
        this.root = root;
        tokenizer = new NotationTokenizer();

    }

    public boolean validation(String notation) {
        return NotationValidator.parse(tokenizer.getTokens(notation));
    }


    public List<String> get(String... paths) {
        List<String> results = new ArrayList<>();

        for (String path : paths) {
            results.addAll(parse(tokenizer.getTokens(path)));
        }

        return results;
    }

    public List<String> parse(List<Token<NotationTokenType>> tokens) {
        List<String> results = new ArrayList<>();
        try {
            Token<NotationTokenType> word = tokens.remove(0);
            if (word.token.equals(NotationTokenType.IDENT)) {
                results.addAll(ident(root.path(word.sequence), tokens));
                return results;
            } else {
                return Collections.emptyList();
            }
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }
    }
    private List<String> ident(JsonNode sub, List<Token<NotationTokenType>> tokens) {

        if (tokens.size() < 1) {
            List<String> results = new ArrayList<>();
            results.add(sub.asText());
            return results;
        }
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.DOT)) {
            return dot(sub, tokens);
        } else if (word.token.equals(NotationTokenType.LP)) {
            return LP(sub, tokens);
        }
        return Collections.emptyList();
    }
    private List<String> dot(JsonNode sub, List<Token<NotationTokenType>> tokens) {

        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.IDENT)) {
            return ident(sub.path(word.sequence), tokens);
        }
        return Collections.emptyList();
    }
    private List<String> LP(JsonNode sub, List<Token<NotationTokenType>> tokens) {
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.INDEX)) {
            return index(sub, tokens, word.sequence);
        }
        return Collections.emptyList();
    }
    private List<String> index(JsonNode sub, List<Token<NotationTokenType>> tokens, String index) {
        List<String> results = new ArrayList<>();
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.RP)) {
            if (index.equals("*")) {
                int size = sub.size();

                for (int i=0; i<size; i++) {
                    List<Token<NotationTokenType>> tokensCopy = new ArrayList<>();
                    tokensCopy.addAll(tokens);
                    results.addAll(RP(sub.get(i), tokensCopy));
                }
                return results;
            } else {
                return RP(sub.get(Integer.parseInt(index)), tokens);
            }
        }
        return Collections.emptyList();
    }
    private List<String> RP(JsonNode sub, List<Token<NotationTokenType>> tokens) {
        List<String> results = new ArrayList<>();
        if (tokens.size() < 1) {
            results.add(sub.asText());
            return results;
        }
        Token<NotationTokenType> word = tokens.remove(0);
        if (word.token.equals(NotationTokenType.LP)) {
            return LP(sub, tokens);
        } else if (word.token.equals(NotationTokenType.DOT)) {
            return dot(sub, tokens);
        }
        return Collections.emptyList();
    }
}
