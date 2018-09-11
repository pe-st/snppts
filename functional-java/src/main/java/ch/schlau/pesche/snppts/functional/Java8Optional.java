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

    public static void callConsumerFunctionalStyle(String nullableString) {
        Optional<String> optionalString = Optional.ofNullable(nullableString);
        optionalString.ifPresent(s -> System.out.printf("consuming %s\n", s));

        // In Java 8 there is no proper solution for if-not-present, here's a workaround returning an ignored null
        // In Java 9 there is ifPresentOrElse
        optionalString.orElseGet(() -> {
            System.out.printf("consuming null\n");
            return null;
        });
    }

    public static void callConsumerVavr(String nullableString) {
        Option<String> optionalString = Option.of(nullableString);
        optionalString
                .peek(s -> System.out.printf("1=%s\n", s)).peek(s -> System.out.printf("2=%s\n", s))
                .onEmpty(() -> System.out.printf("3=null\n"));
    }
}
