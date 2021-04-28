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
import util.Formats;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Parse entities from json file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class EntityParser extends StdDeserializer<Object> {

    private static class Inner {
        static final Map<Predicate<JsonNode>, Function<JsonNode, BaseEntity>> CASES = Map.of(
                (node) -> node.has(Formats.ENTITY_SIGNAL), Inner::parseEntity,
                (node) -> node.has(Formats.SET_SIGNAL), Inner::parseSet,
                (node) -> node.has(Formats.LIST_SIGNAL), Inner::parseList,
                (node) -> node.has(Formats.MAP_SIGNAL), Inner::parseMap,
                JsonNode::isTextual, Inner::parseString,
                JsonNode::isInt, Inner::parseInt,
                JsonNode::asBoolean, Inner::parseBool,
                (node) -> (node.isFloat() || node.isDouble()), Inner::parseFloat
        );

        static BaseEntity parseNode(JsonNode node) {
            for (var entry: CASES.entrySet()) {
                if (entry.getKey().test(node)) {
                    return entry.getValue().apply(node);
                }
            }
            return null;
        }

        static BaseEntity generateEntity(String type, JsonNode fieldsNode) {
            BaseEntity result = Builder.newInstance(type);
            fieldsNode.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                BaseEntity fieldValue = parseNode(entry.getValue());
                Builder.setField(result, fieldName, fieldValue);
            });
            return result;
        }

        static BaseEntity parseEntity(JsonNode node) {
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                String type = entry.getKey();
                if (! Formats.ENTITY_SIGNAL.equals(type)) {
                    JsonNode fieldsNode = entry.getValue();
                    return generateEntity(type, fieldsNode);
                }
            }
            return null;
        }

        static BaseEntity parseSet(JsonNode node) {
            SetEntity<BaseEntity> result = new SetEntity<>();
            for (JsonNode n: node.get(Formats.SET_SIGNAL)) {
                result.add(parseNode(n));
            }
            return result;
        }

        static BaseEntity parseList(JsonNode node) {
            ListEntity<BaseEntity> result = new ListEntity<>();
            for (JsonNode n: node.get(Formats.LIST_SIGNAL)) {
                result.add(parseNode(n));
            }
            return result;
        }

        static BaseEntity parseMap(JsonNode node) {
            MapEntity<BaseEntity, BaseEntity> result = new MapEntity<>();
            for (JsonNode n: node.get(Formats.MAP_SIGNAL)) {
                BaseEntity key = parseNode(n.get(Formats.KEY_SIGNAL));
                BaseEntity value = parseNode(n.get(Formats.VALUE_SIGNAL));
                result.add(key, value);
            }
            return result;
        }

        static BaseEntity parseString(JsonNode node) {
            return StringEntity.valueOf(node.asText());
        }

        static BaseEntity parseInt(JsonNode node) {
            return IntEntity.valueOf(node.asInt());
        }

        static BaseEntity parseBool(JsonNode node) {
            return BoolEntity.valueOf(node.asBoolean());
        }

        static BaseEntity parseFloat(JsonNode node) {
            return FloatEntity.valueOf(node.asDouble());
        }

    }

    public EntityParser() {
        this(null);
    }

    public EntityParser(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<BaseEntity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        List<BaseEntity> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        for (JsonNode n: node) {
            result.add(Inner.parseNode(n));
        }
        return result;
    }

}
