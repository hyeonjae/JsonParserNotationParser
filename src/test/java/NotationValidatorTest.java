import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotationValidatorTest {
    NotationTokenizer notationTokenizer;

    @Before
    public void setUp() {
        notationTokenizer = new NotationTokenizer();
    }

    @Test
    public void test_tokenizer_validator_true() {
        String nodation1 = "aaa.bbb.abc[1].abc[1][12].abc[1].ccc[*].ddd";
        boolean v1 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation1));
        Assert.assertTrue(v1);

        String nodation2 = "aaa.bbb.ccc[1]";
        boolean v2 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation1));
        Assert.assertTrue(v2);

        String nodation3 = "aaa[1][2].bbb";
        boolean v3 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation3));
        Assert.assertTrue(v3);

        String nodation4 = "aaa[1].bbb.ccc[2]";
        boolean v4 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation4));
        Assert.assertTrue(v4);

        String nodation5 = "aaa[*][2]";
        boolean v5 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation5));
        Assert.assertTrue(v5);

        String nodation6 = "aaa";
        boolean v6 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation6));
        Assert.assertTrue(v6);
    }

    @Test
    public void test_tokenizer_validator_false() {
        String nodation1 = "aaa.";
        boolean v1 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation1));
        Assert.assertFalse(v1);

        String nodation2 = "aaa..bbb";
        boolean v2 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation1));
        Assert.assertFalse(v2);

        String nodation3 = "aaa[1].[2]";
        boolean v3 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation3));
        Assert.assertFalse(v3);

        String nodation4 = "aaa[]";
        boolean v4 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation4));
        Assert.assertFalse(v4);

        String nodation5 = "aaa[**]";
        boolean v5 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation5));
        Assert.assertFalse(v5);

        String nodation6 = "aaa[";
        boolean v6 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation6));
        Assert.assertFalse(v6);

        String nodation7 = "aaa[b]";
        boolean v7 = NotationValidator.parse(this.notationTokenizer.getTokens(nodation7));
        Assert.assertFalse(v7);
    }
}
