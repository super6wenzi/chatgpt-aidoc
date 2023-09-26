/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.database.datamodel.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.database.datamodel.domain.DataMeta;
import cn.weegoo.database.datamodel.mapper.DataMetaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据资源Service
 *
 * @author 刘高峰
 * @version 2021-08-07
 */
@Service
@Transactional
public class DataMetaService extends ServiceImpl <DataMetaMapper, DataMeta> {


}
