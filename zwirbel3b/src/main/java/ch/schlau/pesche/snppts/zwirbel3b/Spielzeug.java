package ch.schlau.pesche.snppts.zwirbel3b;

public class Spielzeug {
    final int spielzeugId;

    public Spielzeug(int spielzeugId) {
        this.spielzeugId = spielzeugId;
    }

    public int getSpielzeugId() {
        return spielzeugId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Spielzeug))
            return false;

        Spielzeug that = (Spielzeug) o;

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
