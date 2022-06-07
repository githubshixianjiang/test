package com.gxzd.gxzd.service;

import com.gxzd.gxzd.dto.*;
import com.gxzd.gxzd.entity.BillName;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxzd.gxzd.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface BillNameService extends IService<BillName> {
    /**
     * 新增账单
     * @param saveBillNameDTO
     * @return
     */
    Result saveBill(SaveBillNameDTO saveBillNameDTO);

    /**
     * 修改账单
     * @param updateBillNameDTO
     * @return
     */
    Result updateBill(UpdateBillNameDTO updateBillNameDTO);

    /**
     * 删除账单
     * @param id
     * @return
     */
    Result removeBill(Integer id);

    /**
     * 分页获取账单
     * @param pageDTO
     * @return
     */
    Result getAllBill(PageDTO pageDTO);

    /**
     * 模糊查询账单
     * @param billName
     * @return
     */
    Result getBillLike(String billName);

    /**
     * 新增共享账本邀请
     * @param saveSharingBillDTO
     * @return
     */
    Result saveSharingBill(SaveSharingBillDTO saveSharingBillDTO);

    /**
     * 获取账单详情
     * @param id
     * @param pageDTO
     * @return
     */
    Result getBillDetails(Integer id, PageDTO pageDTO);

    /**
     * 根据年月获取账单列表
     * @param billDTO
     * @param pageDTO
     * @return
     */
    Result getBillByYM(GetBillDTO billDTO, PageDTO pageDTO);

    /**
     * 根据年获取统计数据
     * @param getBillDTO
     * @return
     */
    Result getAllCountByYear(GetBillYearDTO getBillDTO);

    /**
     *
     * @param getBillDTO
     * @return
     */
    Result getAllCountByMoon(GetBillMoonDTO getBillDTO);
}
