package com.gxzd.gxzd.controller;


import com.gxzd.gxzd.aspect.annotation.RequiresLogin;
import com.gxzd.gxzd.dto.*;
import com.gxzd.gxzd.service.BillNameService;
import com.gxzd.gxzd.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/billName")
@Api(tags = "账单接口")
public class BillNameController {

    @Resource
    private BillNameService billNameService;
    ;

    @ApiOperation("新增账单")
    @PostMapping("/saveBill")
    @RequiresLogin
    public Result saveBill(@Valid @RequestBody SaveBillNameDTO saveBillNameDTO) {
        return billNameService.saveBill(saveBillNameDTO);
    }

    @ApiOperation("修改账单")
    @PostMapping("/updateBill")
    @RequiresLogin
    public Result updateBill(@Valid @RequestBody UpdateBillNameDTO updateBillNameDTO) {
        return billNameService.updateBill(updateBillNameDTO);
    }

    @ApiOperation("删除账单")
    @DeleteMapping("/removeBill")
    @RequiresLogin
    public Result removeBill(@RequestParam("id") Integer id) {
        return billNameService.removeBill(id);
    }

    @ApiOperation("分页查询账单")
    @GetMapping("/getAllBill")
    @RequiresLogin
    public Result getALlBill(@Valid PageDTO pageDTO) {
        return billNameService.getAllBill(pageDTO);
    }

    @ApiOperation("模糊查询账单")
    @GetMapping("/getBillLike")
    @RequiresLogin
    public Result getBillLike(@RequestParam("billName") String billName) {
        return billNameService.getBillLike(billName);
    }

    @ApiOperation("新增共享账本邀请")
    @PostMapping("/saveSharingBill")
    @RequiresLogin
    public Result saveSharingBill(@Valid @RequestBody SaveSharingBillDTO saveSharingBillDTO) {
        return billNameService.saveSharingBill(saveSharingBillDTO);
    }

    @ApiOperation("查询账单详情")
    @GetMapping("/getBillDetails")
    @RequiresLogin
    public Result getBillDetails(@RequestParam("id") Integer id
            , @Valid PageDTO pageDTO) {
        return billNameService.getBillDetails(id, pageDTO);
    }

    @ApiOperation("根据年月获取账单列表")
    @GetMapping("/getBillByYM")
    @RequiresLogin
    public Result getBillByYM(GetBillDTO billDTO, PageDTO pageDTO){
        return billNameService.getBillByYM(billDTO, pageDTO);
    }

    @ApiOperation("根据年获取统计数据")
    @GetMapping("/getAllCountByYear")
    @RequiresLogin
    public Result getAllCountByYear(GetBillYearDTO getBillDTO){
        return billNameService.getAllCountByYear(getBillDTO);
    }

    @ApiOperation("根据月获取统计数据")
    @GetMapping("/getAllCountByMoon")
    @RequiresLogin
    public Result getAllCountByMoon(GetBillMoonDTO getBillDTO){
        return billNameService.getAllCountByMoon(getBillDTO);
    }

}

