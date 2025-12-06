package ru.nsu.munkuev;


/**
 * Узел хранимый в хеш-таблице {@link HashTable}.
 * Представляет собой элемент односвязного списка в бакете и содержит пару {@code (key, value)}.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class Node<K, V> {
    private final K key;
    private V value;
    private Node<K, V> next;

    public Node(K key, V value, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public Node<K, V> getNext() {
        return next;
    }
}
