package com.fleetguard.e2e.screenplay.rules;

import java.util.List;

public record RuleData(
        String name,
        String maintenanceType,
        int intervalKm,
        int warningThresholdKm,
        List<String> vehicleTypes
) {}
