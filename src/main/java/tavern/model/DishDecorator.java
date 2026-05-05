package tavern.model;

public abstract class DishDecorator implements Dish {
    protected Dish decoratedDish;

    public DishDecorator(Dish decoratedDish) {
        this.decoratedDish = decoratedDish;
    }

    @Override
    public String getName() {
        return decoratedDish.getName();
    }

    @Override
    public double getPrice() {
        return decoratedDish.getPrice();
    }

    @Override
    public String getDescription() {
        return decoratedDish.getDescription();
    }
}
