package tavern.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int orderCounter = 1;
    private final int orderId;
    private final List<Dish> dishes;
    private final LocalDateTime creationTime;
    private boolean isApproved;

    public Order() {
        this.orderId = orderCounter++;
        this.dishes = new ArrayList<>();
        this.creationTime = LocalDateTime.now();
        this.isApproved = false;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(int index) {
        if (index >= 0 && index < dishes.size()) {
            dishes.remove(index);
        }
    }

    public void modifyDish(int index, Dish newDish) {
        if (index >= 0 && index < dishes.size()) {
            dishes.set(index, newDish);
        }
    }

    public double getTotalPrice() {
        return dishes.stream().mapToDouble(Dish::getPrice).sum();
    }

    public List<Dish> getDishes() {
        return new ArrayList<>(dishes);
    }

    public void approve() {
        this.isApproved = true;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getCreationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return creationTime.format(formatter);
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ЗАКАЗ №").append(orderId).append(" ===\n");
        sb.append("Время: ").append(getCreationTime()).append("\n");
        sb.append("Блюда:\n");

        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            sb.append(String.format("  %d. %s - %.2f золотых\n",
                    i + 1, dish.getDisplayName(), dish.getPrice()));
        }

        sb.append(String.format("\nИТОГО: %.2f золотых\n", getTotalPrice()));
        sb.append("Статус: ").append(isApproved ? "УТВЕРЖДЕН" : "В обработке");

        return sb.toString();
    }
}
