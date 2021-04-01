import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class reliability extends BaseEntity {
    public StringEntity metrics;
    public StringEntity time;
    public IntEntity comp2;
    public IntEntity comp1;
    public IntEntity probability;

    @Override
    public String getType() { return "reliability"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return true; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        reliability that = (reliability) entity;
        return BoolEntity.valueOf(
                   metrics.equal(that.metrics).getValue() &&
                   time.equal(that.time).getValue() &&
                   comp2.equal(that.comp2).getValue() &&
                   comp1.equal(that.comp1).getValue() &&
                   probability.equal(that.probability).getValue()
        );
    }
}