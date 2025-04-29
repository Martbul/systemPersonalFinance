package main.core;

public enum Category {
        INCOME("Income"),
        FOOD("Food"),
        UTILITIES("Utilities"),
        TRANSPORTATION("Transportation"),
        ENTERTAINMENT("Entertainment"),
        HEALTHCARE("Healthcare"),
        OTHER("Other");

        private final String displayName;

        Category(String displayName) {
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}