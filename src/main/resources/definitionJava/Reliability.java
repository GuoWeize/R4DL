import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class Reliability extends BaseEntity {
    public StringEntity metrics;
    public StringEntity time;
    public IntEntity comp2;
    public IntEntity comp1;
    public IntEntity probability;

    @Override
    public String getType() { return "Reliability"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return true; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        Reliability that = (Reliability) entity;
        return BoolEntity.valueOf(
                   metrics.equal(that.metrics).getValue() &&
                   time.equal(that.time).getValue() &&
                   comp2.equal(that.comp2).getValue() &&
                   comp1.equal(that.comp1).getValue() &&
                   probability.equal(that.probability).getValue()
        );
    }
}