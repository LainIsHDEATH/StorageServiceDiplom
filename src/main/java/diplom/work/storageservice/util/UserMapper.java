package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.user.UserRegisterDTO;
import diplom.work.storageservice.dto.user.UserResponseDTO;
import diplom.work.storageservice.dto.user.UserUpdateDTO;
import diplom.work.storageservice.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserRegisterDTO userRegisterDTO);
    User toEntity(UserUpdateDTO userUpdateDTO);
//    User toEntity(UserAuthDTO userAuthDTO);
    UserResponseDTO toDto(User user);
}
