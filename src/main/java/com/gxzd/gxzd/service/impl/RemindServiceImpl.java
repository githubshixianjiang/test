package com.gxzd.gxzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzd.gxzd.constant.DataBasesOperationResult;
import com.gxzd.gxzd.dto.PageDTO;
import com.gxzd.gxzd.dto.SaveRemindDTO;
import com.gxzd.gxzd.dto.UpdateRemindDTO;
import com.gxzd.gxzd.entity.Remind;
import com.gxzd.gxzd.entity.UserCache;
import com.gxzd.gxzd.enums.RemindEnum;
import com.gxzd.gxzd.enums.UserEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import com.gxzd.gxzd.mapper.RemindMapper;
import com.gxzd.gxzd.service.RemindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxzd.gxzd.utils.PageUtils;
import com.gxzd.gxzd.utils.RedisUtils;
import com.gxzd.gxzd.utils.Result;
import com.gxzd.gxzd.vo.RemindDetailsVO;
import com.gxzd.gxzd.vo.RemindVO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Service
@Slf4j
public class RemindServiceImpl extends ServiceImpl<RemindMapper, Remind> implements RemindService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;



    /**
     * 新增提醒
     * @param saveRemindDTO
     * @return
     */
    @Override
    public Result saveRemind(SaveRemindDTO saveRemindDTO) throws ParseException {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Remind remind = new Remind();
        BeanUtils.copyProperties(saveRemindDTO, remind);
        remind.setUserId(userId);
        remind.setRemindTime(df.parse(saveRemindDTO.getRemindTime()));

        log.info("执行新增提醒");
        int insert = baseMapper.insert(remind);
        if (insert == DataBasesOperationResult.ERROR){
            log.error("新增提醒失败");
            ExceptionUtils.error(RemindEnum.SAVE_REMIND_ERROR);
        }

        return Result.success();
    }

    /**
     * 删除提醒
     * @param id
     * @return
     */
    @Override
    public Result removeRemind(Integer id) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        Remind remind = baseMapper.selectById(id);
        if (remind == null){
            ExceptionUtils.error(RemindEnum.THIS_REMIND_IS_NULL);
        }
        if (!remind.getUserId().equals(userId)){
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }

        log.info("执行删除提醒操作");
        int delete = baseMapper.deleteById(id);
        if (delete == DataBasesOperationResult.ERROR){
            log.error("执行删除提醒操作失败");
            ExceptionUtils.error(RemindEnum.REMOVE_REMIND_ERROR);
        }

        return Result.success();
    }

    /**
     * 分页获取提醒列表
     * @param pageDTO
     * @return
     */
    @Override
    public Result getAllRemind(PageDTO pageDTO) {

        Page<Remind> page = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        DateTime startTime = DateTime.now();

        QueryWrapper<Remind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.orderByDesc("remind_time");
        queryWrapper.apply("date_format(remind_time, '%Y-%m-%d %H-%i-%s') <= '"
                + startTime + "'");

        Page<Remind> page1 = baseMapper.selectPage(page, queryWrapper);

        List<RemindVO> list = new ArrayList<>();
        for (Remind record : page1.getRecords()) {
            RemindVO vo = new RemindVO();
            vo.setId(record.getId());
            vo.setRemindTime(record.getRemindTime());
            vo.setUrl(record.getUrl());
            vo.setStatus(record.getStatus());
            list.add(vo);
        }

        return Result.success(PageUtils.restPage(list));
    }

    /**
     * 修改提醒
     * @param updateRemindDTO
     * @return
     */
    @Override
    public Result updateRemind(UpdateRemindDTO updateRemindDTO) throws ParseException {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        Remind remind1 = baseMapper.selectById(updateRemindDTO.getId());
        if (!remind1.getUserId().equals(userId)){
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Remind remind = new Remind();
        BeanUtils.copyProperties(updateRemindDTO, remind);
        remind.setId(remind1.getId());
        remind.setUserId(userId);
        remind.setRemindTime(df.parse(updateRemindDTO.getRemindTime()));

        int update = baseMapper.updateById(remind);
        if (update == DataBasesOperationResult.ERROR){
            ExceptionUtils.error(RemindEnum.UPDATE_REMIND_ERROR);
        }

        return Result.success();
    }

    /**
     * 获取提醒详情
     * @param id
     * @return
     */
    @Override
    public Result getRemind(Integer id) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        Remind remind = baseMapper.selectById(id);
        if (!remind.getUserId().equals(userId)){
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }

        RemindDetailsVO remindDetailsVO = new RemindDetailsVO();
        BeanUtils.copyProperties(remind, remindDetailsVO);

        return Result.success(remindDetailsVO);
    }
}
