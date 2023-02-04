package com.mantis.MantisNotesIterationOne;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void assertDatesAreEquals() {
        Date date1 = new Date();
        Date date2 = new Date();
        assertTrue( date1.equals( date2 ) );
    }
}