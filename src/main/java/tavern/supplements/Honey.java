package tavern.supplements;

import tavern.model.Dish;
import tavern.model.Supplement;

public class Honey extends Supplement {
    public Honey(Dish decoratedDish) {
        super(decoratedDish, "медом", 3.0);
    }
}