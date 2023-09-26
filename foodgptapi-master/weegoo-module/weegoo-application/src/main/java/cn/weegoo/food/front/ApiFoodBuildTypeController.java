package cn.weegoo.food.front;

import cn.weegoo.food.front.vo.BuildTypeVO;
import cn.weegoo.food.front.vo.ContentTypeVO;
import cn.weegoo.food.front.vo.FormVO;
import cn.weegoo.food.service.FoodBuildTypeService;
import cn.weegoo.food.service.FoodFormService;
import cn.weegoo.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "生成分类")
@RequestMapping("/api/foodBuildType")
public class ApiFoodBuildTypeController {

    @Autowired
    private FoodBuildTypeService foodBuildTypeService;
    @Autowired
    private FoodFormService foodFormService;

    @ApiOperation("获取列表")
    @GetMapping("getList")
    public R<List<ContentTypeVO>> getList(){
        return R.data(foodBuildTypeService.getList());
    }

    @ApiOperation("获取品牌应填写的表单")
    @GetMapping("getForm")
    public R<List<FormVO>> getForm(String buildTypeId){
        return R.data(foodFormService.getByBuildTypeId(buildTypeId));
    }
}
