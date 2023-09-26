package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.TreeWrapper;
import cn.weegoo.sys.domain.Area;
import cn.weegoo.sys.service.dto.AreaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface AreaWrapper extends TreeWrapper <AreaDTO, Area> {

    AreaWrapper INSTANCE = Mappers.getMapper ( AreaWrapper.class );
}
