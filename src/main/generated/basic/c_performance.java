package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_performance.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_performance extends BaseEntity {
    public IntEntity f_comp = IntEntity.valueOf(0);
    public StringEntity f_unit = StringEntity.valueOf("");
    public StringEntity f_metrics = StringEntity.valueOf("");
    public IntEntity f_value = IntEntity.valueOf(0);
    public IntEntity f_statistics = IntEntity.valueOf(0);

    @Override
    public String getType() {
        return "performance";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_performance that = (c_performance)entity;
        return BoolEntity.and(
            f_comp.equal(that.f_comp),
            f_unit.equal(that.f_unit),
            f_metrics.equal(that.f_metrics),
            f_value.equal(that.f_value),
            f_statistics.equal(that.f_statistics)
        );
    }
    public static c_performance newInstance() {
        return new c_performance();
    }

}
