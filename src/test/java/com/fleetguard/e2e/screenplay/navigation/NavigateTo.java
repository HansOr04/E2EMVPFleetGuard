package com.fleetguard.e2e.screenplay.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

public class NavigateTo {

    private static final String BASE = "http://localhost:3000";

    public static Performable theDashboard() {
        return Task.where("{0} navigates to the Dashboard",
                Open.url(BASE + "/"));
    }

    public static Performable theRegisterPage() {
        return Task.where("{0} navigates to Register Vehicle page",
                Open.url(BASE + "/register"));
    }

    public static Performable theMileagePage() {
        return Task.where("{0} navigates to Update Mileage page",
                Open.url(BASE + "/mileage"));
    }

    public static Performable theRulesPage() {
        return Task.where("{0} navigates to Maintenance Rules page",
                Open.url(BASE + "/rules"));
    }

    public static Performable theServicesPage() {
        return Task.where("{0} navigates to Service Registration page",
                Open.url(BASE + "/services"));
    }
}
