package process.requirement;

import base.dynamics.Builder;
import base.type.BaseEntity;
import base.type.collection.ListEntity;
import base.type.collection.MapEntity;
import base.type.collection.SetEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.FloatEntity;
import base.type.primitive.IntEntity;
import base.type.primitive.StringEntity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author gwz
 */
public final class EntityParser extends StdDeserializer<Object> {

    private static final String ENTITY_SIGNAL = "_";
    private static final String MAP_SIGNAL = "_map_";
    private static final String LIST_SIGNAL = "_list_";
    private static final String SET_SIGNAL = "_set_";
    private static final Map<Predicate<JsonNode>, Function<JsonNode, BaseEntity>> CASES = new HashMap<>(8);

    public EntityParser() {
        this(null);
    }

    public EntityParser(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<BaseEntity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        init();
        List<BaseEntity> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        for (JsonNode n: node) {
            result.add(parseNode(n));
        }
        return result;
    }

    private void init() {
        CASES.clear();
        CASES.put((node) -> node.has(ENTITY_SIGNAL), this::parseEntity);
        CASES.put((node) -> node.has(SET_SIGNAL), this::parseSet);
        CASES.put((node) -> node.has(LIST_SIGNAL), this::parseList);
        CASES.put((node) -> node.has(MAP_SIGNAL), this::parseMap);
        CASES.put(JsonNode::isTextual, this::parseString);
        CASES.put(JsonNode::isInt, this::parseInt);
        CASES.put(JsonNode::asBoolean, this::parseBool);
        CASES.put((node) -> (node.isFloat() || node.isDouble()), this::parseFloat);
    }

    private BaseEntity parseNode(JsonNode node) {
        for (var entry: CASES.entrySet()) {
            if (entry.getKey().test(node)) {
                return entry.getValue().apply(node);
            }
        }
        return null;
    }

    private BaseEntity generateEntity(String type, JsonNode fieldsNode) {
        String fieldName;
        BaseEntity fieldValue;
        Object result = Builder.newInstance(type);
        Iterator<Map.Entry<String, JsonNode>> iterator = fieldsNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            fieldName = entry.getKey();
            fieldValue = parseNode(entry.getValue());
            Builder.setField(type, fieldName, result, fieldValue);
        }
        return (BaseEntity) result;
    }

    private BaseEntity parseEntity(JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String type = entry.getKey();
            if (! ENTITY_SIGNAL.equals(type)) {
                JsonNode fieldsNode = entry.getValue();
                return generateEntity(type, fieldsNode);
            }
        }
        return null;
    }

    private BaseEntity parseSet(JsonNode node) {
        SetEntity<BaseEntity> result = new SetEntity<>();
        for (JsonNode n: node.get(SET_SIGNAL)) {
            result.add(parseNode(n));
        }
        return result;
    }

    private BaseEntity parseList(JsonNode node) {
        ListEntity<BaseEntity> result = new ListEntity<>();
        for (JsonNode n: node.get(SET_SIGNAL)) {
            result.add(parseNode(n));
        }
        return result;
    }

    private BaseEntity parseMap(JsonNode node) {
        MapEntity<BaseEntity, BaseEntity> result = new MapEntity<>();
        for (JsonNode n: node.get(MAP_SIGNAL)) {
            BaseEntity key = parseNode(n.get("key"));
            BaseEntity value = parseNode(n.get("value"));
            result.add(key, value);
        }
        return result;
    }

    private BaseEntity parseString(JsonNode node) {
        return new StringEntity(node.asText());
    }

    private BaseEntity parseInt(JsonNode node) {
        return new IntEntity(node.asInt());
    }

    private BaseEntity parseBool(JsonNode node) {
        return new BoolEntity(node.asBoolean());
    }

    private BaseEntity parseFloat(JsonNode node) {
        return new FloatEntity(node.asDouble());
    }

}
