/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.weegoo.sys.domain.DataRule;
import cn.weegoo.sys.service.dto.DataRuleDTO;
import cn.weegoo.sys.service.dto.UserDTO;

import java.util.List;

/**
 * 数据权限MAPPER接口
 *
 * @author lgf
 * @version 2021-04-02
 */
public interface DataRuleMapper extends BaseMapper <DataRule> {

    void deleteRoleDataRule(String dataRuleId);

    @InterceptorIgnore
    List <DataRuleDTO> findByUserId(UserDTO userDTO);
}
