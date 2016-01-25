# JsonParserNotationParser

## Example

### Test Payload
```json
{
  "friends": [
    {
      "id": 0,
      "name": "Santiago Mcmillan"
    },
    {
      "id": 1,
      "name": "Graham Zimmerman"
    },
    {
      "id": 2,
      "name": "Tabatha Garrison"
    }
  ]
}
```
### Prepare
```java
String payload = readPayload();
ObjectMapper mapper = new ObjectMapper()
JsonNodeParser parser = new JsonNodeParser(mapper.readValue(payload, JsonNode.class));
```

### Basic Usage
```java
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
```

### Wildcard
```java
Assert.assertEquals(3, parser.get("friends[*].id").size());
Assert.assertEquals(3, parser.get("friends[*].name").size());

Assert.assertEquals("Santiago Mcmillan", parser.get("friends[*].name").get(0));
Assert.assertEquals("Graham Zimmerman", parser.get("friends[*].name").get(1));
Assert.assertEquals("Tabatha Garrison", parser.get("friends[*].name").get(2));
```

### Fail Case
```java
Assert.assertEquals(0, parser.get("friends[0].xxx").size());
Assert.assertEquals(0, parser.get("friends[3].name").size());
Assert.assertEquals(0, parser.get("xxx").size());
```
