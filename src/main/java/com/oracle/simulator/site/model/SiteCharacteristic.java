package com.oracle.simulator.site.model;

/**
 * Characteristics that represent the feature at a point in the SiteMap.
 *
 * @author Joby
 *
 */
public enum SiteCharacteristic {
    PLAIN_LAND('o'), ROCKY_LAND('r'), REMOVABLE_TREE('t'), PROTECTED_TREE('T');

    SiteCharacteristic(Character value) {
        this.value = value;
    }

    public Character getValue() {
        return value;
    }

    public static SiteCharacteristic mapToEnum(Character value) {
        for (SiteCharacteristic enumVal : SiteCharacteristic.values()) {
            if (enumVal.getValue().equals(value)) {
                return enumVal;
            }
        }
        return null;
    }

    private Character value;

    @Override
    public String toString() {
        return Character.toString(value);
    }
}
