package ch.schlau.pesche.snppts.json.emv;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * Simplifies enums with an additional value (like {@code SOME_ENUM("123")}).
 * <p>
 * For an example see the unit test...
 * <p>
 *
 * @param <V> the value type (usually {@link String} for examples like {@code SOME_ENUM("123")})
 * @param <E> the enum type, must implement {@link MappableEnum} with the value type {@code V}
 */
public class EnumMapper<E extends Enum<E> & MappableEnum<V>, V> {
    private final Class<E> enumClass;
    private final Map<V, E> map = new HashMap<>();
    private final E defaultValue;

    /**
     * Creates a mapper for the given enum. The defaultValue will be set to null
     *
     * @param enumToMap
     */
    public EnumMapper(Class<E> enumToMap) {
        this(enumToMap, null);
    }

    /**
     * Creates a mapper for the given enum. The given default value will be returned, if the
     * value cannot be mapped to an enum value.
     *
     * @param enumToMap
     * @param defaultValue
     */
    public EnumMapper(Class<E> enumToMap, E defaultValue) {
        this.enumClass = enumToMap;
        this.defaultValue = defaultValue;
        for (E e : enumToMap.getEnumConstants()) {
            map.put(e.getValue(), e);
        }
    }

    /**
     * Gets the most appropriate enum from the map
     *
     * @param value the value to map from
     * @return the enum, the default or null if no default was specified
     */
    public E get(V value) {
        return map.getOrDefault(value, defaultValue);
    }

    /**
     * Gets the matching enum from the map (if there is one)
     *
     * @param value the value to map from
     * @return the enum (never the default or null)
     *
     * @throws IllegalArgumentException if the value is not found
     */
    public E of(V value) {
        Validate.isTrue(map.containsKey(value), String.format("Unknown %s: %s", enumClass.getSimpleName(), value));
        return map.get(value);
    }
}
