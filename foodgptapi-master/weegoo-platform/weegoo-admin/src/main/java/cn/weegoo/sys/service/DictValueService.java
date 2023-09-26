/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.sys.domain.DictValue;
import cn.weegoo.sys.mapper.DictValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据字典Service
 *
 * @author lgf
 * @version 2021-01-16
 */
@Service
@Transactional
public class DictValueService extends ServiceImpl <DictValueMapper, DictValue> {

}

