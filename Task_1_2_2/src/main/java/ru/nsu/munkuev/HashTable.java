package ru.nsu.munkuev;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Objects;

/**
 * Реализация хеш-таблицы, отображений ключей {@code K} в значения {@code V}.
 * <p>
 * Внутри таблица хранит элементы в массиве бакетов. Каждый бакет представляет собой
 * односвязный список из узлов {@link Node}, в каждом из которых лежит пара {@code (key, value)}.
 * Коллизии разрешаются методом цепочек.
 * </p>
 *
 * <p>
 * Основные операции:
 * <ul>
 *     <li>{@link #add(Object, Object)} — добавление или обновление пары {@code (key, value)};</li>
 *     <li>{@link #get(Object)} — получение значения по ключу;</li>
 *     <li>{@link #remove(Object)} — удаление пары по ключу;</li>
 *     <li>{@link #contains(Object)} — проверка наличия ключа;</li>
 *     <li>{@link #clear()} — полная очистка таблицы.</li>
 * </ul>
 * При нормальной загрузке таблицы амортизированная сложность этих операций близка к {@code O(1)}.
 * </p>
 *
 * <p>
 * Вместимость таблицы увеличивается автоматически при превышении порога загрузки
 * ({@code threshold}).
 *      <blockquote><pre>
 *          {@code threshold = capacity * loadFactor}
 *      </pre></blockquote>
 * При ресайзе все существующие узлы перераспределяются по новому массиву бакетов.
 * </p>
 *
 * <p>
 * Ключи не могут быть {@code null}. Попытка передать {@code null} в методы
 * {@link #add(Object, Object)}, {@link #get(Object)}, {@link #remove(Object)} или
 * {@link #contains(Object)} приведёт к выбросу {@link NullPointerException}.
 * Значения {@code V} допускается хранить равными {@code null}.
 * </p>
 *
 * <p>
 * Класс реализует интерфейс {@link Iterable}, поэтому по всем хранимым узлам
 * можно пройтись с помощью цикла {@code for-each}. При структурных изменениях таблицы
 * (добавление, удаление, очистка) после создания итератора последующие вызовы его
 * методов приводят к выбросу {@link java.util.ConcurrentModificationException}.
 * </p>
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class HashTable<K,V> implements Iterable<Node<K,V>> {
    /**
     * При создании хеш-таблицы пустым конструктором, ей присваивается значение
     * вместимости по умолчанию.
     */
    private static final int DEFAULT_CAPACITY = 16;
    /**
     * При создании хеш-таблицы пустым конструктором, ей присваивается значение
     * коэффициента заполненности по умолчанию.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Массив бакетов, в каждом элементе которого хранится цепочка узлов {@link Node}.
     */
    Node<K,V>[] table;

    /**
     * Текущее количество пар {@code (K, V)} в таблице.
     */
    private int size;

    /**
     * Порог, при достижении которого выполняется ресайз таблицы.
     * <p>
     * Вычисляется как:
     * <blockquote><pre>
     * threshold = (int) (newCapacity * loadFactor);
     * </pre></blockquote>
     * </p>
     */
    private int threshold;

    /**
     * Текущий коэффициент заполнения таблицы. Определяет момент ресайза.
     */
    private float loadFactor;

    /**
     * Счётчик структурных модификаций таблицы.
     * Используется для обнаружения конкурентных модификаций в итераторе.
     */
    private int modCount;


    /**
     * Конструктор по изначальной вместимости и степени загрузки таблицы.
     * При инициализации вместимость таблицы становится равна {@code initialCapacity},
     * а степень загрузки {@code loadFactor}.
     * @param initialCapacity изначальная вместимость таблицы
     * @param loadFactor степень загрузки таблицы
     */
    public HashTable(int initialCapacity, float loadFactor) {
        if(initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if(loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }


    /**
     * При создании таблицы изначальная вместимость приравнивается к стандартному значению {@code 16},
     * а степень загрузки равна стандартному значению {@code 0.75}.
     */
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    /**
     * Возвращает количество пар (K,V) хранящихся в таблице.
     * @return количество пар
     */
    public int getSize(){
        return size;
    }


    /**
     * <p>
     * Добавляет узел в хеш таблицу. Вычисляется хеш узла и вставляется в таблицу на место с соответствующим индексом.
     * Коллизии разрешаются с помощью цепочек. При добавлении возвращает {@code null}.
     * </p>
     * <p>
     * При добавлении узла с ключом, уже существующим в таблице, значение заменяется на новое.
     * После замены значения метод возвращает старое значение типа {@code V} узла.
     * </p>
     * @param key ключ
     * @param value значение
     * @return {@code null} в случае обычной вставки узла. Старое значение {@code value}в случае
     * замены уже существующего значения ключа.
     */
    public V add (K key, V value) {
        int index = hash(key, table.length);

        //Ищем узел с таким же ключом и обновляем значение
        Node<K,V> cur = table[index];
        while(cur != null) {
            if(cur.getKey().equals(key)) {
                V oldValue = cur.getValue();
                cur.setValue(value);

                return oldValue;
            }
            cur = cur.getNext();
        }

        //Если не нашли, то просто вставляем в начало линкед-листа
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        modCount++;

        if (size > threshold) {
            resize(2 * table.length);
        }

        return null;
    }


    /**
     * Возвращает значение по заданному ключу {@code key}. В случае, если нет узла с ключом {@code key}
     * возвращает null.
     * @param key ключ
     * @return значение заданного ключа. {@code null} если нет заданного ключа
     */
    public V get (K key) {
        int index = hash(key, table.length);

        Node<K,V> cur = table[index];
        while(cur != null) {
            if(cur.getKey().equals(key)) {
                return cur.getValue();
            }

            cur = cur.getNext();
        }

        return null;
    }


    /**
     * Удаляет из таблицы пару-узел {@code (K,V)} по заданному ключу. При удалении возвращает значение {@code V}
     * удаленной пары. В случае, если такой пары не существует, возвращает {@code null}.
     * @param key ключ
     * @return значение типа {@code V} удаленной пары. {@code null} в случае отсутствия пары.
     */
    public V remove (K key) {
        int index = hash(key, table.length);

        Node<K,V> cur = table[index];
        Node<K,V> prev = null;

        while(cur != null) {
            if(cur.getKey().equals(key)) {
                //Удаляем из середины/конца цепочки
                if(prev != null) {
                    prev.setNext(cur.getNext());
                }
                //Удаляем из начала цепочки
                else {
                    table[index] = cur.getNext();
                }
                size--;
                modCount++;

                return cur.getValue();
            }
            prev = cur;
            cur = cur.getNext();
        }

        return null;
    }

    /**
     * Проверяет наличия значения по ключу. Возвращает {@code true/false} результат проверки.
     * @param key ключ
     * @return {@code true} если значение есть, {@code false} если значения нет.
     */
    public boolean contains (K key) {
        int index = hash(key, table.length);
        Node<K,V> cur = table[index];
        while(cur != null) {
            if(cur.getKey().equals(key)) {
                return true;
            }

            cur = cur.getNext();
        }

        return false;
    }

    /**
     * Очищает хеш таблицу путем назначения каждому элементу таблицы значения {@code null}.
     */
    public void clear() {
        for(int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        modCount++;
    }


    @Override
    public Iterator<Node<K, V>> iterator() {
        return new HashIterator();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTable<?, ?>)) {
            return false;
        }

        HashTable<?, ?> tmp = (HashTable<?, ?>) o;

        if (this.size != tmp.size) {
            return false;
        }

        @SuppressWarnings("unchecked")
        HashTable<K, ?> other = (HashTable<K, ?>) tmp;

        for (Node<K, V> node : this) {
            K key = node.getKey();
            V value = node.getValue();
            Object otherValue = other.get(key);
            if (!java.util.Objects.equals(value, otherValue)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public int hashCode() {
        int h = 0;
        for (Node<K, V> node : this) {
            K key = node.getKey();
            V value = node.getValue();
            h += Objects.hashCode(key) ^ Objects.hashCode(value);
        }
        return h;
    }



    /**
     * <p>
     * Расширяет хеш-таблицу для увеличения вместимости до {@code newCapacity}.
     * Обычно увеличение происходит путем удваивания размера таблицы.
     * </p>
     * <p>
     * Также пересчитываются значение {@code threshold}.
     *      <blockquote><pre>
     *      threshold = (int) (newCapacity * loadFactor);
     *      </pre></blockquote>
     * </p>
     * @param newCapacity новый размер таблицы
     */
    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for(int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                Node<K, V> next = node.getNext();

                //Считаем индекс в новой таблице и кладем туда узел.
                int newIndex = hash(node.getKey(), newCapacity);
                //Так как на месте newIndex может возникнуть цепочка, помещаем в начало списка новый узел
                node.setNext(newTable[newIndex]);
                newTable[newIndex] = node;

                node = next;
            }
        }

        this.table = newTable;
        this.threshold = (int) (newCapacity * loadFactor);
    }


    @Override
    public String toString() {
        if(size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;
        sb.append("[");
        for(Node<K, V> node : this) {
            if(!isFirst) {
                sb.append(", ");
            }
            else{
                isFirst = false;
            }
            sb.append("(").append(node.getKey()).append(", ").append(node.getValue()).append(")");
        }
        sb.append("]");

        return sb.toString();
    }


    /**
     * Вычисляет хеш по заданному ключу исходя из размеров текущей хеш-таблицы.
     * @param key ключ для которого нужно посчитать хеш
     * @param capacity текущая вместимость таблицы
     * @return {@code hash} вычисленный хеш
     */
    private int hash(K key, int capacity) {
        int h = key.hashCode();
        int hash = h & 0x7fffffff; // избавляемся от знака
        return hash % capacity;
    }


    /**
     *
     */
    private class HashIterator implements Iterator<Node<K, V>> {

        private int bucketIndex = 0;        // Индекс текущего бакета
        private Node<K, V> nextNode;        // Следующая нода для выдачи
        private int expectedModCount;       // Снимок modCount

        HashIterator() {
            this.expectedModCount = modCount;
            advanceToNextNonEmptyBucket();
        }

        private void advanceToNextNonEmptyBucket() {
            while(bucketIndex < table.length && (nextNode = table[bucketIndex]) == null) {
                bucketIndex++;
            }
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public Node<K, V> next() {
            if(modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if(nextNode == null) {
                throw new NoSuchElementException();
            }

            Node<K, V> current = nextNode;

            // сдвигаем nextNode
            if(nextNode.getNext() != null) {
                nextNode = nextNode.getNext();
            }
            else {
                bucketIndex++;
                nextNode = null;
                advanceToNextNonEmptyBucket();
            }

            return current;
        }

    }

}
