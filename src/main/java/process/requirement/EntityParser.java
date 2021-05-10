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
import lombok.extern.slf4j.Slf4j;
import util.FormatsConsts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import util.PathConsts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Parse entities from json file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class EntityParser extends StdDeserializer<Object> {

    private static final Map<String, Map<String, BaseEntity>> ENTITIES_ID = new HashMap<>(16);
    private static final Map<BaseEntity, String> REQS2ID = new HashMap<>(16);
    public static final Map<String, Set<BaseEntity>> ENTITIES = new HashMap<>(16);

    public EntityParser() {
        this(null);
    }

    public EntityParser(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<BaseEntity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ENTITIES_ID.clear();
        REQS2ID.clear();
        ENTITIES.clear();
        List<BaseEntity> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        node.forEach(n -> result.add(Inner.parseNode(n)));
        log.info("Finish parse requirement JSON file: " + PathConsts.REQUIREMENT_FILE);
        return result;
    }

    public static String getID(BaseEntity entity) {
        if (! REQS2ID.containsKey(entity)) {
            log.error("Can not find entity: " + entity.toString());
            return "entity ID not found!";
        }
        return REQS2ID.get(entity);
    }

    private static class Inner {
        private static final Map<Predicate<JsonNode>, Function<JsonNode, BaseEntity>> CASES = Map.ofEntries(
            Map.entry((node) -> node.has(FormatsConsts.ENTITY_SIGNAL), Inner::parseEntity),
            Map.entry((node) -> node.has(FormatsConsts.LINK_SIGNAL), Inner::parseLink),
            Map.entry((node) -> node.has(FormatsConsts.SET_SIGNAL), Inner::parseSet),
            Map.entry((node) -> node.has(FormatsConsts.LIST_SIGNAL), Inner::parseList),
            Map.entry((node) -> node.has(FormatsConsts.MAP_SIGNAL), Inner::parseMap),
            Map.entry(JsonNode::isTextual, Inner::parseString),
            Map.entry(JsonNode::isInt, Inner::parseInt),
            Map.entry(JsonNode::isBoolean, Inner::parseBool),
            Map.entry((node) -> (node.isFloat() || node.isDouble()), Inner::parseFloat)
        );

        private static BaseEntity parseNode(JsonNode node) {
            for (var entry: CASES.entrySet()) {
                if (entry.getKey().test(node)) {
                    return entry.getValue().apply(node);
                }
            }
            return null;
        }

        private static BaseEntity parseEntity(JsonNode node) {
            var iterator = node.fields();
            var signal = iterator.next();
            String entityID = signal.getValue().asText();
            var entityNode = iterator.next();
            String type = entityNode.getKey();
            JsonNode fieldsNode = entityNode.getValue();
            BaseEntity entity = generateEntity(type, fieldsNode);
            if (! ENTITIES_ID.containsKey(type)) {
                ENTITIES_ID.put(type, new HashMap<>(32));
                ENTITIES.put(type, new HashSet<>(32));
            }
            ENTITIES_ID.get(type).putIfAbsent(entityID, entity);
            if (entity.isRequirement()) {
                REQS2ID.put(entity, entityID);
            }
            ENTITIES.get(type).add(entity);
            return entity;
        }

        private static BaseEntity generateEntity(String type, JsonNode fieldsNode) {
            BaseEntity result = Builder.newInstance(type);
            fieldsNode.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                BaseEntity fieldValue = parseNode(entry.getValue());
                Builder.setField(result, fieldName, fieldValue);
            });
            return result;
        }

        private static BaseEntity parseLink(JsonNode node) {
            var pair = node.get(FormatsConsts.LINK_SIGNAL).fields().next();
            String entityType = pair.getKey();
            String entityID = pair.getValue().asText();
            return EntityParser.ENTITIES_ID.get(entityType).get(entityID);
        }

        private static BaseEntity parseList(JsonNode node) {
            ListEntity<BaseEntity> result = new ListEntity<>();
            node.get(FormatsConsts.LIST_SIGNAL).forEach(n ->
                result.add(parseNode(n))
            );
            return result;
        }

        private static BaseEntity parseSet(JsonNode node) {
            SetEntity<BaseEntity> result = new SetEntity<>();
            node.get(FormatsConsts.SET_SIGNAL).forEach(n ->
                result.add(parseNode(n))
            );
            return result;
        }

        private static BaseEntity parseMap(JsonNode node) {
            MapEntity<BaseEntity, BaseEntity> result = new MapEntity<>();
            node.get(FormatsConsts.MAP_SIGNAL).forEach(n -> {
                BaseEntity key = parseNode(n.get(FormatsConsts.KEY_SIGNAL));
                BaseEntity value = parseNode(n.get(FormatsConsts.VALUE_SIGNAL));
                result.add(key, value);
            });
            return result;
        }

        private static BaseEntity parseString(JsonNode node) {
            return StringEntity.valueOf(node.asText());
        }

        private static BaseEntity parseInt(JsonNode node) {
            return IntEntity.valueOf(node.asInt());
        }

        private static BaseEntity parseBool(JsonNode node) {
            return BoolEntity.valueOf(node.asBoolean());
        }

        private static BaseEntity parseFloat(JsonNode node) {
            return FloatEntity.valueOf(node.asDouble());
        }

    }
}
