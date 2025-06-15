package diplom.work.storageservice.dto.sensor_data;

public record MetricsDTO(
        double mae,     // Mean-Absolute-Error (°C)
        double mse,     // Mean-Squared-Error (°C²)
        double rmse,    // Root-MSE (°C)
        double energyKWh, // кВт·ч
        double overshoot, // максимальне перерегулювання, °C
        double settlingTimeS   // cекунди (-1 якщо система не вклалась у ±ε)
) {}
