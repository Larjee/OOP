package ru.nsu.munkuev.pizzeria;

public enum OrderState {
    QUEUED("в очереди"),
    BAKING("готовится"),
    WAITING_FOR_STORAGE("ожидает место на складе"),
    STORED("на складе"),
    DELIVERING("доставляется"),
    DELIVERED("доставлен"),
    SUSPENDED("сериализован на следующий день");

    private final String label;

    OrderState(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
