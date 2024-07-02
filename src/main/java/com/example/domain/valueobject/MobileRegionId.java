package com.example.domain.valueobject;

import com.example.domain.common.Result;

import java.util.regex.Pattern;

public class MobileRegionId implements ValueObject<String> {
    private final String value;
    private static final Pattern isStringConsist4Digits = Pattern.compile("^\\d{1,4}$");

    private MobileRegionId(String value) {
        this.value = value;
    }

    public static Result<MobileRegionId> emerge(String mobileRegionId) {
        if (isStringWith4Digits(mobileRegionId)) {
            return Result.success(new MobileRegionId(mobileRegionId));
        } else {
            return Result.failure(new IllegalArgumentException("Mobile Region Id consists of numbers maximum length 4"));
        }
    }

    private static boolean isStringWith4Digits(String value) {
        return isStringConsist4Digits.matcher(value).matches();
    }

    @Override
    public String value() {
        return value;
    }
}
