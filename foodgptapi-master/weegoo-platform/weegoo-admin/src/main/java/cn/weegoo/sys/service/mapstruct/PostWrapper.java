package cn.weegoo.sys.service.mapstruct;

import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.sys.domain.Post;
import cn.weegoo.sys.service.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface PostWrapper extends EntityWrapper <PostDTO, Post> {

    PostWrapper INSTANCE = Mappers.getMapper ( PostWrapper.class );
}
