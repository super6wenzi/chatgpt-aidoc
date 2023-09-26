package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.DictType;
import cn.weegoo.sys.service.dto.DictTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DictTypeWrapper extends EntityWrapper <DictTypeDTO, DictType> {

    DictTypeWrapper INSTANCE = Mappers.getMapper ( DictTypeWrapper.class );
}
