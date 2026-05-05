package tavern.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Supplement extends DishDecorator {
    private final String supplementName;
    private final double supplementPrice;

    public Supplement(Dish decoratedDish, String supplementName, double supplementPrice) {
        super(decoratedDish);
        if (countSupplements(decoratedDish) >= 3) {
            throw new IllegalArgumentException("Превышен лимит: к одному блюду можно добавить не более 3 добавок!");
        }
        this.supplementName = supplementName;
        this.supplementPrice = supplementPrice;
    }

    private static int countSupplements(Dish dish) {
        int count = 0;
        Dish current = dish;
        while (current instanceof Supplement) {
            count++;
            current = ((Supplement) current).decoratedDish;
        }
        return count;
    }

    @Override
    public String getName() {
        return decoratedDish.getName() + " с " + supplementName;
    }

    @Override
    public double getPrice() {
        return decoratedDish.getPrice() + supplementPrice;
    }

    @Override
    public String getDescription() {
        return decoratedDish.getDescription() + " (+ " + supplementName + ")";
    }

    @Override
    public String getDisplayName() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Dish current = this;

        while (current instanceof Supplement) {
            Supplement s = (Supplement) current;
            counts.merge(s.supplementName, 1, Integer::sum);
            current = s.decoratedDish;
        }

        String baseName = current.getName();
        StringBuilder sb = new StringBuilder(baseName);

        List<String> orderedSupplements = new ArrayList<>(counts.keySet());
        Collections.reverse(orderedSupplements);

        for (String name : orderedSupplements) {
            int count = counts.get(name);
            sb.append(" с ").append(name);
            if (count > 1) {
                sb.append("*").append(count);
            }
        }

        return sb.toString();
    }
}