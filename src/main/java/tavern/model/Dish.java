package tavern.model;

public interface Dish {
    String getName();
    double getPrice();
    String getDescription();

    default String getDisplayName() {
        return getName();
    }
}