package ch.schlau.pesche.snppts.functional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

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
}
