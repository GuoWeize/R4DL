import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class Performance extends BaseEntity {
    public IntEntity comp;
    public StringEntity unit;
    public StringEntity metrics;
    public IntEntity value;
    public IntEntity statistics;

    @Override
    public String getType() { return "Performance"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return true; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        Performance that = (Performance) entity;
        return BoolEntity.valueOf(
                   comp.equal(that.comp).getValue() &&
                   unit.equal(that.unit).getValue() &&
                   metrics.equal(that.metrics).getValue() &&
                   value.equal(that.value).getValue() &&
                   statistics.equal(that.statistics).getValue()
        );
    }
}