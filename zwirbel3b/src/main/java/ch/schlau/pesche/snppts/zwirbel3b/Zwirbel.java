package ch.schlau.pesche.snppts.zwirbel3b;

public final class Zwirbel extends Spielzeug {
    int currentRotation;

    public Zwirbel(int spielzeugId) {
        super(spielzeugId);
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }

//    @Override
//    public boolean canEqual(Object other) {
//        return other instanceof Zwirbel;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        Zwirbel that = (Zwirbel) o;
////        if (!that.canEqual(this)) {
////            return false;
////        }
//
//        return new EqualsBuilder()
//                .appendSuper(super.equals(o))
//                .isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37)
//                .appendSuper(super.hashCode())
//                .toHashCode();
//    }
}
