package com.sipios.refactoring.enums;

public enum CustomerEnum {
    STANDARD("STANDARD_CUSTOMER"),
    PREMIUM("PREMIUM_CUSTOMER"),
    PLATINUM("PLATINUM_CUSTOMER");

    private String value;

    CustomerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
