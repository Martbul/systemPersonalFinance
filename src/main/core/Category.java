package main.core;

public enum Category {
        ДОХОД("Доход"),
        ХРАНА("Храна"),
        ТРАНСПОРТ("Транспорт"),
        ЗАБАВЛЕНИЯ("Забавления"),
        ЗДРАВЕ("Здраве"),
        ДРУГО("Друго");

        private final String displayName;

        Category(String displayName) {
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}