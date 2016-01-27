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
        String notation1 = "aaa";
        boolean v1 = NotationValidator.parse(this.notationTokenizer.getTokens(notation1));
        Assert.assertTrue(v1);

        String notation2 = "aaa.bbb";
        boolean v2 = NotationValidator.parse(this.notationTokenizer.getTokens(notation2));
        Assert.assertTrue(v2);

        String notation3 = "aaa[1]";
        boolean v3 = NotationValidator.parse(this.notationTokenizer.getTokens(notation3));
        Assert.assertTrue(v3);

        String notation4 = "aaa[*]";
        boolean v4 = NotationValidator.parse(this.notationTokenizer.getTokens(notation4));
        Assert.assertTrue(v4);

        String notation5 = "aaa[*].bbb";
        boolean v5 = NotationValidator.parse(this.notationTokenizer.getTokens(notation5));
        Assert.assertTrue(v5);

        String notation6 = "aaa[*][2]";
        boolean v6 = NotationValidator.parse(this.notationTokenizer.getTokens(notation6));
        Assert.assertTrue(v6);

        String notation7 = "[2]";
        boolean v7 = NotationValidator.parse(this.notationTokenizer.getTokens(notation7));
        Assert.assertTrue(v7);

        String notation8 = "[2].aaa";
        boolean v8 = NotationValidator.parse(this.notationTokenizer.getTokens(notation8));
        Assert.assertTrue(v8);

        String notation9 = "aaa.bbb.ccc[1]";
        boolean v9 = NotationValidator.parse(this.notationTokenizer.getTokens(notation9));
        Assert.assertTrue(v9);

        String notation10 = "aaa[1][2].bbb";
        boolean v10 = NotationValidator.parse(this.notationTokenizer.getTokens(notation10));
        Assert.assertTrue(v10);

        String notation11 = "aaa[1].bbb.ccc[2]";
        boolean v11 = NotationValidator.parse(this.notationTokenizer.getTokens(notation11));
        Assert.assertTrue(v11);

        String notation12 = "aaa.bbb.abc[1].abc[1][12].abc[1].ccc[*].ddd";
        boolean v12 = NotationValidator.parse(this.notationTokenizer.getTokens(notation12));
        Assert.assertTrue(v12);
    }

    @Test
    public void test_tokenizer_validator_false() {
        String notation1 = "aaa.";
        boolean v1 = NotationValidator.parse(this.notationTokenizer.getTokens(notation1));
        Assert.assertFalse(v1);

        String notation2 = "aaa..bbb";
        boolean v2 = NotationValidator.parse(this.notationTokenizer.getTokens(notation2));
        Assert.assertFalse(v2);

        String notation3 = "aaa[1].[2]";
        boolean v3 = NotationValidator.parse(this.notationTokenizer.getTokens(notation3));
        Assert.assertFalse(v3);

        String notation4 = "aaa[]";
        boolean v4 = NotationValidator.parse(this.notationTokenizer.getTokens(notation4));
        Assert.assertFalse(v4);

        String notation5 = "aaa[**]";
        boolean v5 = NotationValidator.parse(this.notationTokenizer.getTokens(notation5));
        Assert.assertFalse(v5);

        String notation6 = "aaa[";
        boolean v6 = NotationValidator.parse(this.notationTokenizer.getTokens(notation6));
        Assert.assertFalse(v6);

        String notation7 = "aaa[b]";
        boolean v7 = NotationValidator.parse(this.notationTokenizer.getTokens(notation7));
        Assert.assertFalse(v7);

        String notation8 = "aaa[1]a";
        boolean v8 = NotationValidator.parse(this.notationTokenizer.getTokens(notation8));
        Assert.assertFalse(v8);
    }
}
