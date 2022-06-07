package com.gxzd.gxzd.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzd.gxzd.entity.BillName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxzd.gxzd.vo.BillNameVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
public interface BillNameMapper extends BaseMapper<BillName> {
    /**
     * 分享查询账单
     *
     * @param page
     * @param userId
     * @return
     */
    Page<BillNameVO> getAllBill(Page<BillNameVO> page,
                                @Param("endIndexId") Integer endIndexId,
                                @Param("userId") Integer userId);
}
