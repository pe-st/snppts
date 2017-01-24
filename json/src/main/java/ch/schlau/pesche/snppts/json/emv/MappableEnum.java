package ch.schlau.pesche.snppts.json.emv;

/**
 * An interface which can be used together with {@link EnumMapper}.
 * <p>
 * @param <V> type that must be returned by the {@link #getValue()} method.
 */
public interface MappableEnum<V> {
    /**
     * Get the value assigned to this enum
     * @return
     */
    V getValue();
}
