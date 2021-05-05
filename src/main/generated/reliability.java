import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: reliability.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/model.json
 * @date 2021/05/05 16:50:16
 */
public final class reliability extends BaseEntity {
    public StringEntity metrics;
    public StringEntity time;
    public IntEntity comp2;
    public IntEntity comp1;
    public IntEntity probability;

    @Override
    public String getType() {
        return "reliability";
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
        reliability that = (reliability)entity;
        return BoolEntity.and(
            metrics.equal(that.metrics),
            time.equal(that.time),
            comp2.equal(that.comp2),
            comp1.equal(that.comp1),
            probability.equal(that.probability)
        );
    }
}