package de.nichtsroffler.dynamicworld.core;

import com.badlogic.gdx.math.Rectangle;

public class ToSpawnObjectDefinition<T extends DynamicEntity> {

    private final Class<T> blueprint;

    private final Rectangle rectangle;

    public ToSpawnObjectDefinition(Class<T> blueprint, float posXInWorldUnits, float posYInWorldUnits) {
        this.blueprint = blueprint;

        rectangle = new Rectangle(posXInWorldUnits, posYInWorldUnits,0,0);
    }

    public ToSpawnObjectDefinition(Class<T> blueprint, Rectangle rectangle) {
        this.blueprint = blueprint;

        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Class<T> getBlueprint() {
        return blueprint;
    }
}
