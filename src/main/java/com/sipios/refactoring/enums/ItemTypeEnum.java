package com.sipios.refactoring.enums;

public enum ItemTypeEnum {
    TSHIRT("TSHIRT"),
    DRESS("DRESS"),
    JACKET("JACKET");

    private String value;

    ItemTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
