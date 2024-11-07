package ru.yandex.practicum.catsgram.model;

public enum SortOrder {
    ASCENDING, DESCENDING;

    public static SortOrder from(String order) {
        if (order == null) {
            return DESCENDING;
        }
        return switch (order.toLowerCase()) {
            case "asc", "ascending" -> ASCENDING;
            default -> DESCENDING;
        };
    }
}
