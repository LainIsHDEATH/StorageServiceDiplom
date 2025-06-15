package diplom.work.storageservice.dto.simulation;

import diplom.work.storageservice.model.simulation.Simulation;

public record SimulationDTO(
        Long id,
        Simulation.Status status,
        Simulation.ControllerType controllerType,
        Long iterations,
        Integer timestepSeconds
) {
    public static SimulationDTO fromEntity(Simulation s) {
        return new SimulationDTO(
                s.getId(),
                s.getStatus(),
                s.getControllerType(),
                s.getIterations(),
                s.getTimestepSeconds()
        );
    }

    public Simulation toEntity() {
        Simulation s = new Simulation();
        s.setId(id);
        s.setStatus(status);
        s.setControllerType(controllerType);
        s.setIterations(iterations);
        s.setTimestepSeconds(timestepSeconds);
        return s;
    }
}