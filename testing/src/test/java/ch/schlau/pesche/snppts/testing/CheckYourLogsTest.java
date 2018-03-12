package ch.schlau.pesche.snppts.testing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * Show how you can test what you have logged
 */
class CheckYourLogsTest {

    private static final Logger logger = LoggerFactory.getLogger(CheckYourLogsTest.class);

    private static ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    /**
     * Add/initialize the listAppender. Allows to verify and assert what was logged.
     */
    @BeforeAll
    static void addListAppender() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.addAppender(listAppender);
    }

    @BeforeEach
    void setUpTest() {
        listAppender.list.clear();
        listAppender.start();
    }

    @Test
    void green_no_warnings() {

        logger.info("logging on INFO keeps the test green");

        assertThat(listAppender.list.size(), greaterThan(0));
        for (ILoggingEvent logLine : listAppender.list) {
            assertThat(logLine.getLevel().toInt(), lessThan(Level.WARN.toInt()));
        }
    }

    @Test
    @Disabled // because it's red...
    void red_here_be_warnings() {

        logger.info("some INFO logs coming along");
        logger.warn("this one turns the test red");

        assertThat(listAppender.list, hasSize(2));
        for (ILoggingEvent logLine : listAppender.list) {
            assertThat(logLine.getLevel().toInt(), lessThan(Level.WARN.toInt()));
        }
    }
}

