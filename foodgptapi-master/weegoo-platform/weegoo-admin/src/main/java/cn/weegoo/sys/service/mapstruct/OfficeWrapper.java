package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.TreeWrapper;
import cn.weegoo.sys.domain.Office;
import cn.weegoo.sys.service.dto.OfficeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface OfficeWrapper extends TreeWrapper <OfficeDTO, Office> {

    OfficeWrapper INSTANCE = Mappers.getMapper ( OfficeWrapper.class );

    /**
     * dto对象转化成entity对象
     */
    @Mappings({
            @Mapping(source = "areaDTO.id", target = "areaId"),
            @Mapping(source = "parent.id", target = "parentId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping(source = "updateBy.id", target = "updateBy")})
    Office toEntity(OfficeDTO dto);

    /**
     * entity对象转换成dto对象
     */
    @Mappings({
            @Mapping(source = "areaId", target = "areaDTO.id"),
            @Mapping(source = "parentId", target = "parent.id"),
            @Mapping(source = "createBy", target = "createBy.id"),
            @Mapping(source = "updateBy", target = "updateBy.id")})
    OfficeDTO toDTO(Office entity);

}
