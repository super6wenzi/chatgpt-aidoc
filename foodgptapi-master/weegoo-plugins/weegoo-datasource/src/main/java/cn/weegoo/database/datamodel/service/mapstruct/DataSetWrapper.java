package cn.weegoo.database.datamodel.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.database.datamodel.domain.DataSet;
import cn.weegoo.database.datamodel.service.dto.DataSetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DataSetWrapper extends EntityWrapper <DataSetDTO, DataSet> {

    DataSetWrapper INSTANCE = Mappers.getMapper ( DataSetWrapper.class );

    @Mappings({
            @Mapping(source = "dataSource.id", target = "dataSourceId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping(source = "updateBy.id", target = "updateBy")})
    DataSet toEntity(DataSetDTO dto);


    @Mappings({
            @Mapping(source = "dataSourceId", target = "dataSource.id"),
            @Mapping(source = "createBy", target = "createBy.id"),
            @Mapping(source = "updateBy", target = "updateBy.id")})
    DataSetDTO toDTO(DataSet entity);

}
