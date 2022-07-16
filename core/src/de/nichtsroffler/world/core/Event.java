package de.nichtsroffler.world.core;

import de.nichtsroffler.world.InteractiveTileObject;

public class Event {

    private InteractiveTileObject interactiveTileObject;
    private int counter;

    public Event(InteractiveTileObject interactiveTileObject, int counter) {
        this.interactiveTileObject = interactiveTileObject;
        this.counter = counter;
    }

    public void update(float dt) {
        counter--;
        if (counter <= 0) {
            interactiveTileObject.onEvent();
        }
    }

    public boolean happened() {
        return counter <= 0;
    }
}
