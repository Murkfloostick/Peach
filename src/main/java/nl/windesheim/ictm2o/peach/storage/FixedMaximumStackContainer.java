package nl.windesheim.ictm2o.peach.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

// LIFO container with a fixed maximum size
// data[0] is the last element
// data[count - 1] is the first element
public class FixedMaximumStackContainer<T> implements Iterable<T> {

    private class Iter implements Iterator<T> {

        private int index = FixedMaximumStackContainer.this.count - 1;

        @Override
        public boolean hasNext() {
            return index >= 0;
        }

        @Override
        @Nullable
        public T next() {
            if (index >= FixedMaximumStackContainer.this.data.length)
                throw new IllegalStateException("next() called when no next is available");
            return cast(FixedMaximumStackContainer.this.data[index--]);
        }
    }

    private final Object[] data;
    private int count = 0;

    public FixedMaximumStackContainer(int maximum) {
        if (maximum <= 0)
            throw new IllegalArgumentException("maximum should be greater than 0");
        data = new Object[maximum];
    }

    public int maximum() {
        return data.length;
    }

    public int count() {
        return count;
    }

    public boolean isFull() {
        return count == data.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    @Nullable
    public T get(int index) {
        return cast(data[index]);
    }

    public void push(@Nullable T element) {
        if (isFull()) {
            // Optimized loop using System.arraycopy
            if (data.length - 1 >= 0) {
                System.arraycopy(data, 1, data, 0, data.length - 2);
            }
            data[data.length - 1] = element;
        } else {
            data[count++] = element;
        }
    }

    public void remove(int index) {
        if (index > count)
            throw new IndexOutOfBoundsException(index + " > " + count);

        if (index == count - 1)
            data[index] = null;
        else if (index == 0)
            System.arraycopy(data, 1, data, 0, data.length - 1);
        else
            System.arraycopy(data, index + 1, data, index + 1 - 1, count - (index + 1));

        --count;
    }

    // Remove the last item in the list (also the last added)
    public void popBack() {
        if (count == 0)
            throw new IllegalStateException("cannot pop when empty");
        System.arraycopy(data, 1, data, 0, count - 1);
        --count;
    }

    // Remove the first item in the list (also the first added)
    public void popFront() {
        if (count == 0)
            throw new IllegalStateException("cannot pop when empty");
        data[--count] = null;
    }

    // Get the first element.
    @Nullable
    public T front() {
        if (count == 0)
            return null;
        return cast(data[count - 1]);
    }

    // Get the last element.
    @Nullable
    public T back() {
        return cast(data[0]);
    }

    @NotNull
    public Iterator<T> iterator() {
        return new Iter();
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }

}
