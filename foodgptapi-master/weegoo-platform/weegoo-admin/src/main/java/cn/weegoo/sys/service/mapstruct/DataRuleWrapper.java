package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.DataRule;
import cn.weegoo.sys.service.dto.DataRuleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DataRuleWrapper extends EntityWrapper <DataRuleDTO, DataRule> {

    DataRuleWrapper INSTANCE = Mappers.getMapper ( DataRuleWrapper.class );
}
