package com.gxzd.gxzd.service;

import com.gxzd.gxzd.dto.SaveBillRecordDTO;
import com.gxzd.gxzd.dto.UpdateBillRecordDTO;
import com.gxzd.gxzd.entity.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxzd.gxzd.utils.Result;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface BillService extends IService<Bill> {
    /**
     * 新增账单记录
     * @param saveBillRecord
     * @return
     */
    Result saveBillRecord(SaveBillRecordDTO saveBillRecord) throws ParseException;

    /**
     * 修改账单记录
     * @param updateBillRecord
     * @return
     */
    Result updateBillRecord(UpdateBillRecordDTO updateBillRecord);

    /**
     * 删除账单记录
     * @param id
     * @return
     */
    Result removeBillRecord(Integer id);

    /**
     * 获取我的账单列表
     * @return
     */
    Result getMyBillList();


}
