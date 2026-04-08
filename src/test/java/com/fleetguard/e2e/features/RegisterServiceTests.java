package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.services.QueryAlerts;
import com.fleetguard.e2e.screenplay.services.RegisterService;
import com.fleetguard.e2e.screenplay.services.ServiceData;
import com.fleetguard.e2e.screenplay.services.ServicesForm;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.annotations.CastMember;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static org.hamcrest.Matchers.is;

@ExtendWith(SerenityJUnit5Extension.class)
class RegisterServiceTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldQueryAlertsForExistingVehicle() {
        // Precondición: registrar vehículo (recién creado → sin alertas)
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData();
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Ir a /services y consultar alertas (Paso 1)
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(vehicle.plate()));

        // Sin km registrados el sistema responde con "no hay alertas"
        hans.attemptsTo(
                WaitUntil.the(ServicesForm.NO_ALERTS_MESSAGE, isVisible()).forNoMoreThan(10).seconds()
        );
        hans.should(seeThat(
                actor -> ServicesForm.NO_ALERTS_MESSAGE.resolveFor(actor).isVisible(), is(true)
        ));
    }

    @Test
    void shouldShowMessageWhenVehicleHasNoAlerts() {
        hans.attemptsTo(NavigateTo.theServicesPage());
        // Placa que no existe en el sistema
        hans.attemptsTo(QueryAlerts.forPlate("SINALER01"));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }

    @Test
    void shouldRegisterServiceWhenAlertExists() {
        /**
         * Este test requiere una placa con alertas activas en el entorno.
         * En CI se debe usar un fixture/seed de datos con un vehículo que tenga alertas.
         * La placa "TEST01" debe pre-cargarse vía docker-compose seed data.
         */
        String plateWithAlert = "TEST01";

        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(plateWithAlert));

        ServiceData serviceData = new ServiceData(
                plateWithAlert,
                "Técnico E2E",
                TestDataGenerator.today(),
                "Taller E2E",
                "150.00",
                "Mantenimiento preventivo automatizado",
                "50000"
        );

        hans.attemptsTo(RegisterService.with(serviceData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
