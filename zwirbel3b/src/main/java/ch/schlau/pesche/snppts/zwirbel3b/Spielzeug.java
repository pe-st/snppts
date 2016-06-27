package ch.schlau.pesche.snppts.zwirbel3b;

public class Spielzeug {
    final int spielzeugId;

    public Spielzeug(int spielzeugId) {
        this.spielzeugId = spielzeugId;
    }

    public int getSpielzeugId() {
        return spielzeugId;
    }

//    /**
//     * Don't allow subclasses with added state to be equal;
//     * see http://www.artima.com/lejava/articles/equality.html for more about canEqual()
//     * @param other
//     * @return
//     */
//    public boolean canEqual(Object other) {
//        return other instanceof Spielzeug;
//    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Spielzeug))
            return false;

        Spielzeug that = (Spielzeug) o;
//        if (!that.canEqual(this)) {
//            return false;
//        }

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(spielzeugId, that.spielzeugId)
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(spielzeugId)
                .toHashCode();
    }
}
