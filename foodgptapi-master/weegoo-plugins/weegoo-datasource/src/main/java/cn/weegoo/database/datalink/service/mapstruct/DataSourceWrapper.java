package cn.weegoo.database.datalink.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.database.datalink.domain.DataSource;
import cn.weegoo.database.datalink.service.dto.DataSourceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DataSourceWrapper extends EntityWrapper <DataSourceDTO, DataSource> {

    DataSourceWrapper INSTANCE = Mappers.getMapper ( DataSourceWrapper.class );

    @Mappings({
            @Mapping(source = "pollName", target = "enName"),
            @Mapping(source = "driverClassName", target = "driver")})
    DataSource toEntity(DataSourceDTO dto);


    @Mappings({
            @Mapping(source = "enName", target = "pollName"),
            @Mapping(source = "driver", target = "driverClassName")})
    DataSourceDTO toDTO(DataSource entity);
}
