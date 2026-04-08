package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.mileage.UpdateMileage;
import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.rules.CreateMaintenanceRule;
import com.fleetguard.e2e.screenplay.rules.RuleData;
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
 * Test de ciclo completo de mantenimiento de FleetGuard.
 * Requiere que el backend (fleet-service + rules-alerts-service + RabbitMQ) esté levantado.
 * Ejecutar: docker-compose up antes de correr este test.
 */
@ExtendWith(SerenityJUnit5Extension.class)
class FullFlowTest {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldCompleteFullMaintenanceLifecycle() throws InterruptedException {

        // PASO 1: Crear regla de mantenimiento para tipo Sedán
        RuleData rule = TestDataGenerator.uniqueRuleData("Sedán");
        hans.attemptsTo(NavigateTo.theRulesPage());
        hans.attemptsTo(CreateMaintenanceRule.with(rule));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // PASO 2: Registrar vehículo Sedán
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData("Sedán");
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // PASO 3: Registrar km alto que dispare la alerta
        // La regla tiene intervalKm=10000; registrar 12000 supera el umbral
        MileageData mileageData = TestDataGenerator.mileageFor(vehicle.plate(), 12000L);
        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(mileageData));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // PASO 4: Esperar procesamiento asíncrono RabbitMQ (3 segundos)
        // EXCEPCIÓN a la regla de no usar Thread.sleep() — necesario para mensajería async
        Thread.sleep(3000);

        // PASO 5: Ir a /services y consultar alertas
        hans.attemptsTo(NavigateTo.theServicesPage());
        hans.attemptsTo(QueryAlerts.forPlate(vehicle.plate()));

        // PASO 6: Verificar que aparece la alerta generada
        hans.attemptsTo(
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(15).seconds()
        );
        hans.should(seeThat(
                actor -> ServicesForm.FIRST_ALERT.resolveFor(actor).isVisible(), is(true)
        ));

        // PASO 7: Llenar formulario de servicio y registrar
        ServiceData serviceData = new ServiceData(
                vehicle.plate(),
                "Técnico E2E Full Flow",
                TestDataGenerator.today(),
                "Taller FleetGuard E2E",
                "200.00",
                "Mantenimiento preventivo E2E – ciclo completo",
                "12000"
        );
        hans.attemptsTo(RegisterService.with(serviceData));

        // PASO 8: Verificar toast de éxito
        hans.attemptsTo(WaitForToast.toAppear());
        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
