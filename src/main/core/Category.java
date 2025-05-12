package main.core;

public enum Category {
        INCOME("Доход"),
        FOOD("Храна"),
        TRANSPORTATION("Транспорт"),
        ENTERTAINMENT("Забавления"),
        HEALTHCARE("Здраве"),
        OTHER("Друго");

        private final String displayName;

        Category(String displayName) {
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}