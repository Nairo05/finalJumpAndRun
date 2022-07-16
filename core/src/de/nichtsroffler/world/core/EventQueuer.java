package de.nichtsroffler.world.core;

import com.badlogic.gdx.utils.Array;

import de.nichtsroffler.world.InteractiveTileObject;

public class EventQueuer {

    private Array<Event> events;

    public EventQueuer() {
        events = new Array<>();
    }

    public void update(float dt) {
        for (int i = 0; i < events.size; i++) {
            events.get(i).update(dt);

            if (events.get(i).happened()) {
                events.removeIndex(i);
            }
        }
    }

    public void queueEvent(InteractiveTileObject interactiveTileObject, int counter) {
        events.add(new Event(interactiveTileObject, counter));
    }
}
