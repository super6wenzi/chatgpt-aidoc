package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.SysConfig;
import cn.weegoo.sys.domain.vo.SysConfigVo;
import cn.weegoo.sys.service.dto.SysConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface SysConfigWrapper extends EntityWrapper <SysConfigDTO, SysConfig> {

    SysConfigWrapper INSTANCE = Mappers.getMapper ( SysConfigWrapper.class );

    SysConfigVo toVo(SysConfig sysConfig);

}
