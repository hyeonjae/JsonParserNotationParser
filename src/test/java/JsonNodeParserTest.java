import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonNodeParserTest {
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testName() throws Exception {
        String payload = "{\"aaa\":{\"bbb\":{\"ccc\":\"hello\",\"ddd\":\"world\",\"eee\":[{\"fff\":\"fizz\"},{\"ggg\":\"buzz\"}]},\"hhh\":[[{\"iii\":\"apple\"}],[{\"jjj\":\"orange\"}],[{\"kkk\":\"banana\"}]],\"lll\":[[{\"color\":\"red\"}],[{\"color\":\"blue\"}],[{\"color\":\"green\"}]]}}";
        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));

        Assert.assertEquals(parser.get("aaa.bbb.ccc").get(0), "hello");
        Assert.assertEquals(parser.get("aaa.bbb.ddd").get(0), "world");
        Assert.assertEquals(parser.get("aaa.bbb.eee[0].fff").get(0), "fizz");
        Assert.assertEquals(parser.get("aaa.bbb.eee[1].ggg").get(0), "buzz");
        Assert.assertEquals(parser.get("aaa.bbb.eee[*].fff").get(0), "fizz");
        Assert.assertEquals(parser.get("aaa.bbb.eee[*].ggg").get(1), "buzz");
        Assert.assertEquals(parser.get("aaa.hhh[0][0].iii").get(0), "apple");
        Assert.assertEquals(parser.get("aaa.hhh[1][0].jjj").get(0), "orange");
        Assert.assertEquals(parser.get("aaa.hhh[2][0].kkk").get(0), "banana");
        Assert.assertEquals(parser.get("aaa.hhh[0][*].iii").get(0), "apple");
        Assert.assertEquals(parser.get("aaa.hhh[1][*].jjj").get(0), "orange");
        Assert.assertEquals(parser.get("aaa.hhh[2][*].kkk").get(0), "banana");
        Assert.assertEquals(parser.get("aaa.lll[0][0].color").get(0), "red");
        Assert.assertEquals(parser.get("aaa.lll[1][0].color").get(0), "blue");
        Assert.assertEquals(parser.get("aaa.lll[2][0].color").get(0), "green");
        Assert.assertEquals(parser.get("aaa.lll[*][*].color").get(0), "red");
        Assert.assertEquals(parser.get("aaa.lll[*][*].color").get(0), "red");
        Assert.assertEquals(parser.get("aaa.lll[*][*].color").get(1), "blue");
        Assert.assertEquals(parser.get("aaa.lll[*][*].color").get(2), "green");
    }
}
