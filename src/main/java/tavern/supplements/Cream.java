package tavern.supplements;

import tavern.model.Dish;
import tavern.model.Supplement;

public class Cream extends Supplement {
    public Cream(Dish decoratedDish) {
        super(decoratedDish, "сливками", 4.0);
    }
}