package com.example.android.customarraylist;

import java.lang.reflect.Array;
import java.util.Arrays;

import static android.R.attr.id;

/**
 * Created by kevinsun on 10/22/17.
 */

public class CustomArraylist<T> {

    private Object[] element;

    private int size;

    public CustomArraylist() {
        element = new Object[10];
        size = 0;

    }

    public void add(T t) {

        checkArrayLength(size + 1);

        element[size++] = t;
    }

    public T remove(int index) {

        checkIndex(index);

        T oldValue = (T) element[index];

        int numberMoved = size - index - 1;

        if (numberMoved > 0) {
            System.arraycopy(element, index + 1, element, index, numberMoved);
        }

        element[--size] = null;

        return oldValue;
    }

    public T get(int index) {

        checkIndex(index);

        return (T) element[index];
    }

    public T set(int index, T element1) {

        checkIndex(index);

        T oldValue = (T) element[index];

        element[index] = element1;

        return oldValue;
    }

    public boolean contains(Object o) {
        for (Object t : element) {
            if (t == null && o == null || (t != null && t.equals(o))) {
                return true;
            }
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ". Size: " + size);
        }
    }

    private void checkArrayLength(int length) {
        if (length > element.length) {
            grow(length);
        }
    }

    private void grow(int length) {
        int oldLength = element.length;

        int newLength = oldLength + (oldLength >> 1);

        element = Arrays.copyOf(element, newLength);
    }

}
