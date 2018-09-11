package ch.schlau.pesche.snppts.functional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.vavr.control.Option;

class Java8OptionalTest {

    /**
     * nullsafe call to String::toUpperCase
     */
    @Test
    void callMethodNullsafe() {

        assertThat(Java8Optional.callMethodIsPresentAndGet("gugus"), is("GUGUS"));
        assertThat(Java8Optional.callMethodIsPresentAndGet(null), is(nullValue()));

        assertThat(Java8Optional.callMethodFunctionalStyle("gugus"), is("GUGUS"));
        assertThat(Java8Optional.callMethodFunctionalStyle(null), is(nullValue()));

        assertThat(Java8Optional.callMethodVavr("gugus"), is("GUGUS"));
        assertThat(Java8Optional.callMethodVavr(null), is(nullValue()));
    }

    /**
     * nullsafe call to StringUtils.reverse
     */
    @Test
    void callFunctionNullsafe() {

        assertThat(Java8Optional.callFunctionIsPresentAndGet("gugus"), is("sugug"));
        assertThat(Java8Optional.callFunctionIsPresentAndGet(null), is(nullValue()));

        assertThat(Java8Optional.callFunctionFunctionalStyle("gugus"), is("sugug"));
        assertThat(Java8Optional.callFunctionFunctionalStyle(null), is(nullValue()));

        assertThat(Java8Optional.callFunctionVavr("gugus"), is("sugug"));
        assertThat(Java8Optional.callFunctionVavr(null), is(nullValue()));
    }

    /**
     * nullsafe call to System.out.printf
     */
    @Test
    void callConsumerNullsafe() {

        assertDoesNotThrow(() -> Java8Optional.callConsumerFunctionalStyle("gugus"));
        assertDoesNotThrow(() -> Java8Optional.callConsumerFunctionalStyle(null));

        assertDoesNotThrow(() -> Java8Optional.callConsumerVavr("gugus"));
        assertDoesNotThrow(() -> Java8Optional.callConsumerVavr(null));
    }

    @Test
    void flatMap_functionReturningNull() {

        // Java 8 Optional
        assertThrows(NullPointerException.class, () -> Optional.ofNullable("42").flatMap(v -> null));

        // Vavr Option
        assertThat(Option.of("42").flatMap(v -> null), is(nullValue()));
    }
}
