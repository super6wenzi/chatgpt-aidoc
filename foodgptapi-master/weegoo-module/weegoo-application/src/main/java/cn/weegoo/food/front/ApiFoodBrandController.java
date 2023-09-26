package cn.weegoo.food.front;

import cn.weegoo.food.front.dto.BrandDTO;
import cn.weegoo.food.front.vo.BrandVO;
import cn.weegoo.food.service.FoodBrandService;
import cn.weegoo.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author damin
 */
@Api(tags = "品牌")
@RestController
@RequestMapping("/api/foodBrand")
public class ApiFoodBrandController {
    @Autowired
    private FoodBrandService foodBrandService;

    @ApiOperation("获取列表")
    @GetMapping("getList")
    public R<Page<BrandVO>> getList(int current, int size){
        return R.data(foodBrandService.getList(current,size));
    }

    @ApiOperation("添加品牌")
    @PostMapping("addBrand")
    public R<String> addBrand(@RequestBody BrandDTO brandDTO){
        return R.data(foodBrandService.addOrSaveBrand(brandDTO));
    }
    @ApiOperation("品牌详情")
    @GetMapping("getInfoById")
    public R<BrandVO> getInfoById(String id){
        return R.data(foodBrandService.getInfo(id));
    }
}
