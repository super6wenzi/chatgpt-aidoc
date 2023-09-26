package cn.weegoo.database.datamodel.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.database.datamodel.domain.DataMeta;
import cn.weegoo.database.datamodel.service.dto.DataMetaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DataMetaWrapper extends EntityWrapper <DataMetaDTO, DataMeta> {

    DataMetaWrapper INSTANCE = Mappers.getMapper ( DataMetaWrapper.class );

    @Mappings({
            @Mapping(source = "dataSetDTO.id", target = "dataSetId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping(source = "updateBy.id", target = "updateBy")})
    DataMeta toEntity(DataMetaDTO dto);


    @Mappings({
            @Mapping(source = "dataSetId", target = "dataSetDTO.id"),
            @Mapping(source = "createBy", target = "createBy.id"),
            @Mapping(source = "updateBy", target = "updateBy.id")})
    DataMetaDTO toDTO(DataMeta entity);

}
