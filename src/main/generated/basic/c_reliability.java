package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_reliability.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_reliability extends BaseEntity {
    public StringEntity f_metrics = StringEntity.valueOf("");
    public StringEntity f_time = StringEntity.valueOf("");
    public IntEntity f_comp2 = IntEntity.valueOf(0);
    public IntEntity f_comp1 = IntEntity.valueOf(0);
    public IntEntity f_probability = IntEntity.valueOf(0);

    @Override
    public String getType() {
        return "reliability";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_reliability that = (c_reliability)entity;
        return BoolEntity.and(
            f_metrics.equal(that.f_metrics),
            f_time.equal(that.f_time),
            f_comp2.equal(that.f_comp2),
            f_comp1.equal(that.f_comp1),
            f_probability.equal(that.f_probability)
        );
    }
    public static c_reliability newInstance() {
        return new c_reliability();
    }

}
