package ch.schlau.pesche.snppts.functional;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import io.vavr.control.Option;

public class Java8Optional {

    public static String callMethodIsPresentAndGet(String nullableString) {
        Optional<String> optionalString = Optional.ofNullable(nullableString);
        if (optionalString.isPresent()) {
            return optionalString.get().toUpperCase();
        } else {
            return null;
        }
    }

    public static String callMethodFunctionalStyle(String nullableString) {
        Optional<String> optionalString = Optional.ofNullable(nullableString);
        return optionalString.map(String::toUpperCase).orElse(null);
    }

    public static String callMethodVavr(String nullableString) {
        Option<String> optionalString = Option.of(nullableString);
        return optionalString.map(String::toUpperCase).getOrNull();
    }

    public static String callFunctionIsPresentAndGet(String nullableString) {
        Optional<String> optionalString = Optional.ofNullable(nullableString);
        if (optionalString.isPresent()) {
            return StringUtils.reverse(optionalString.get());
        } else {
            return null;
        }
    }

    public static String callFunctionFunctionalStyle(String nullableString) {
        Optional<String> optionalString = Optional.ofNullable(nullableString);
        return optionalString.map(StringUtils::reverse).orElse(null);
    }

    public static String callFunctionVavr(String nullableString) {
        Option<String> optionalString = Option.of(nullableString);
        return optionalString.map(StringUtils::reverse).getOrNull();
    }
}
