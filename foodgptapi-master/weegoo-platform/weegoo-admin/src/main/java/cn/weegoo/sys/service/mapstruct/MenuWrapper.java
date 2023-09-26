package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.TreeWrapper;
import cn.weegoo.sys.domain.Menu;
import cn.weegoo.sys.service.dto.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface MenuWrapper extends TreeWrapper <MenuDTO, Menu> {

    MenuWrapper INSTANCE = Mappers.getMapper ( MenuWrapper.class );
}
