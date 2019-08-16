package ru.mortihead.numbers;

import org.apache.log4j.Logger;

/**
 * Класс работы с инкрементом
 *
 * @author Бочкарев Н.В.
 * @version 0.2
 * <p>
 * Доступны методы:
 * @see Incrementor#incrementNumber()
 * Увеличение "счетчика"
 * @see Incrementor#setMaximumValue(int)
 * Установка максимального значения
 * @see Incrementor#getNumber()
 * Получение значения
 */
public class Incrementor {
    static final private Logger logger = Logger.getLogger(Incrementor.class);

    // Число для работы
    private int number;

    // максимальное значение числа
    // По умолчанию максимум -- максимальное значение int.
    private int maximumValue = Integer.MAX_VALUE;

    /**
     * Конструктор
     */
    public Incrementor() {
        // При начале работы = 0
        initNumber();
    }

    /**
     * Обнуляет текущее число.
     * Этого метода не было в постановке, но использование инкремента
     * для нескольких "сессий" потребовало его создания
     */
    public void initNumber() {
        number = 0;
    }


    /**
     * Возвращает текущее число.
     *
     * @return текущее число счетчика
     */
    public int getNumber() {
        return number;
    }

    /**
     * Увеличивает текущее число счетчика number на один.
     * Когда при вызове incrementNumber() текущее число достигает maximumValue
     * number обнуляется
     *
     * @see Incrementor#setMaximumValue(int)
     */
    public void incrementNumber() {
        number++;
        // В текущей постановке max(number) == getMaximumValue()-1
        // TODO: Уточнить постановку, насколько это правильно?
        if (number == getMaximumValue()) {
            logger.debug("number == maximumValue, set number to 0");
            number = 0;
        }
        logger.debug(String.format("number: %d", number));
    }

    /**
     * Устанавливает максимальное значение текущего числа.
     * Если при смене максимального значения число резко начинает
     * превышать максимальное значение, то число = 0.
     * Нельзя позволять установить < 0.
     *
     * @param value значение, которое устанавливается для maximumValue
     * @throws IllegalArgumentException (optional) при попытке установить
     *                                  значение < 0
     */
    public void setMaximumValue(int value) throws IllegalArgumentException {
        if (value >= 0) {
            maximumValue = value;
            if (number > value) {
                logger.warn("number > maximumValue. Set value to 0!");
                number = 0;
            }
        } else {
            throw new IllegalArgumentException("Была попытка установить максимальное значение < 0");
        }
        logger.debug("maximumValue: " + maximumValue);
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


