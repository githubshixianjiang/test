package com.gxzd.gxzd.service;

import com.gxzd.gxzd.dto.PageDTO;
import com.gxzd.gxzd.dto.SaveRemindDTO;
import com.gxzd.gxzd.dto.UpdateRemindDTO;
import com.gxzd.gxzd.entity.Remind;
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
public interface RemindService extends IService<Remind> {

    /**
     * 新增提醒
     * @param saveRemindDTO
     * @return
     */
    Result saveRemind(SaveRemindDTO saveRemindDTO) throws ParseException;

    /**
     * 删除提醒
     * @param id
     * @return
     */
    Result removeRemind(Integer id);

    /**
     * 分页获取提醒列表
     * @param pageDTO
     * @return
     */
    Result getAllRemind(PageDTO pageDTO);

    /**
     * 修改提醒
     * @param updateRemindDTO
     * @return
     */
    Result updateRemind(UpdateRemindDTO updateRemindDTO) throws ParseException;

    /**
     * 获取提醒详情
     * @param id
     * @return
     */
    Result getRemind(Integer id);
}
