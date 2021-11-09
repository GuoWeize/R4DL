//package types;
//
//import types.primitive.BoolEntity;
//
///**
// * @author Guo Weize
// * @date 2021/10/2
// */
//public final class NullEntity<T> extends BaseEntity {
//
//    private T unused;
//
//    private final static String TYPE = "\"null\"";
//
//    public T s() {
//        return (new NullEntity<T>()).unused;
//    }
//
//    @Override
//    public String getType() {
//        return TYPE;
//    }
//
//    @Override
//    public BoolEntity isNull() {
//        return BoolEntity.TRUE;
//    }
//
//    @Override
//    public BoolEntity equal(BaseEntity entity) {
//        return entity.isNull();
//    }
//}
