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

/**
 * Tests para /services — Registro de Servicios.
 * Orden en el flujo completo: PASO 5-6 (después de km update y espera de alertas).
 */
@ExtendWith(SerenityJUnit5Extension.class)
class RegisterServiceTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldQueryAlertsForExistingVehicle() {
        // Precondición: vehículo recién registrado a 0 km → sin historial de alertas
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData();
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Consultar alertas: vehículo a 0 km → kmRemaining > warningThreshold → SIN ALERTA
        // El sistema muestra el mensaje "No hay alertas activas para este vehículo"
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(vehicle.plate()));

        hans.attemptsTo(
                WaitUntil.the(ServicesForm.NO_ALERTS_MESSAGE, isVisible()).forNoMoreThan(10).seconds()
        );
        hans.should(seeThat(
                actor -> ServicesForm.NO_ALERTS_MESSAGE.resolveFor(actor).isVisible(), is(true)
        ));
    }

    @Test
    void shouldShowFeedbackWhenPlateHasNoHistory() {
        // Placa sin historial → el backend retorna toast de error o mensaje inline
        // Se acepta cualquiera de las dos respuestas: toast o mensaje "no hay alertas"
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate("SINALER01"));

        // Esperar hasta 8s que aparezca el toast (error de backend) o el mensaje inline
        // El backend puede tardar en responder con error 404 para placa inexistente
        try {
            hans.attemptsTo(
                    WaitUntil.the(ServicesForm.NO_ALERTS_MESSAGE, isVisible()).forNoMoreThan(8).seconds()
            );
        } catch (Exception ignored) {
            // Si no aparece el mensaje inline, puede ser toast — también válido
        }

        // Si el backend respondió con error → toast visible
        // Si respondió con vacío → NO_ALERTS_MESSAGE visible
        // En cualquier caso, la página muestra algún feedback
        hans.should(seeThat(
                actor -> {
                    boolean toastVisible = Toast.isVisible().answeredBy(actor);
                    boolean msgVisible;
                    try {
                        msgVisible = ServicesForm.NO_ALERTS_MESSAGE.resolveFor(actor).isVisible();
                    } catch (Exception e) {
                        msgVisible = false;
                    }
                    return toastVisible || msgVisible;
                },
                is(true)
        ));
    }

    @Test
    void shouldRegisterServiceWhenAlertExists() {
        /**
         * Requiere placa "TEST01" con alertas activas pre-cargadas vía seed data.
         * Si "TEST01" no existe en el entorno, este test fallará intencionalmente.
         */
        String plateWithAlert = "TEST01";

        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(plateWithAlert));

        // Verificar que hay alertas disponibles antes de intentar registrar servicio
        hans.attemptsTo(
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(10).seconds()
        );

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
