package ru.nsu.munkuev.pizzeria;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Управляет состоянием жизненного цикла пиццерии в течение рабочего дня.
 *
 * <p>Класс хранит информацию о том, принимает ли пиццерия новые заказы,
 * а также отслеживает заказы, которые в данный момент находятся в обработке
 * у пекарей или курьеров.
 *
 * <p>Активными считаются заказы, которые уже были взяты из очереди или со склада,
 * но ещё не были полностью обработаны. Например, заказ может готовиться пекарем
 * или доставляться курьером.
 *
 * <p>Множество активных заказов является потокобезопасным, так как с ним
 * одновременно могут работать несколько потоков пекарей и курьеров.
 */
public class PizzeriaLifecycle {
    private volatile boolean acceptingOrders = true;
    private final Set<Order> activeOrders = ConcurrentHashMap.newKeySet();

    public boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    public void stopAcceptingOrders() {
        this.acceptingOrders = false;
    }

    public void registerActive(Order order) {
        activeOrders.add(order);
    }

    public void registerActiveBatch(Collection<Order> orders) {
        activeOrders.addAll(orders);
    }

    public void unregisterActive(Order order) {
        activeOrders.remove(order);
    }

    public Set<Order> getActiveOrders() {
        return Set.copyOf(activeOrders);
    }

    public boolean workingDay() {
        return true;
    }

}
