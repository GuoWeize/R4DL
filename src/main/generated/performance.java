import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: performance.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/model.json
 * @date 2021/05/11 19:35:27
 */
public final class performance extends BaseEntity {
    public IntEntity comp;
    public StringEntity unit;
    public StringEntity metrics;
    public IntEntity value;
    public IntEntity statistics;

    @Override
    public String getType() {
        return "performance";
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isRequirement() {
        return true;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        performance that = (performance)entity;
        return BoolEntity.and(
            BaseEntity.equal(comp, that.comp),
            BaseEntity.equal(unit, that.unit),
            BaseEntity.equal(metrics, that.metrics),
            BaseEntity.equal(value, that.value),
            BaseEntity.equal(statistics, that.statistics)
        );
    }
}
