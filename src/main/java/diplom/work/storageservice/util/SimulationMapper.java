package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.simulation.SimulationDTO;
import diplom.work.storageservice.dto.user.UserRegisterDTO;
import diplom.work.storageservice.dto.user.UserResponseDTO;
import diplom.work.storageservice.dto.user.UserUpdateDTO;
import diplom.work.storageservice.model.simulation.Simulation;
import diplom.work.storageservice.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SimulationMapper {
    @Mapping(target = "id", ignore = true)
    Simulation toEntity(SimulationDTO simulationDTO);

    SimulationDTO toDto(Simulation simulation);
}
