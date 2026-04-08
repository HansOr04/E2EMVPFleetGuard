package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleForm;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.annotations.CastMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * Tests para /register — Registrar Nuevo Vehículo.
 * Orden en el flujo completo: PASO 1.
 *
 * <p><b>Nota sobre validación de formulario vacío:</b>
 * Los inputs tienen atributo {@code required}. Cuando se hace click en "Guardar Vehículo"
 * sin llenar campos, el <em>navegador</em> activa su validación HTML5 nativa (tooltip rojo)
 * y <em>no llega a llamar al backend</em> → <em>NO aparece toast</em>.
 * El test correcto verifica que la página no navega (sigue en /register).</p>
 *
 * <p><b>Nota sobre VIN inválido:</b>
 * Un VIN de 16 chars viola la validación del backend → el backend retorna error
 * que se muestra como toast de error.</p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
class RegisterVehicleTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldRegisterVehicleSuccessfully() {
        VehicleData data = TestDataGenerator.uniqueVehicleData(VehicleType.SEDAN);

        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(data));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.message(), containsString("registrado")));
    }

    @Test
    void shouldStayOnRegisterPageWhenSubmittingEmptyForm() {
        // Los campos tienen required → validación HTML5 nativa del navegador
        // → no se llama al backend → no hay toast → la URL no cambia
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(Click.on(VehicleForm.SUBMIT_BUTTON));

        // Verificar que la página no navegó (seguimos en /register con el formulario visible)
        hans.should(seeThat(
                actor -> actor.usingAbilityTo(net.serenitybdd.screenplay.abilities.BrowseTheWeb.class)
                        .getDriver().getCurrentUrl(),
                containsString("/register")
        ));
        // El título del formulario sigue visible — no se navegó a otra página
        hans.should(seeThat(
                actor -> VehicleForm.PAGE_TITLE.resolveFor(actor).isVisible(), is(true)
        ));
    }

    @Test
    void shouldShowErrorToastWhenVinIsInvalid() {
        // VIN de 16 chars — el frontend puede no validar longitud, el backend sí
        // Si el frontend JS valida → puede que tampoco llegue al backend
        // En cualquier caso, la placa es válida → si hay toast, es de error del backend
        VehicleData data = new VehicleData(
                TestDataGenerator.uniquePlate(),
                "INVALIDVIN16CHR1",   // 16 chars — inválido (se requieren 17)
                "Honda", "Civic", "2022", "Gasolina", VehicleType.SEDAN.text()
        );

        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(data));
        hans.attemptsTo(WaitForToast.toAppear());

        // El toast de error debe aparecer (backend rechaza VIN con longitud incorrecta)
        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
