/**
 * Copyright © 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.sys.domain.Post;
import cn.weegoo.sys.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 岗位Service
 *
 * @author 刘高峰
 * @version 2021-08-17
 */
@Service
@Transactional
public class PostService extends ServiceImpl <PostMapper, Post> {

}
