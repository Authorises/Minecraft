package dev.authorises.cavelet.utils;

import java.util.*;

public class LootDecider<T> {
    private final List<LootItem<T>> lootItems;
    private double totalPercentage;

    public LootDecider() {
        lootItems = new ArrayList<>();
        totalPercentage = 0.0;
    }

    public LootDecider<T> addChance(double percentageChance, T value) {
        lootItems.add(new LootItem<>(percentageChance, value));
        totalPercentage += percentageChance;
        return this;
    }

    public T decide() throws IllegalStateException {
        if (Math.abs(totalPercentage - 100.0) > 1e-9) {
            throw new IllegalStateException("Percentages do not add up to 100");
        }

        double rand = Math.random() * 100.0;
        double currRange = 0.0;

        for (LootItem<T> item : lootItems) {
            currRange += item.getPercentageChance();
            if (rand < currRange) {
                return item.getValue();
            }
        }

        // Should never reach here, but just in case
        throw new IllegalStateException("No item was chosen");
    }

    private static class LootItem<T> {
        private final double percentageChance;
        private final T value;

        public LootItem(double percentageChance, T value) {
            this.percentageChance = percentageChance;
            this.value = value;
        }

        public double getPercentageChance() {
            return percentageChance;
        }

        public T getValue() {
            return value;
        }
    }
}
