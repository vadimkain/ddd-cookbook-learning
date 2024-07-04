package com.example.domain.subscriberDataUpdate.valueobject;

import com.example.domain.subscriberDataUpdate.common.Result;

import java.util.regex.Pattern;

public class Msisdn implements ValueObject<String> {
    private final String value;
    private static final Pattern isStringConsist10Digits = Pattern.compile("^\\d{10}$");

    private Msisdn(String value) {
        this.value = value;
    }

    public static Result<Msisdn> emerge(String msisdn) {
        if (isStringConsists10Digits(msisdn)) {
            return Result.success(new Msisdn(msisdn));
        } else {
            return Result.failure(new IllegalArgumentException("Msisdn consists of 10 numbers"));
        }
    }

    private static boolean isStringConsists10Digits(String value) {
        return isStringConsist10Digits.matcher(value).matches();
    }

    @Override
    public String value() {
        return this.value;
    }
}
