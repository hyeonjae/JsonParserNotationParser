import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonNodeParser {
    private static final String ALL = "*";
    private static final String DOT = ".";
    private NotationTokenizer tokenizer;
    private JsonNode root;

    public JsonNodeParser() {
        this.tokenizer = new NotationTokenizer();
    }

    public JsonNodeParser(JsonNode root) {
        this();
        this.root = root;
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

    public boolean has(String path) {
        int i = StringUtils.indexOf(path, DOT);

        if (i > 0) {
            String left = this.left(path, i);
            String right = this.right(path, i);

            if (isArray(left)) {
                String pure = extract(left, 1);
                String index = extract(left, 2);
                JsonNode arrNode = root.path(pure);
                assert index != null;
                JsonNode child = arrNode.get(Integer.parseInt(index));
                return has(child, right);
            } else {
                JsonNode child = root.path(left);
                return has(child, right);
            }
        } else {
            return root.has(path);
        }
    }

    private boolean has(JsonNode node, String subPath) {
        int i = StringUtils.indexOf(subPath, DOT);

        if (i > 0) {
            String left = this.left(subPath, i);
            String right = this.right(subPath, i);

            if (isArray(left)) {
                String pure = extract(left, 1);
                String index = extract(left, 2);
                JsonNode arrNode = node.path(pure);
                assert index != null;
                JsonNode child = arrNode.get(Integer.parseInt(index));
                return has(child, right);
            } else {
                JsonNode child = node.path(left);
                return has(child, right);
            }
        } else {
            return node.has(subPath);
        }
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
        if (sub == null || sub instanceof MissingNode) {
            return Collections.emptyList();
        }

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
            if (index.equals(ALL)) {
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
        if (sub == null || sub instanceof MissingNode) {
            return Collections.emptyList();
        }

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

    private String left(String str, int pos) {
        return StringUtils.substring(str, 0, pos);
    }

    private String right(String str, int pos) {
        return StringUtils.substring(str, pos + 1, Optional.ofNullable(str).map(String::length).orElse(0));
    }

    private boolean isArray(String path) {
        Pattern p = Pattern.compile("^\\w+\\[[0-9]+|\\*\\]$");
        Matcher m = p.matcher(path);
        return m.find();
    }

    private String extract(String path, int group) {
        Pattern p = Pattern.compile("^(\\w+)\\[([0-9]+|\\*)\\]$");
        Matcher m = p.matcher(path);
        if (m.find()) {
            return m.group(group);
        }
        return null;
    }
}
