package com.gxzd.gxzd.mapper;

import com.gxzd.gxzd.entity.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface BillMapper extends BaseMapper<Bill> {
    /**
     * 统计收入
     * @param id
     * @param i
     * @return
     */
    Integer sumPrice(@Param("billNameId") Integer id,
                 @Param("status") int status);

    Integer typePrice(@Param("billNameId")Integer billNameId,
                      @Param("status")int status,
                      @Param("type") int type);

    /**
     * 统计记录条数
     * @param billNameId
     * @return
     */
    int countByBill(Integer billNameId);

}
