package com.kirichko.kirichkorouteplanner.model.datakeepers;

import com.kirichko.kirichkorouteplanner.model.datastructures.Route;

/**
 * Created by Киричко on 26.03.2016.
 */
public class ChosenRoute {
    private static Route route;

    public ChosenRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
