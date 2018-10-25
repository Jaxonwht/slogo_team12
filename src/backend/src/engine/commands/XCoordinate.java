package engine.commands;

import model.impl.TurtleModelImpl;

public class XCoordinate implements Command<TurtleModelImpl> {
    public XCoordinate (){}
    @Override
    public double update(TurtleModelImpl turtleModel) {
        return turtleModel.getX();
    }
}
