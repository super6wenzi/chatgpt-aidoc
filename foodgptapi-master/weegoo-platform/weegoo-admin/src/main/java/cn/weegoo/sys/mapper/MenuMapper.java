/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import cn.weegoo.core.domain.TreeMapper;
import cn.weegoo.sys.domain.Menu;
import cn.weegoo.sys.service.dto.MenuDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单MAPPER接口
 *
 * @author weegoo
 * @version 2021-05-16
 */
public interface MenuMapper extends TreeMapper<Menu> {

    List <MenuDTO> findByUserId(MenuDTO menuDTO);

    void deleteMenuRole(@Param("menuId") String menuId);

    List <MenuDTO> findAllWithDataRuleList();

    List <String> mrList(@Param(Constants.WRAPPER) Wrapper wrapper);

    List <String> mdList(@Param(Constants.WRAPPER) Wrapper wrapper);

}
