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
import util.FormatsConsts;

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
        static final Map<Predicate<JsonNode>, Function<JsonNode, BaseEntity>> CASES = Map.ofEntries(
            Map.entry((node) -> node.has(FormatsConsts.ENTITY_SIGNAL), Inner::parseEntity),
            Map.entry((node) -> node.has(FormatsConsts.SET_SIGNAL), Inner::parseSet),
            Map.entry((node) -> node.has(FormatsConsts.LIST_SIGNAL), Inner::parseList),
            Map.entry((node) -> node.has(FormatsConsts.MAP_SIGNAL), Inner::parseMap),
            Map.entry(JsonNode::isTextual, Inner::parseString),
            Map.entry(JsonNode::isInt, Inner::parseInt),
            Map.entry(JsonNode::asBoolean, Inner::parseBool),
            Map.entry((node) -> (node.isFloat() || node.isDouble()), Inner::parseFloat)
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
                if (! Objects.equals(type, FormatsConsts.ENTITY_SIGNAL)) {
                    JsonNode fieldsNode = entry.getValue();
                    return generateEntity(type, fieldsNode);
                }
            }
            return null;
        }

        static BaseEntity parseSet(JsonNode node) {
            SetEntity<BaseEntity> result = new SetEntity<>();
            for (JsonNode n: node.get(FormatsConsts.SET_SIGNAL)) {
                result.add(parseNode(n));
            }
            return result;
        }

        static BaseEntity parseList(JsonNode node) {
            ListEntity<BaseEntity> result = new ListEntity<>();
            for (JsonNode n: node.get(FormatsConsts.LIST_SIGNAL)) {
                result.add(parseNode(n));
            }
            return result;
        }

        static BaseEntity parseMap(JsonNode node) {
            MapEntity<BaseEntity, BaseEntity> result = new MapEntity<>();
            for (JsonNode n: node.get(FormatsConsts.MAP_SIGNAL)) {
                BaseEntity key = parseNode(n.get(FormatsConsts.KEY_SIGNAL));
                BaseEntity value = parseNode(n.get(FormatsConsts.VALUE_SIGNAL));
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
