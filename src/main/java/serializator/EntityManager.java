package serializator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Objects;
import dynamics.Builder;
import dynamics.Compiler;
import parser.TypeManager;
import types.BaseEntity;
import types.collection.ListEntity;
import types.collection.MapEntity;
import types.collection.SetEntity;
import types.primitive.BoolEntity;
import types.primitive.FloatEntity;
import types.primitive.IntEntity;
import types.primitive.StringEntity;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.File;
import java.io.FileInputStream;
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
public final class EntityManager extends StdDeserializer<Object> {

    /** {@literal Map: <customized type> -> {set of all entities of this type} } */
    public static final Map<String, Set<BaseEntity>> ENTITIES = new HashMap<>(16);
    /** {@literal Map: <customized type> -> {Map: <entity ID> -> specific entity} } */
    private static final Map<String, Map<String, BaseEntity>> ENTITIES_ID = new HashMap<>(16);
    private static final Map<BaseEntity, String> REQS2ID = new HashMap<>(64);

    public EntityManager() { this(null); }
    public EntityManager(Class<?> vc) { super(vc); }

    @Override
    public List<BaseEntity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ENTITIES_ID.clear();
        REQS2ID.clear();
        ENTITIES.clear();
        List<BaseEntity> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        node.forEach(n -> result.add(Parser.parseNode(n)));
        log.info("Finish parse requirement JSON file: ");
        return result;
    }

    public static String getId(BaseEntity entity) {
        if (! REQS2ID.containsKey(entity)) {
            log.error("Can not find entity: " + entity.toString());
            return "entity ID not found!";
        }
        return REQS2ID.get(entity);
    }

    public static BaseEntity getEntityById(String type, String id) {
        if (ENTITIES_ID.containsKey(type) && ENTITIES_ID.get(type).containsKey(id)) {
            return ENTITIES_ID.get(type).get(id);
        }
        log.error(String.format("Can not find \"%s\" entity of ID \"%s\"", type, id));
        return null;
    }

    private static class Parser {
        private static final Map<Predicate<JsonNode>, Function<JsonNode, BaseEntity>> CASES = Map.ofEntries(
            Map.entry((node) -> node.has("*"), Parser::parseEntity),
            Map.entry((node) -> node.has("()"), Parser::parseSet),
            Map.entry((node) -> node.has("[]"), Parser::parseList),
            Map.entry((node) -> node.has("{}"), Parser::parseMap),
            Map.entry(JsonNode::isTextual, Parser::parseString),
            Map.entry(JsonNode::isInt, Parser::parseInt),
            Map.entry(JsonNode::isBoolean, Parser::parseBool),
            Map.entry((node) -> (node.isFloat() || node.isDouble()), Parser::parseFloat)
        );

        private static BaseEntity parseNode(JsonNode node) {
            for (var entry: CASES.entrySet()) {
                if (entry.getKey().test(node)) {
                    return entry.getValue().apply(node);
                }
            }
            return Parser.parseLink(node);
        }

        private static BaseEntity parseEntity(JsonNode node) {
            String type = node.get("*").asText();
            String entityId = node.get("#").asText();
            BaseEntity entity = Builder.newInstance(type);
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                if (!Objects.equal(fieldName, "#") && !Objects.equal(fieldName, "*") && !Objects.equal(fieldName, ":")) {
                    BaseEntity fieldValue = parseNode(entry.getValue());
                    Builder.setField(entity, fieldName, fieldValue);
                }
            });
            if (! ENTITIES_ID.containsKey(type)) {
                ENTITIES_ID.put(type, new HashMap<>(32));
                ENTITIES.put(type, new HashSet<>(32));
            }
            ENTITIES_ID.get(type).putIfAbsent(entityId, entity);
            if (TypeManager.isRequirement(type)) {
                REQS2ID.put(entity, entityId);
            }
            ENTITIES.get(type).add(entity);
            return entity;
        }

        private static BaseEntity parseLink(JsonNode node) {
            var pair = node.fields().next();
            String entityType = pair.getKey();
            String entityId = pair.getValue().asText();
            return EntityManager.ENTITIES_ID.get(entityType).get(entityId);
        }

        private static BaseEntity parseList(JsonNode node) {
            ListEntity<BaseEntity> list = new ListEntity<>();
            node.get("[]").forEach(n -> list.add(parseNode(n)));
            return list;
        }

        private static BaseEntity parseSet(JsonNode node) {
            SetEntity<BaseEntity> set = new SetEntity<>();
            node.get("()").forEach(n -> set.add(parseNode(n)));
            return set;
        }

        private static BaseEntity parseMap(JsonNode node) {
            MapEntity<BaseEntity, BaseEntity> map = new MapEntity<>();
            node.get("{}").forEach(n -> {
                BaseEntity key = parseNode(n.get("K"));
                BaseEntity value = parseNode(n.get("V"));
                map.add(key, value);
            });
            return map;
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

    public static void main(String[] args) {
        Compiler.loadPackage("basic");

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new EntityManager());
        mapper.registerModule(module);

        File file = new File("/Users/gwz/Desktop/Code/R4DL/src/main/resources/entities/UAV/entity.json");
        long length = file.length();
        byte[] content = new byte[(int) length];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = new String(content);
        try {
            mapper.readValue(value, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
