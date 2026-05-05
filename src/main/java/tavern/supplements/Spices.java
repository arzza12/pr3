package tavern.supplements;

import tavern.model.Dish;
import tavern.model.Supplement;

public class Spices extends Supplement {
    public Spices(Dish decoratedDish) {
        super(decoratedDish, "специями", 5.0);
    }
}