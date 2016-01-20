import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonNodeParser {
    NotationTokenizer tokenizer;
    private final String DOT = ".";
    private final String ALL = "*";
    private final int PATH = 1;
    private final int INDEX = 2;

    private JsonNode root;

    public JsonNodeParser(JsonNode root) {
        this.root = root;
        tokenizer = new NotationTokenizer();
    }

    public List<String> get(String... paths) {
        List<String> results = new ArrayList<>();

        for (String path : paths) {
            int i = StringUtils.indexOf(path, DOT);

            if (i > 0) {
                String left = this.left(path, i);
                String right = this.right(path, i);

                if (isArray(left)) {
                    String pure = extract(left, PATH);
                    String index = extract(left, INDEX);
                    JsonNode arrNode = root.path(pure);
                    if (StringUtils.equals(index, ALL)) {
                        for (JsonNode element : arrNode) {
                            parse(element, right).stream().forEach(results::add);
                        }
                    } else {
                        assert index != null;
                        JsonNode child = arrNode.get(Integer.parseInt(index));
                        parse(child, right).forEach(results::add);
                    }
                } else {
                    JsonNode child = root.path(left);
                    parse(child, right).forEach(results::add);
                }
            }
        }

        return results;
    }

    public boolean validation(String notation) {
        return NotationValidator.parse(tokenizer.getTokens(notation));
    }

    private List<String> parse(JsonNode node, String subPath) {
        List<String> results = new ArrayList<>();
        int i = StringUtils.indexOf(subPath, DOT);

        if (i > 0) {
            String left = this.left(subPath, i);
            String right = this.right(subPath, i);

            if (isArray(left)) {
                String pure = extract(left, PATH);
                String index = extract(left, INDEX);
                JsonNode arrNode = node.path(pure);
                if (StringUtils.equals(index, "*")) {
                    for (JsonNode element : arrNode) {
                        parse(element, right).stream().forEach(results::add);
                    }
                } else {
                    assert index != null;
                    JsonNode child = arrNode.get(Integer.parseInt(index));
                    if (child != null) {
                        parse(child, right).forEach(results::add);
                    }
                }
            } else {
                JsonNode child = node.path(left);
                if (!(child instanceof MissingNode)) {
                    parse(child, right).forEach(results::add);
                }
            }
        } else {
            results.add(node.path(subPath).asText());
        }
        return results;
    }

    private String left(String str, int pos) {
        return StringUtils.substring(str, 0, pos);
    }

    private String right(String str, int pos) {
        return StringUtils.substring(str, pos + 1, Optional.ofNullable(str).map(String::length).orElse(0));
    }

    private boolean isArray(String path) {
        Pattern p = Pattern.compile("^\\w+(?:\\[(?:\\d+|\\*)\\])+$");
        Matcher m = p.matcher(path);
        return m.find();
    }

    private String extract(String path, int group) {
        Pattern p = Pattern.compile("^(\\w+)(?:\\[(\\d+|\\*)\\])+$");
        Matcher m = p.matcher(path);
        if (m.find()) {
            return m.group(group);
        }
        return null;
    }
}
