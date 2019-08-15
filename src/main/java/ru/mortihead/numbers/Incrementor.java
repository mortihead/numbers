package ru.mortihead.numbers;

import org.apache.log4j.Logger;
/**
 * Класс работы с инкрементом
 *
 * @author Бочкарев Н.В.
 * @version 0.1
 */
public class Incrementor {
    static final private Logger logger = Logger.getLogger(Incrementor.class);

    // Число для работы
    // При начале работы = 0
    private int number = 0;

    // максимальное значение числа
    // По умолчанию максимум -- максимальное значение int.
    private int maximumValue = Integer.MAX_VALUE;

    /**
     * Возвращает текущее число.
     *
     * @return текущее число
     */
    public int getNumber() {
        return number;
    }

    /**
     * Увеличивает текущее число number на один.
     * Когда текущее число достигает maximumValue, number обнуляется
     * @see Incrementor#setMaximumValue(int)
     */
    public void incrementNumber() {
        number++;
        if (number == getMaximumValue()) {
            logger.debug("number достигнуто maximumValue, по условию задачи значение будет обнулено");
            number = 0;
        }
        logger.debug(String.format("Значение установлено: %d", number));
    }

    /**
     * Устанавливает максимальное значение текущего числа.
     * Если при смене максимального значения число резко начинает
     * превышать максимальное значение, то число = 0.
     * Нельзя позволять установить < 0.
     *
     * @param value значение, которое устанавливается для maximumValue
     */
    public void setMaximumValue(int value) {
        if (value >= 0) {
            maximumValue = value;
            if (number > value) {
                logger.warn("При попытке смены максимального значения, число стало превышать его. Обнуляем число!");
                number = 0;
            }
        } else {
            logger.warn("Была попытка установить максимальное значение < 0. Операция отменена.");
        }
        logger.debug("Максимальное значение: " + maximumValue);
    }

    /**
     * Возвращает maximumValue
     *
     * @return значение maximumValue
     */
    public int getMaximumValue() {
        return maximumValue;
    }

}


