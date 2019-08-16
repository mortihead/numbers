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
     * Тест инициализации счетчика
     */
    @Test
    public void testInitNumber()  {
        incrementor.incrementNumber();
        incrementor.initNumber();
        assertEquals(incrementor.getNumber(), 0);
    }

    /**
     * Тест инкремента: увеличение счетчика до 100
     */
    @Test
    public void testIncrementor_Expect100() {
        incrementor.initNumber();
        System.out.println("maximum value: "+incrementor.getMaximumValue());
        for (int i = 0; i < 100; i++) {
            incrementor.incrementNumber();
        }
        assertEquals(incrementor.getNumber(), 100);
    }


    /**
     * Тест инкремента: - попытка установить MaximumValue меньше текущего счетчика
     */
    @Test
    public void testIncrementor_ExpectNumber0() {
        incrementor.initNumber();
        System.out.println("maximum value: "+incrementor.getMaximumValue());
        for (int i = 0; i < 100; i++) {
            incrementor.incrementNumber();
        }
        incrementor.setMaximumValue(50);
        System.out.println("number: "+incrementor.getNumber());

        assertEquals(incrementor.getNumber(), 0);
    }


    /**
     * Тест MaximumValue = 105
     */
    @Test
    public void testIncrementor_ExpectaMaximumValue105() {
        incrementor.setMaximumValue(105);
        assertEquals(incrementor.getMaximumValue(), 105);
    }

    /**
     * Тест отрицательного значения в setMaximumValue
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumValue_ExpectIllegalArgumentException() {
        incrementor.setMaximumValue(-5);
    }
}