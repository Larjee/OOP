package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class HashTableTest {

    // ======================================================================
    // ===================== Create and basic operations =====================
    // ======================================================================
    @Test
    void defaultConstructorCreatesEmptyTable() {
        HashTable<Integer, String> table = new HashTable<>();

        assertEquals(0, table.getSize());
        assertEquals(16, table.table.length); // DEFAULT_CAPACITY
        assertEquals("[]", table.toString());
        assertFalse(table.contains(1));
        assertNull(table.get(1));
    }

    @Test
    void customConstructorCreatesEmptyTableWithGivenCapacityAndLoadFactor() {
        HashTable<Integer, String> table = new HashTable<>(4, 0.5f);

        assertEquals(0, table.getSize());
        assertEquals(4, table.table.length);
        assertEquals("[]", table.toString());
    }

    @Test
    void constructorThrowsOnIllegalCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, String>(0, 0.75f));
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, String>(-5, 0.75f));
    }

    @Test
    void constructorThrowsOnIllegalLoadFactor() {
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, String>(8, 0.0f));
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, String>(8, -1.0f));
    }

    @Test
    void addAndGetSingleElement() {
        HashTable<Integer, String> table = new HashTable<>();

        assertNull(table.add(1, "one"));
        assertEquals(1, table.getSize());
        assertEquals("one", table.get(1));
        assertTrue(table.contains(1));
    }

    @Test
    void addUpdatesExistingKeyAndReturnsOldValue() {
        HashTable<Integer, String> table = new HashTable<>();

        assertNull(table.add(1, "one"));
        int sizeAfterFirst = table.getSize();
        String old = table.add(1, "newOne");

        assertEquals("one", old); // вернул старое
        assertEquals(sizeAfterFirst, table.getSize()); // размер не изменился
        assertEquals("newOne", table.get(1)); // значение обновилось
    }

    @Test
    void removeExistingKeyReturnsValueAndDecreasesSize() {
        HashTable<Integer, String> table = new HashTable<>();

        table.add(1, "one");
        table.add(2, "two");
        table.add(3, "three");

        int sizeBefore = table.getSize();
        String removed = table.remove(2);

        assertEquals("two", removed);
        assertEquals(sizeBefore - 1, table.getSize());
        assertNull(table.get(2));
        assertFalse(table.contains(2));

        assertEquals("one", table.get(1));
        assertEquals("three", table.get(3));
    }

    @Test
    void removeNonExistingKeyReturnsNullAndDoesNotChangeSize() {
        HashTable<Integer, String> table = new HashTable<>();

        table.add(1, "one");
        int sizeBefore = table.getSize();

        assertNull(table.remove(42));
        assertEquals(sizeBefore, table.getSize());
    }

    @Test
    void clearRemovesAllElements() {
        HashTable<Integer, String> table = new HashTable<>();

        table.add(1, "one");
        table.add(2, "two");
        table.add(3, "three");

        table.clear();

        assertEquals(0, table.getSize());
        assertEquals("[]", table.toString());
        assertNull(table.get(1));
        assertNull(table.get(2));
        assertNull(table.get(3));

        Iterator<Node<Integer, String>> it = table.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void containsReturnsTrueOnlyForExistingKeys() {
        HashTable<Integer, String> table = new HashTable<>();

        table.add(1, "one");
        table.add(2, "two");

        assertTrue(table.contains(1));
        assertTrue(table.contains(2));
        assertFalse(table.contains(3));

        table.remove(1);
        assertFalse(table.contains(1));
    }

    @Test
    void operationsWithNullKeyThrowNullPointerException() {
        HashTable<String, String> table = new HashTable<>();

        assertThrows(NullPointerException.class, () -> table.add(null, "x"));
        assertThrows(NullPointerException.class, () -> table.get(null));
        assertThrows(NullPointerException.class, () -> table.remove(null));
        assertThrows(NullPointerException.class, () -> table.contains(null));
    }


    // ======================================================================
    // =================== Resize and collisions resolving ===================
    // ======================================================================
    @Test
    void resizeTriggeredAndPreservesEntries() {
        HashTable<Integer, String> table = new HashTable<>(2, 0.75f);
        int oldCapacity = table.table.length;

        table.add(1, "one");
        table.add(2, "two");

        int newCapacity = table.table.length;
        assertTrue(newCapacity > oldCapacity, "Capacity should grow after resize");

        assertEquals(2, table.getSize());
        assertEquals("one", table.get(1));
        assertEquals("two", table.get(2));
    }

    @Test
    void separateChainingHandlesCollisionsCorrectly() {
        HashTable<Integer, String> table = new HashTable<>();

        //при capacity=16 у 1 и 17 одинаковый индекс
        table.add(1, "one");
        table.add(17, "seventeen");

        assertEquals(2, table.getSize());
        assertEquals("one", table.get(1));
        assertEquals("seventeen", table.get(17));

        //удаляем голову цепочки, 17 добавлен последним
        String removedHead = table.remove(17);
        assertEquals("seventeen", removedHead);
        assertNull(table.get(17));
        assertEquals("one", table.get(1));

        //удаляем хвост / единственный оставшийся
        String removedTail = table.remove(1);
        assertEquals("one", removedTail);
        assertEquals(0, table.getSize());
        assertNull(table.get(1));
    }


    // ======================================================================
    // ================= Iterator and ConcurrentModification =================
    // ======================================================================
    @Test
    void iteratorTraversesAllElements() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");
        table.add(2, "two");
        table.add(3, "three");

        Set<Integer> keys = new HashSet<>();
        Set<String> values = new HashSet<>();

        for (Node<Integer, String> node : table) {
            keys.add(node.getKey());
            values.add(node.getValue());
        }

        assertEquals(3, keys.size());
        assertEquals(3, values.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertTrue(keys.contains(3));
        assertTrue(values.contains("one"));
        assertTrue(values.contains("two"));
        assertTrue(values.contains("three"));
    }

    @Test
    void iteratorOnEmptyTableHasNoElements() {
        HashTable<Integer, String> table = new HashTable<>();

        Iterator<Node<Integer, String>> it = table.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorDetectsConcurrentModificationOnNext() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");
        table.add(2, "two");

        Iterator<Node<Integer, String>> it = table.iterator();

        //до первого next модификаций нет
        assertTrue(it.hasNext());
        Node<Integer, String> first = it.next();
        assertNotNull(first);

        //модифицируем таблицу после создания итератора
        table.add(3, "three");

        //падает
        assertThrows(ConcurrentModificationException.class, it::next);
    }


    // ======================================================================
    // ================= Iterator and ConcurrentModification =================
    // ======================================================================
    @Test
    void equalsSameInstanceIsTrue() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");

        assertTrue(table.equals(table));
    }

    @Test
    void equalsNullIsFalse() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");

        assertFalse(table.equals(null));
    }

    @Test
    void equalsDifferentTypeIsFalse() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");

        assertFalse(table.equals("not a table"));
    }

    @Test
    void equalsTrueForTablesWithSameEntries() {
        HashTable<Integer, String> t1 = new HashTable<>();
        HashTable<Integer, String> t2 = new HashTable<>();

        t1.add(1, "one");
        t1.add(2, "two");

        t2.add(1, "one");
        t2.add(2, "two");

        assertEquals(t1, t2);
        assertEquals(t2, t1); // симметричность
    }

    @Test
    void equalsFalseWhenSizesDiffer() {
        HashTable<Integer, String> t1 = new HashTable<>();
        HashTable<Integer, String> t2 = new HashTable<>();

        t1.add(1, "one");
        t1.add(2, "two");

        t2.add(1, "one");

        assertNotEquals(t1, t2);
        assertNotEquals(t2, t1);
    }

    @Test
    void equalsFalseWhenValuesDiffer() {
        HashTable<Integer, String> t1 = new HashTable<>();
        HashTable<Integer, String> t2 = new HashTable<>();

        t1.add(1, "one");
        t2.add(1, "different");

        assertNotEquals(t1, t2);
    }

    @Test
    void toStringOnEmptyTable() {
        HashTable<Integer, String> table = new HashTable<>();
        assertEquals("[]", table.toString());
    }

    @Test
    void toStringOnNonEmptyTableContainsAllPairs() {
        HashTable<Integer, String> table = new HashTable<>();
        table.add(1, "one");
        table.add(2, "two");

        String s = table.toString();

        assertTrue(s.startsWith("["));
        assertTrue(s.endsWith("]"));
        assertTrue(s.contains("(1, one)"));
        assertTrue(s.contains("(2, two)"));
        assertTrue(s.contains(", "));
    }

}
