package com.gxzd.gxzd.controller;


import com.gxzd.gxzd.aspect.annotation.RequiresLogin;
import com.gxzd.gxzd.dto.PageDTO;
import com.gxzd.gxzd.dto.SaveRemindDTO;
import com.gxzd.gxzd.dto.UpdateRemindDTO;
import com.gxzd.gxzd.service.RemindService;
import com.gxzd.gxzd.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/remind")
@Api(tags = "提醒接口")
public class RemindController {

    @Resource
    private RemindService remmindService;

    @ApiOperation("新增提醒")
    @PostMapping("/saveRemind")
    @RequiresLogin
    public Result saveRemind(@Valid @RequestBody SaveRemindDTO saveRemindDTO) throws ParseException {
        return remmindService.saveRemind(saveRemindDTO);
    }

    @ApiOperation("修改提醒")
    @PostMapping("/updateRemind")
    @RequiresLogin
    public Result updateRemind(@Valid @RequestBody UpdateRemindDTO updateRemindDTO) throws ParseException {
        return remmindService.updateRemind(updateRemindDTO);
    }

    @ApiOperation("删除提醒")
    @DeleteMapping("/removeRemind")
    @RequiresLogin
    public Result removeRemind(@RequestParam("id") Integer id){
        return remmindService.removeRemind(id);
    }

    @ApiOperation("分页获取提醒列表")
    @GetMapping("/getAllRemind")
    @RequiresLogin
    public Result getAllRemind(PageDTO pageDTO){
        return remmindService.getAllRemind(pageDTO);
    }

    @ApiOperation("查询提醒详情")
    @GetMapping("/getRemind")
    @RequiresLogin
    public Result getRemind(@RequestParam("id") Integer id){
        return remmindService.getRemind(id);
    }

}

