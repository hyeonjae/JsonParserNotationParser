import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class JsonNodeParserTest {
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void test기본동작() throws Exception {
        String payload = "{\"aaa\":{\"bbb\":{\"ccc\":\"hello\",\"ddd\":\"world\",\"eee\":[{\"fff\":\"fizz\"},{\"ggg\":\"buzz\"}]},\"hhh\":[[{\"iii\":\"apple\"}],[{\"jjj\":\"orange\"}],[{\"kkk\":\"banana\"}]],\"lll\":[[{\"color\":\"red\"}],[{\"color\":\"blue\"}],[{\"color\":\"green\"}]]}}";
        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));

        Assert.assertEquals("hello", parser.get("aaa.bbb.ccc").get(0));
        Assert.assertEquals("world", parser.get("aaa.bbb.ddd").get(0));
        Assert.assertEquals("fizz", parser.get("aaa.bbb.eee[0].fff").get(0));
        Assert.assertEquals("buzz", parser.get("aaa.bbb.eee[1].ggg").get(0));
        Assert.assertEquals("fizz", parser.get("aaa.bbb.eee[*].fff").get(0));
        Assert.assertEquals("buzz", parser.get("aaa.bbb.eee[*].ggg").get(0));
        Assert.assertEquals("apple", parser.get("aaa.hhh[0][0].iii").get(0));
        Assert.assertEquals("orange", parser.get("aaa.hhh[1][0].jjj").get(0));
        Assert.assertEquals("banana", parser.get("aaa.hhh[2][0].kkk").get(0));
        Assert.assertEquals("apple", parser.get("aaa.hhh[0][*].iii").get(0));
        Assert.assertEquals("orange", parser.get("aaa.hhh[1][*].jjj").get(0));
        Assert.assertEquals("banana", parser.get("aaa.hhh[2][*].kkk").get(0));
        Assert.assertEquals("red", parser.get("aaa.lll[0][0].color").get(0));
        Assert.assertEquals("blue", parser.get("aaa.lll[1][0].color").get(0));
        Assert.assertEquals("green", parser.get("aaa.lll[2][0].color").get(0));
        Assert.assertEquals("red", parser.get("aaa.lll[*][*].color").get(0));
        Assert.assertEquals("red", parser.get("aaa.lll[*][*].color").get(0));
        Assert.assertEquals("blue", parser.get("aaa.lll[*][*].color").get(1));
        Assert.assertEquals("green", parser.get("aaa.lll[*][*].color").get(2));
    }

    @Test
    public void testBasic() throws Exception {
        String payload = "{\"friends\":[{\"id\":0,\"name\":\"Santiago Mcmillan\"},{\"id\":1,\"name\":\"Graham Zimmerman\"},{\"id\":2,\"name\":\"Tabatha Garrison\"}]}";
        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));

        Assert.assertEquals(1, parser.get("friends[0].id").size());
        Assert.assertEquals(1, parser.get("friends[1].id").size());
        Assert.assertEquals(1, parser.get("friends[2].id").size());

        Assert.assertEquals("0", parser.get("friends[0].id").get(0));
        Assert.assertEquals("1", parser.get("friends[1].id").get(0));
        Assert.assertEquals("2", parser.get("friends[2].id").get(0));

        Assert.assertEquals(1, parser.get("friends[0].name").size());
        Assert.assertEquals(1, parser.get("friends[1].name").size());
        Assert.assertEquals(1, parser.get("friends[2].name").size());

        Assert.assertEquals("Santiago Mcmillan", parser.get("friends[0].name").get(0));
        Assert.assertEquals("Graham Zimmerman", parser.get("friends[1].name").get(0));
        Assert.assertEquals("Tabatha Garrison", parser.get("friends[2].name").get(0));

        Assert.assertEquals(3, parser.get("friends[*].id").size());
        Assert.assertEquals(3, parser.get("friends[*].name").size());

        Assert.assertEquals("Santiago Mcmillan", parser.get("friends[*].name").get(0));
        Assert.assertEquals("Graham Zimmerman", parser.get("friends[*].name").get(1));
        Assert.assertEquals("Tabatha Garrison", parser.get("friends[*].name").get(2));


        Assert.assertEquals(0, parser.get("friends[0].xxx").size());
        Assert.assertEquals(0, parser.get("friends[3].name").size());
        Assert.assertEquals(0, parser.get("xxx").size());
    }

    // TODO
    @Test
    @Ignore
    public void test배열() throws Exception {
        String payload = "[{\"aaa\":\"hello\"}, {\"aaa\":\"world\"}]";
        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));

        Assert.assertEquals("hello", parser.get("[0].aaa").get(0));
        Assert.assertEquals("world", parser.get("[1].aaa").get(0));
        Assert.assertEquals("world", parser.get("[*].aaa").get(0));
        Assert.assertEquals("world", parser.get("[*].aaa").get(1));
    }

    @Test
    public void test비트버킷_데이터() throws IOException {
        String payload = "{\"push\":{\"changes\":[{\"forced\":false,\"old\":null,\"links\":{\"commits\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commits?include=e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/branch/feature-three\"}},\"created\":true,\"commits\":[{\"hash\":\"e6c96415d27049e20795905debb137a3926796ec\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/e6c96415d27049e20795905debb137a3926796ec\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}}}],\"date\":\"2016-01-18T07:37:03+00:00\",\"message\":\"This is the second title\\n\\n - fix #2-1\\n - fix #2-2\\n\",\"type\":\"commit\"},{\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"29af8d486527459f64e9c00bb4c6155878018367\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/29af8d486527459f64e9c00bb4c6155878018367\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/29af8d486527459f64e9c00bb4c6155878018367\"}}}],\"date\":\"2016-01-18T07:36:33+00:00\",\"message\":\"This is the title\\n\\n - fix #1\\n - fix #2\\n - fix #3\\n\",\"type\":\"commit\"},{\"hash\":\"29af8d486527459f64e9c00bb4c6155878018367\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/29af8d486527459f64e9c00bb4c6155878018367\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/29af8d486527459f64e9c00bb4c6155878018367\"}},\"author\":{\"raw\":\"박현재 <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"5c390fb33770b83d76f63f14055f833e23c4a1f0\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/5c390fb33770b83d76f63f14055f833e23c4a1f0\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/5c390fb33770b83d76f63f14055f833e23c4a1f0\"}}}],\"date\":\"2015-12-02T13:18:16+00:00\",\"message\":\"second commit\\n\",\"type\":\"commit\"},{\"hash\":\"5c390fb33770b83d76f63f14055f833e23c4a1f0\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/5c390fb33770b83d76f63f14055f833e23c4a1f0\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/5c390fb33770b83d76f63f14055f833e23c4a1f0\"}},\"author\":{\"raw\":\"박현재 <hyeonjae.park@nhnent.com>\"},\"parents\":[],\"date\":\"2015-12-02T13:15:07+00:00\",\"message\":\"first commit\\n\",\"type\":\"commit\"}],\"truncated\":false,\"closed\":false,\"new\":{\"links\":{\"commits\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commits/feature-three\"},\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/refs/branches/feature-three\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/branch/feature-three\"}},\"type\":\"branch\",\"name\":\"feature-three\",\"repository\":{\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo\"},\"avatar\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/avatar/32/\"}},\"type\":\"repository\",\"uuid\":\"{e07de9e6-f6c1-4b8a-9921-c1d5047cc4b6}\",\"full_name\":\"hyeonjae_park/bitbucket_test_repo\",\"name\":\"bitbucket_test_repo\"},\"target\":{\"hash\":\"e6c96415d27049e20795905debb137a3926796ec\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/e6c96415d27049e20795905debb137a3926796ec\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}}}],\"date\":\"2016-01-18T07:37:03+00:00\",\"message\":\"This is the second title\\n\\n - fix #2-1\\n - fix #2-2\\n\",\"type\":\"commit\"}}}]},\"actor\":{\"username\":\"hyeonjae_park\",\"type\":\"user\",\"display_name\":\"hyeonjae park\",\"uuid\":\"{118501b7-ebc3-495e-bc3a-59e28e158cc0}\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/users/hyeonjae_park\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/\"},\"avatar\":{\"href\":\"https://bitbucket.org/account/hyeonjae_park/avatar/32/\"}}},\"repository\":{\"website\":\"\",\"scm\":\"git\",\"name\":\"bitbucket_test_repo\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo\"},\"avatar\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/avatar/32/\"}},\"full_name\":\"hyeonjae_park/bitbucket_test_repo\",\"owner\":{\"username\":\"hyeonjae_park\",\"type\":\"user\",\"display_name\":\"hyeonjae park\",\"uuid\":\"{118501b7-ebc3-495e-bc3a-59e28e158cc0}\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/users/hyeonjae_park\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/\"},\"avatar\":{\"href\":\"https://bitbucket.org/account/hyeonjae_park/avatar/32/\"}}},\"type\":\"repository\",\"is_private\":true,\"uuid\":\"{e07de9e6-f6c1-4b8a-9921-c1d5047cc4b6}\"}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("push.changes[0].links.html.href");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/branch/feature-three", result.get(0));
    }

    @Test
    public void test잘못된_경로_파싱() throws IOException {
        String payload = "{\"bbb\":{\"ccc\":\"hello\",\"ddd\":\"world\",\"eee\":[{\"fff\":\"fizz\"},{\"ggg\":\"buzz\"}]},\"hhh\":[[{\"iii\":\"apple\"}],[{\"jjj\":\"orange\"}],[{\"kkk\":\"banana\"}]],\"lll\":[[{\"color\":\"red\"}],[{\"color\":\"blue\"}],[{\"color\":\"green\"}]]}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("xxx.yyy");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_트렐로_카드_이동_파싱_실패() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("push.changes[0].links.html.href");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_트렐로_카드_이동_파싱_실패2() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.changes.links.html.href");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_트렐로_카드_이동_파싱_실패3() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("xx.changes.links.html.href");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_트렐로_카드_이동_파싱_실패4() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.data.listAfter.name.");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_트렐로_카드_이동_파싱_성공() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.data.listAfter.name", "action.data.listBefore.name", "action.data.card.name");

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("zzz", result.get(0));
        Assert.assertEquals("hihi", result.get(1));
        Assert.assertEquals("fdf", result.get(2));
    }

    @Test
    public void test_트렐로_카드_이동_색_추출_성공() throws IOException {
        String payload = " {\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee2fb28788eec79287357\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"listAfter\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"listBefore\":{\"name\":\"hihi\",\"id\":\"5664873e26a53299851af4f8\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"hkyxzI3e\",\"idShort\":16,\"name\":\"fdf\",\"id\":\"566488de4db1051a1811922e\",\"idList\":\"5662abe3bde465fa5b5588c2\"},\"old\":{\"idList\":\"5664873e26a53299851af4f8\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:29:31.531Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("model.prefs.backgroundColor");

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("#0079BF", result.get(0));
    }

    @Test
    public void test_트렐로_카드_편집() throws IOException {
        String payload = "{\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"fds\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569ee4daed44df222eaaea19\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"list\":{\"name\":\"zzz\",\"id\":\"5662abe3bde465fa5b5588c2\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"UNY5VR07\",\"idShort\":18,\"id\":\"56648f4c36e248e8172d717a\",\"name\":\"hello world\"},\"old\":{\"name\":\"fdsa\"}},\"type\":\"updateCard\",\"date\":\"2016-01-20T01:37:30.355Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.data.old.name", "action.data.list.name");

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("fdsa", result.get(0));
        Assert.assertEquals("zzz", result.get(1));
    }

    @Test
    public void test_불리언_타입_처리() throws IOException {
        String payload = "{\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"red\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#B04632\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569f1014e9868571f214b008\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"list\":{\"name\":\"hoho\",\"id\":\"5664874037075df7b71945d5\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"IYIzKkI9\",\"idShort\":34,\"name\":\"fff\",\"id\":\"569f0e832d0162b37797071c\",\"closed\":false},\"old\":{\"closed\":true}},\"type\":\"updateCard\",\"date\":\"2016-01-20T04:41:56.123Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.data.old.closed");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("true", result.get(0));
    }

    @Test
    public void test_정수형_타입_처리() throws IOException {
        String payload = "{\"model\":{\"id\":\"565bd9d1c0942027fdab7448\",\"name\":\"트렐로 연동\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"pinned\":false,\"url\":\"https://trello.com/b/ZBt9S9V1/-\",\"shortUrl\":\"https://trello.com/b/ZBt9S9V1\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"red\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#B04632\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"}},\"action\":{\"id\":\"569f1014e9868571f214b008\",\"idMemberCreator\":\"54b3c11f154e70c8dbfd3d61\",\"data\":{\"list\":{\"name\":\"hoho\",\"id\":\"5664874037075df7b71945d5\"},\"board\":{\"shortLink\":\"ZBt9S9V1\",\"name\":\"트렐로 연동\",\"id\":\"565bd9d1c0942027fdab7448\"},\"card\":{\"shortLink\":\"IYIzKkI9\",\"idShort\":34,\"name\":\"fff\",\"id\":\"569f0e832d0162b37797071c\",\"closed\":false},\"old\":{\"closed\":true}},\"type\":\"updateCard\",\"date\":\"2016-01-20T04:41:56.123Z\",\"memberCreator\":{\"id\":\"54b3c11f154e70c8dbfd3d61\",\"avatarHash\":null,\"fullName\":\"Hyeonjae\",\"initials\":\"H\",\"username\":\"hyeonjae\"}}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        List<String> result = parser.get("action.data.card.idShort");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("34", result.get(0));
    }

    @Test
    public void test_필드_유무_검사() throws IOException {
        String payload = "{\"push\":{\"changes\":[{\"forced\":false,\"old\":null,\"links\":{\"commits\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commits?include=e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/branch/feature-three\"}},\"created\":true,\"commits\":[{\"hash\":\"e6c96415d27049e20795905debb137a3926796ec\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/e6c96415d27049e20795905debb137a3926796ec\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}}}],\"date\":\"2016-01-18T07:37:03+00:00\",\"message\":\"This is the second title\\n\\n - fix #2-1\\n - fix #2-2\\n\",\"type\":\"commit\"},{\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"29af8d486527459f64e9c00bb4c6155878018367\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/29af8d486527459f64e9c00bb4c6155878018367\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/29af8d486527459f64e9c00bb4c6155878018367\"}}}],\"date\":\"2016-01-18T07:36:33+00:00\",\"message\":\"This is the title\\n\\n - fix #1\\n - fix #2\\n - fix #3\\n\",\"type\":\"commit\"},{\"hash\":\"29af8d486527459f64e9c00bb4c6155878018367\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/29af8d486527459f64e9c00bb4c6155878018367\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/29af8d486527459f64e9c00bb4c6155878018367\"}},\"author\":{\"raw\":\"박현재 <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"5c390fb33770b83d76f63f14055f833e23c4a1f0\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/5c390fb33770b83d76f63f14055f833e23c4a1f0\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/5c390fb33770b83d76f63f14055f833e23c4a1f0\"}}}],\"date\":\"2015-12-02T13:18:16+00:00\",\"message\":\"second commit\\n\",\"type\":\"commit\"},{\"hash\":\"5c390fb33770b83d76f63f14055f833e23c4a1f0\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/5c390fb33770b83d76f63f14055f833e23c4a1f0\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/5c390fb33770b83d76f63f14055f833e23c4a1f0\"}},\"author\":{\"raw\":\"박현재 <hyeonjae.park@nhnent.com>\"},\"parents\":[],\"date\":\"2015-12-02T13:15:07+00:00\",\"message\":\"first commit\\n\",\"type\":\"commit\"}],\"truncated\":false,\"closed\":false,\"new\":{\"links\":{\"commits\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commits/feature-three\"},\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/refs/branches/feature-three\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/branch/feature-three\"}},\"type\":\"branch\",\"name\":\"feature-three\",\"repository\":{\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo\"},\"avatar\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/avatar/32/\"}},\"type\":\"repository\",\"uuid\":\"{e07de9e6-f6c1-4b8a-9921-c1d5047cc4b6}\",\"full_name\":\"hyeonjae_park/bitbucket_test_repo\",\"name\":\"bitbucket_test_repo\"},\"target\":{\"hash\":\"e6c96415d27049e20795905debb137a3926796ec\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/e6c96415d27049e20795905debb137a3926796ec\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/e6c96415d27049e20795905debb137a3926796ec\"}},\"author\":{\"raw\":\"박현재/NE <hyeonjae.park@nhnent.com>\"},\"parents\":[{\"type\":\"commit\",\"hash\":\"9b74015d8170dfae35242d498acc7d90a8f6469f\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo/commit/9b74015d8170dfae35242d498acc7d90a8f6469f\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/commits/9b74015d8170dfae35242d498acc7d90a8f6469f\"}}}],\"date\":\"2016-01-18T07:37:03+00:00\",\"message\":\"This is the second title\\n\\n - fix #2-1\\n - fix #2-2\\n\",\"type\":\"commit\"}}}]},\"actor\":{\"username\":\"hyeonjae_park\",\"type\":\"user\",\"display_name\":\"hyeonjae park\",\"uuid\":\"{118501b7-ebc3-495e-bc3a-59e28e158cc0}\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/users/hyeonjae_park\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/\"},\"avatar\":{\"href\":\"https://bitbucket.org/account/hyeonjae_park/avatar/32/\"}}},\"repository\":{\"website\":\"\",\"scm\":\"git\",\"name\":\"bitbucket_test_repo\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/repositories/hyeonjae_park/bitbucket_test_repo\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo\"},\"avatar\":{\"href\":\"https://bitbucket.org/hyeonjae_park/bitbucket_test_repo/avatar/32/\"}},\"full_name\":\"hyeonjae_park/bitbucket_test_repo\",\"owner\":{\"username\":\"hyeonjae_park\",\"type\":\"user\",\"display_name\":\"hyeonjae park\",\"uuid\":\"{118501b7-ebc3-495e-bc3a-59e28e158cc0}\",\"links\":{\"self\":{\"href\":\"https://api.bitbucket.org/2.0/users/hyeonjae_park\"},\"html\":{\"href\":\"https://bitbucket.org/hyeonjae_park/\"},\"avatar\":{\"href\":\"https://bitbucket.org/account/hyeonjae_park/avatar/32/\"}}},\"type\":\"repository\",\"is_private\":true,\"uuid\":\"{e07de9e6-f6c1-4b8a-9921-c1d5047cc4b6}\"}}";

        JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
        Assert.assertTrue(parser.has("push"));
        Assert.assertTrue(parser.has("push.changes"));
        Assert.assertTrue(parser.has("push.changes[0].forced"));
        Assert.assertTrue(parser.has("push.changes[0].old"));
        Assert.assertFalse(parser.has("push.pull"));
    }

    @Test
    public void test_tokenizer_validator() throws IOException {
        JsonNodeParser jnp = new JsonNodeParser();
        Assert.assertTrue(jnp.validation("aaa.bbb.abc[1].abc[1][12].abc[1].ccc[*].ddd"));
        Assert.assertTrue(jnp.validation("aaa.bbb.abc[1].abc[1][12].abc[1].ccc[*].ddd"));
        Assert.assertTrue(jnp.validation("aaa.bbb.ccc[1]"));
        Assert.assertTrue(jnp.validation("aaa[1][2].bbb"));
        Assert.assertTrue(jnp.validation("aaa[1].bbb.ccc[2]"));
        Assert.assertTrue(jnp.validation("aaa[*][2]"));
        Assert.assertTrue(jnp.validation("aaa"));
    }

    @Test
    public void test_tokenizer_validator_fail() throws IOException {
        JsonNodeParser jnp = new JsonNodeParser();
        Assert.assertFalse(jnp.validation("aaa."));
        Assert.assertFalse(jnp.validation("aaa..bbb"));
        Assert.assertFalse(jnp.validation("aaa[1].[2]"));
        Assert.assertFalse(jnp.validation("aaa[]"));
        Assert.assertFalse(jnp.validation("aaa[**]"));
        Assert.assertFalse(jnp.validation("aaa["));
        Assert.assertFalse(jnp.validation("aaa[b]"));
    }
}
