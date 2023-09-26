package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.Log;
import cn.weegoo.sys.service.dto.LogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface LogWrapper extends EntityWrapper <LogDTO, Log> {

    LogWrapper INSTANCE = Mappers.getMapper ( LogWrapper.class );

}
