package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.mileage.UpdateMileage;
import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.rules.CreateMaintenanceRule;
import com.fleetguard.e2e.screenplay.rules.MaintenanceRuleType;
import com.fleetguard.e2e.screenplay.services.QueryAlerts;
import com.fleetguard.e2e.screenplay.services.RegisterService;
import com.fleetguard.e2e.screenplay.services.ServiceData;
import com.fleetguard.e2e.screenplay.services.ServicesForm;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;
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
 */
@ExtendWith(SerenityJUnit5Extension.class)
class RegisterServiceTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldQueryAlertsForExistingVehicle() {
        // Vehículo recién registrado a 0 km → sin historial → "No hay alertas activas"
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData();
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

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
        // Placa sin historial → backend retorna error (toast) o mensaje inline
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate("SINALER01"));

        try {
            hans.attemptsTo(
                    WaitUntil.the(ServicesForm.NO_ALERTS_MESSAGE, isVisible()).forNoMoreThan(8).seconds()
            );
        } catch (Exception ignored) {
            // Si no aparece mensaje inline, puede ser toast de error — también válido
        }

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
    void shouldRegisterServiceWhenAlertExists() throws InterruptedException {
        // Setup completo inline: vehículo → regla → km en rango PENDING → alerta → servicio
        // ACEITE_MOTOR_LIVIANO: intervalKm=5000, warningThresholdKm=500
        // Registrar 4750 km → dueAtKm=5000, kmRemaining=250 → PENDING ✓

        // Paso 1: Registrar vehículo
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData(VehicleType.SEDAN);
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Paso 2: Crear regla de mantenimiento
        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(CreateMaintenanceRule.with(
                TestDataGenerator.ruleFor(MaintenanceRuleType.ACEITE_MOTOR_LIVIANO, VehicleType.SEDAN)
        ));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Paso 3: Actualizar km en zona PENDING (4750 km para intervalKm=5000)
        long pendingKm = 5000L - 500L / 2; // = 4750
        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(TestDataGenerator.mileageFor(vehicle.plate(), pendingKm)));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Paso 4: Esperar procesamiento asíncrono RabbitMQ
        Thread.sleep(5000);

        // Paso 5: Consultar alertas y verificar que hay al menos una
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(vehicle.plate()));
        hans.attemptsTo(
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(15).seconds()
        );

        // Paso 6: Registrar servicio
        ServiceData serviceData = new ServiceData(
                vehicle.plate(),
                "Técnico E2E",
                TestDataGenerator.today(),
                "Taller E2E",
                "150.00",
                "Mantenimiento preventivo automatizado",
                String.valueOf(pendingKm)
        );
        hans.attemptsTo(RegisterService.with(serviceData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
