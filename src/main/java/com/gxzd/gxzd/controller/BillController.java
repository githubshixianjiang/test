package com.gxzd.gxzd.controller;


import com.gxzd.gxzd.aspect.annotation.RequiresLogin;
import com.gxzd.gxzd.dto.SaveBillRecordDTO;
import com.gxzd.gxzd.dto.UpdateBillRecordDTO;
import com.gxzd.gxzd.service.BillService;
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
@RequestMapping("/bill")
@Api(tags = "账单记录接口")
public class BillController {

    @Resource
    private BillService billService;

    @ApiOperation("新增账单记录")
    @PostMapping("/saveBillRecord")
    @RequiresLogin
    public Result saveBillRecord(@Valid @RequestBody SaveBillRecordDTO saveBillRecord) throws ParseException {
        return billService.saveBillRecord(saveBillRecord);
    }

    @ApiOperation("修改账单记录")
    @PostMapping("/updateBillRecord")
    @RequiresLogin
    public Result updateBillRecord(@Valid @RequestBody UpdateBillRecordDTO updateBillRecord){
        return billService.updateBillRecord(updateBillRecord);
    }

    @ApiOperation("删除账单记录")
    @DeleteMapping("/removeBillRecord")
    @RequiresLogin
    public Result removeBillRecord(@RequestParam("id") Integer id){
        return billService.removeBillRecord(id);
    }

    @ApiOperation("获取我的账单列表")
    @GetMapping("/getMyBillList")
    @RequiresLogin
    public Result getMyBillList(){
        return billService.getMyBillList();
    }

}

