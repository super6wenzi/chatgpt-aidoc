package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.Role;
import cn.weegoo.sys.service.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface RoleWrapper extends EntityWrapper <RoleDTO, Role> {

    RoleWrapper INSTANCE = Mappers.getMapper ( RoleWrapper.class );

}
