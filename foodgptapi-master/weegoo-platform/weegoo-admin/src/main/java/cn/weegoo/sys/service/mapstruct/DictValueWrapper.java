package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.DictValue;
import cn.weegoo.sys.service.dto.DictValueDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DictValueWrapper extends EntityWrapper <DictValueDTO, DictValue> {

    DictValueWrapper INSTANCE = Mappers.getMapper ( DictValueWrapper.class );
}
