package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.navigation.SidebarLinks;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.annotations.CastMember;
import net.serenitybdd.screenplay.questions.WebAdaptorQuestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(SerenityJUnit5Extension.class)
class NavigationTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldNavigateThroughAllSidebarLinks() {
        hans.attemptsTo(NavigateTo.theDashboard());

        // Link: Registrar Vehículo → /register
        hans.attemptsTo(Click.on(SidebarLinks.REGISTER_VEHICLE));
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("/register")
        ));

        // Link: Reglas de Mantenimiento → /rules
        hans.attemptsTo(Click.on(SidebarLinks.RULES));
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("/rules")
        ));

        // Link: Actualizar Kilometraje → /mileage
        hans.attemptsTo(Click.on(SidebarLinks.MILEAGE));
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("/mileage")
        ));

        // Link: Registro de Servicios → /services
        hans.attemptsTo(Click.on(SidebarLinks.SERVICES));
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("/services")
        ));

        // Link: Home → /
        hans.attemptsTo(Click.on(SidebarLinks.HOME));
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("localhost:3000")
        ));
    }
}
