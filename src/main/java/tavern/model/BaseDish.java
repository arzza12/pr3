package tavern.model;

public class BaseDish implements Dish {
    private final String name;
    private final double price;
    private final String description;

    public BaseDish(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f золотых", name, price);
    }
}