package ru.mortihead.numbers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IncrementorTest {
    private Incrementor incrementor;

    @Before
    public void initTest() {
        incrementor = new Incrementor();
    }

    /**
     * Сделал один тест, который покрывает все методы
     */
    @Test
    public void testIncrementor() {
        System.out.println("maximum value: "+incrementor.getMaximumValue());
        for (int i = 0; i < 100; i++) {
            incrementor.incrementNumber();
        }
        assertEquals(incrementor.getNumber(), 100);
        System.out.println("number: "+incrementor.getNumber());

        incrementor.setMaximumValue(-5);
        assertFalse(incrementor.getNumber() < 0);

        incrementor.setMaximumValue(105);
        assertEquals(incrementor.getMaximumValue(), 105);

        System.out.println("number: "+incrementor.getNumber());

        for (int i = 0; i < 10; i++) {
            incrementor.incrementNumber();
        }
        System.out.println("number: "+incrementor.getNumber());
        assertEquals(incrementor.getNumber(), 5);
    }

    @Test
    public void setMaximumValue() throws Exception {
        incrementor.setMaximumValue(-5);
        assertFalse(incrementor.getMaximumValue() < 0);
    }
}