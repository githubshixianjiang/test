package com.gxzd.gxzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxzd.gxzd.constant.DataBasesOperationResult;
import com.gxzd.gxzd.dto.SaveBillRecordDTO;
import com.gxzd.gxzd.dto.UpdateBillRecordDTO;
import com.gxzd.gxzd.entity.Bill;
import com.gxzd.gxzd.entity.BillName;
import com.gxzd.gxzd.entity.BillSharing;
import com.gxzd.gxzd.entity.UserCache;
import com.gxzd.gxzd.enums.BillEnum;
import com.gxzd.gxzd.enums.UserEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import com.gxzd.gxzd.mapper.BillMapper;
import com.gxzd.gxzd.service.BillNameService;
import com.gxzd.gxzd.service.BillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxzd.gxzd.service.BillSharingService;
import com.gxzd.gxzd.utils.RedisUtils;
import com.gxzd.gxzd.utils.Result;
import com.gxzd.gxzd.vo.MyBillNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-29
 */
@Service
@Slf4j
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private BillNameService billNameService;

    @Resource
    private BillSharingService billSharingService;


    /**
     * 新增账单记录
     *
     * @param saveBillRecord
     * @return
     */
    @Override
    @Transactional
    public Result saveBillRecord(SaveBillRecordDTO saveBillRecord) throws ParseException {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        QueryWrapper<BillSharing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_name_id", saveBillRecord.getBillNameId());
        queryWrapper.eq("sharing_user_id", userId);
        BillSharing billSharing = billSharingService.getOne(queryWrapper);
        if (billSharing == null) {
            ExceptionUtils.error(UserEnum.USER_LOGIN_NOT_SHARTING);
        }
        BillName billName = billNameService.getById(saveBillRecord.getBillNameId());
        Bill billRecord = new Bill();
        BeanUtils.copyProperties(saveBillRecord, billRecord);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        billRecord.setUserId(userId);
        billRecord.setBillTime(sdf.parse(saveBillRecord.getBillTime()));
        billRecord.setSharing(billName.getSharing());
        int insert = baseMapper.insert(billRecord);
        log.info("执行新增账单记录");
        if (insert == DataBasesOperationResult.ERROR) {
            log.error("新增账单记录失败");
            ExceptionUtils.error(BillEnum.SAVE_BILL_RECORD_ERROR);
        }

        return Result.success();
    }

    /**
     * 修改账单记录
     *
     * @param updateBillRecord
     * @return
     */
    @Override
    public Result updateBillRecord(UpdateBillRecordDTO updateBillRecord) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        QueryWrapper<BillSharing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_name_id", updateBillRecord.getBillNameId());
        queryWrapper.eq("sharing_user_id", userId);
        BillSharing billSharing = billSharingService.getOne(queryWrapper);
        if (billSharing == null) {
            ExceptionUtils.error(UserEnum.USER_LOGIN_NOT_SHARTING);
        }

        Bill bill = baseMapper.selectById(updateBillRecord.getId());
        if (!bill.getUserId().equals(userId)) {
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }
        BeanUtils.copyProperties(updateBillRecord, bill);
        Integer update = baseMapper.updateById(bill);
        log.info("执行修改账单记录");
        if (update == DataBasesOperationResult.ERROR) {
            log.error("执行修改账单记录失败");
            ExceptionUtils.error(BillEnum.UPDATE_BILL_RECORD_ERROR);
        }

        return Result.success();
    }

    /**
     * 删除账单记录
     *
     * @param id
     * @return
     */
    @Override
    public Result removeBillRecord(Integer id) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();
        Bill bill = baseMapper.selectById(id);
        if (bill == null) {
            ExceptionUtils.error(BillEnum.BILL_RECORD_IS_NOLL);
        }
        if (!bill.getUserId().equals(userId)) {
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }
        int delete = baseMapper.deleteById(id);
        if (delete == DataBasesOperationResult.ERROR) {
            ExceptionUtils.error(BillEnum.REMOVE_BILL_RECORD_ERROR);
        }

        return Result.success();
    }

    /**
     * 获取我的账单列表
     * @return
     */
    @Override
    public Result getMyBillList() {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);

        Integer userId = userMessage.getId();

        QueryWrapper<BillSharing> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sharing_user_id", userId);
        List<BillSharing> list2 = billSharingService.list(queryWrapper1);
        Set<Integer> set = new HashSet<>();
        for (BillSharing billSharing : list2) {
            set.add(billSharing.getSharingUserId());
        }
        List<MyBillNameVO> list1 = new ArrayList<>();
        for (Integer sharingUserId : set) {
            QueryWrapper<BillName> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", sharingUserId);
            queryWrapper.orderByDesc("create_time");
            List<BillName> list = billNameService.list(queryWrapper);
            for (BillName billName : list) {
                MyBillNameVO myBillNameVO = new MyBillNameVO();

                myBillNameVO.setId(billName.getId());
                myBillNameVO.setBillName(billName.getBillName());
                list1.add(myBillNameVO);

            }
        }

        return Result.success(list1);
    }

}
