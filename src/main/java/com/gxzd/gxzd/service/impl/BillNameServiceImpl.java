package com.gxzd.gxzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzd.gxzd.constant.DataBasesOperationResult;
import com.gxzd.gxzd.dto.*;
import com.gxzd.gxzd.entity.*;
import com.gxzd.gxzd.enums.BillNameEnum;
import com.gxzd.gxzd.enums.UserEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import com.gxzd.gxzd.mapper.BillMapper;
import com.gxzd.gxzd.mapper.BillNameMapper;
import com.gxzd.gxzd.service.BillNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxzd.gxzd.service.BillService;
import com.gxzd.gxzd.service.BillSharingService;
import com.gxzd.gxzd.service.UserService;
import com.gxzd.gxzd.utils.PageUtils;
import com.gxzd.gxzd.utils.RedisCommonUtils;
import com.gxzd.gxzd.utils.RedisUtils;
import com.gxzd.gxzd.utils.Result;
import com.gxzd.gxzd.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
public class BillNameServiceImpl extends ServiceImpl<BillNameMapper, BillName> implements BillNameService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private BillSharingService billSharingService;

    @Resource
    private UserService userService;

    @Resource
    private BillService billService;

    @Resource
    private BillMapper billMapper;

    @Resource
    private RedisCommonUtils redisCommonUtils;

    /**
     * 新增账单
     *
     * @param saveBillNameDTO
     * @return
     */
    @Override
    @Transactional
    public Result saveBill(SaveBillNameDTO saveBillNameDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        BillName billName = new BillName();
        BeanUtils.copyProperties(saveBillNameDTO, billName);
        billName.setUserId(userId);

        Integer integer = baseMapper.insert(billName);
        log.info("执行新增账单");
        if (integer == DataBasesOperationResult.ERROR) {
            log.error("新增账单失败");
            ExceptionUtils.error(BillNameEnum.SAVE_BILLNAME_ERROR);
        }

        BillSharing billSharing = new BillSharing();
        billSharing.setUserId(userId);
        billSharing.setBillNameId(billName.getId());
        billSharing.setSharingUserId(userId);
        log.info("执行新增共享账单");
        boolean save = billSharingService.save(billSharing);
        if (!save) {
            ExceptionUtils.error(BillNameEnum.SAVE_BILLSHARING_ERROR);
        }


        return Result.success();
    }

    /**
     * 修改账单
     *
     * @param updateBillNameDTO
     * @return
     */
    @Override
    @Transactional
    public Result updateBill(UpdateBillNameDTO updateBillNameDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        BillName billName = baseMapper.selectById(updateBillNameDTO.getId());
        if (billName == null) {
            ExceptionUtils.error(BillNameEnum.THIS_BILL_IS_NOLL);
        }
        if (!billName.getUserId().equals(userMessage.getId())) {
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }
        BeanUtils.copyProperties(updateBillNameDTO, billName);
        Integer update = baseMapper.updateById(billName);
        log.info("执行修改账单");
        if (update == DataBasesOperationResult.ERROR) {
            log.error("执行修改账单失败");
            ExceptionUtils.error(BillNameEnum.UPDATE_BILL_ERROR);
        }

        return Result.success();
    }

    /**
     * 删除账单
     *
     * @param id
     * @return
     */
    @Override
    public Result removeBill(Integer id) {

        if (id == null) {
            ExceptionUtils.error(BillNameEnum.BILL_ID_IS_NOT_NULL);
        }

        BillName billName = baseMapper.selectById(id);
        if (billName == null) {
            ExceptionUtils.error(BillNameEnum.THIS_BILL_IS_NOLL);
        }
        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        if (!billName.getUserId().equals(userMessage.getId())) {
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_name_id", id);
        List<Bill> list = billService.list(queryWrapper);
        for (Bill bill : list) {
            billService.removeBillRecord(bill.getId());
        }

        Integer delete = baseMapper.deleteById(id);
        log.info("执行删除账单");
        if (delete == DataBasesOperationResult.ERROR) {
            log.error("删除账单失败");
            ExceptionUtils.error(BillNameEnum.REMOVE_BILL_ERROR);
        }

        return Result.success();
    }

    /**
     * 分页获取账单
     *
     * @param pageDTO
     * @return
     */
    @Override
    public Result getAllBill(PageDTO pageDTO) {
        UserCache userMessage = redisUtils.getRedisUserMessage(request);

        QueryWrapper<BillSharing> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sharing_user_id", userMessage.getId());
        List<BillSharing> list2 = billSharingService.list(queryWrapper1);
        Set<Integer> set = new HashSet<>();
        for (BillSharing billSharing : list2) {
            set.add(billSharing.getSharingUserId());
        }
        List<BillNameVO> list = new ArrayList<>();
        Page<BillName> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());
        for (Integer sharingUserId : set) {
            QueryWrapper<BillName> wrapper = new QueryWrapper<>();
            wrapper.gt("id", pageDTO.getEndIndexId());
            wrapper.eq("user_id", sharingUserId);
            wrapper.orderByDesc("create_time");
            Page<BillName> page1 = baseMapper.selectPage(page, wrapper);

            for (BillName record : page1.getRecords()) {
                BillNameVO vo = new BillNameVO();
                vo.setId(record.getId());
                vo.setBillName(record.getBillName());
                vo.setContent(record.getContent());
                vo.setSharing(record.getSharing());
                vo.setCreateTime(record.getCreateTime());
                Integer income = billMapper.sumPrice(record.getId(), 1);
                Integer expenditure = billMapper.sumPrice(record.getId(), 2);
                expenditure = expenditure == null ? 0 : expenditure;
                income = income == null ? 0 : income;
                vo.setExpenditure(expenditure);
                vo.setIncome(income);
                list.add(vo);
            }
        }


        return Result.success(PageUtils.restPage(list));
    }

    /**
     * 模糊查询账单
     *
     * @param billName
     * @return
     */
    @Override
    public Result getBillLike(String billName) {

        QueryWrapper<BillName> wrapper = new QueryWrapper<>();
        wrapper.like("bill_name", billName);
        List<BillName> billNames = baseMapper.selectList(wrapper);
        List<BillLikeVO> billLikeVOs = new ArrayList<>();
        for (BillName name : billNames) {
            BillLikeVO billLikeVO = new BillLikeVO();
            billLikeVO.setBillName(name.getBillName());
            billLikeVO.setId(name.getId());
            billLikeVOs.add(billLikeVO);
        }
        return Result.success(billLikeVOs);

    }

    /**
     * 新增共享账本邀请
     *
     * @param saveSharingBillDTO
     * @return
     */
    @Override
    public Result saveSharingBill(SaveSharingBillDTO saveSharingBillDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        BillName billName = baseMapper.selectById(saveSharingBillDTO.getId());
        if (!billName.getUserId().equals(userId)) {
            ExceptionUtils.error(UserEnum.USER_LOGING_NOT_ONE);
        }

        if (!billName.getSharing().equals(1)){
            ExceptionUtils.error(BillNameEnum.BILLSHARING_IS_NOT_SHARING);
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", saveSharingBillDTO.getUsername());
        User user = userService.getOne(wrapper);
        if (user == null){
            ExceptionUtils.error(UserEnum.USER_IS_NOT_EXIST);
        }
        BillSharing billSharing = new BillSharing();
        billSharing.setBillNameId(saveSharingBillDTO.getId());
        billSharing.setSharingUserId(user.getId());
        billSharing.setUserId(userId);

        QueryWrapper<BillSharing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_name_id",saveSharingBillDTO.getId());
        queryWrapper.eq("sharing_user_id",user.getId());
        queryWrapper.eq("user_id",userId);
        BillSharing one = billSharingService.getOne(queryWrapper);
        if (one != null){
            ExceptionUtils.error(BillNameEnum.BILLSHARING_IS_NOT_NULL);
        }
        log.info("新增邀请");
        boolean save = billSharingService.save(billSharing);
        if (!save) {
            log.error("新增邀请失败");
            ExceptionUtils.error(BillNameEnum.SAVE_BILLSHARING_USER_ERROR);
        }

        return Result.success();
    }

    /**
     * 获取账单详情
     *
     * @param id
     * @param pageDTO
     * @return
     */
    @Override
    public Result getBillDetails(Integer id, PageDTO pageDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);
        Integer userId = userMessage.getId();

        BillName billName = baseMapper.selectById(id);
        if (billName.getSharing() == 1) {
            QueryWrapper<BillSharing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bill_name_id", id);
            queryWrapper.eq("sharing_user_id", userId);
            BillSharing billSharing = billSharingService.getOne(queryWrapper);
            if (billSharing == null) {
                ExceptionUtils.error(UserEnum.USER_LOGIN_NOT_SHARTING);
            }
        }

        Page<Bill> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());
        QueryWrapper<Bill> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("bill_name_id", id);
        queryWrapper1.orderByDesc("create_time");
        queryWrapper1.gt("id", pageDTO.getEndIndexId());
        Page<Bill> page1 = billService.page(page, queryWrapper1);

        List<BillRecordVO> billRecordVOS = new ArrayList<>();
        for (Bill bill : page1.getRecords()) {
            BillRecordVO billRecordVO = new BillRecordVO();
            billRecordVO.setUsername(userMessage.getUsername());
            billRecordVO.setStatus(bill.getStatus());
            billRecordVO.setType(bill.getType());
            billRecordVO.setPrice(bill.getPrice());
            billRecordVO.setCreateTime(bill.getCreateTime());
            billRecordVOS.add(billRecordVO);
        }

        Integer income = billMapper.sumPrice(id, 1);
        Integer expenditure = billMapper.sumPrice(id, 2);
        expenditure = expenditure == null ? 0 : expenditure;
        income = income == null ? 0 : income;
        BillDetailsVO billDetailsVO = new BillDetailsVO();
        billDetailsVO.setExpenditure(expenditure);
        billDetailsVO.setIncome(income);
        billDetailsVO.setBillRecords(billRecordVOS);

        return Result.success(billDetailsVO);
    }

    /**
     * 根据年月获取账单列表
     *
     * @param billDTO
     * @param pageDTO
     * @return
     */
    @Override
    public Result getBillByYM(GetBillDTO billDTO, PageDTO pageDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);

        Page<BillName> page = new Page<>(pageDTO.getPageNum(),
                pageDTO.getPageSize());

        String startTime = billDTO.getApplyTime();

        QueryWrapper<BillSharing> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sharing_user_id", userMessage.getId());
        List<BillSharing> list2 = billSharingService.list(queryWrapper1);
        Set<Integer> set = new HashSet<>();
        for (BillSharing billSharing : list2) {
            set.add(billSharing.getSharingUserId());
        }
        List<BillNameListVO> list = new ArrayList<>();

        for (Integer sharingUserId : set) {
            QueryWrapper<BillName> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", sharingUserId);
            queryWrapper.gt("id", pageDTO.getEndIndexId());
            queryWrapper.orderByAsc("create_time");
            queryWrapper.apply("date_format(create_time, '%Y-%m') = '"
                    + startTime + "'");

            Page<BillName> page1 = baseMapper.selectPage(page, queryWrapper);

            for (BillName billName : page1.getRecords()) {
                BillNameListVO billNameListVO = new BillNameListVO();
                billNameListVO.setBillName(billName.getBillName());
                billNameListVO.setId(billName.getId());
                list.add(billNameListVO);
            }
        }

        return Result.success(PageUtils.restPage(list));
    }

    /**
     * 根据年获取统计数据
     *
     * @param getBillDTO
     * @return
     */
    @Override
    public Result getAllCountByYear(GetBillYearDTO getBillDTO) {

        UserCache userMessage = redisUtils.getRedisUserMessage(request);

        Integer userId = userMessage.getId();

        String startTime = getBillDTO.getApplyTime();

        GetBillYearVO getBillYearVO = new GetBillYearVO();

        Integer status = getBillDTO.getStatus();

        QueryWrapper<BillSharing> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sharing_user_id", userId);
        List<BillSharing> list2 = billSharingService.list(queryWrapper1);
        Set<Integer> set = new HashSet<>();
        for (BillSharing billSharing : list2) {
            set.add(billSharing.getSharingUserId());
        }

        Map<Integer, Integer> map = new HashMap<>();

        List<BillDetailsByCount> detailsByCounts = new ArrayList<>();

        for (Integer sharingUserId : set) {
            QueryWrapper<BillName> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", sharingUserId);
            queryWrapper.orderByDesc("create_time");
            queryWrapper.apply("date_format(create_time, '%Y') = '"
                    + startTime + "'");

            List<BillName> page1s = baseMapper.selectList(queryWrapper);

            int n = page1s.size();
            if (n > 2) {
                n = 2;
            }
            for (int i = 0; i < n; i++) {
                Integer billNameId = page1s.get(i).getId();
                String billName = page1s.get(i).getBillName();

                Integer income = billMapper.sumPrice(billNameId, 1);
                Integer expenditure = billMapper.sumPrice(billNameId, 2);
                expenditure = expenditure == null ? 0 : expenditure;
                income = income == null ? 0 : income;

                BillDetailsByCount billDetailsByCount = new BillDetailsByCount();
                billDetailsByCount.setBillName(billName);
                billDetailsByCount.setIncome(income);
                billDetailsByCount.setExpenditure(expenditure);
                detailsByCounts.add(billDetailsByCount);
            }

            for (BillName billName : page1s) {
                Integer billNameId = billName.getId();
                if (billName.getSharing() == 1) {
                    QueryWrapper<BillSharing> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("bill_name_id", billNameId);
                    queryWrapper2.eq("sharing_user_id", userId);
                    BillSharing billSharing = billSharingService.getOne(queryWrapper2);
                    if (billSharing == null) {
                        ExceptionUtils.error(UserEnum.USER_LOGIN_NOT_SHARTING);
                    }
                }
                int count = billMapper.countByBill(billNameId);
                Integer count1 = getBillYearVO.getBillNumCount();
                count1 = count1 == null ? 0 : count1;
                getBillYearVO.setBillNumCount(count + count1);

                for (int i = 1; i <= 10; i++) {
                    Integer sum = billMapper.typePrice(billNameId, status, i);
                    sum = sum == null ? 0 : sum;
                    if (map.containsKey(i)) {
                        map.put(i, map.get(i) + sum);
                    } else {
                        map.put(i, sum);
                    }
                }

            }
        }
        Map<Integer, Integer> map1 = new HashMap<>();
        map.forEach((key, value) -> {
            if (map.get(key) != 0) {
                map1.put(key, value);
            }
        });

        getBillYearVO.setBillMap(map1);

        Integer result = 0;
        for (Integer price : map1.values()) {
            result += price;
        }

        getBillYearVO.setBillCount(result);

        getBillYearVO.setBillDetailsList(detailsByCounts);

        return Result.success(getBillYearVO);
    }

    /**
     * 获取月账单
     * @param getBillDTO
     * @return
     */
    @Override
    public Result getAllCountByMoon(GetBillMoonDTO getBillDTO) {
        UserCache userMessage = redisUtils.getRedisUserMessage(request);

        Integer userId = userMessage.getId();

        String startTime = getBillDTO.getApplyTime();

        GetBillYearVO getBillYearVO = new GetBillYearVO();

        Integer status = getBillDTO.getStatus();

        QueryWrapper<BillSharing> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sharing_user_id", userId);
        List<BillSharing> list2 = billSharingService.list(queryWrapper1);
        Set<Integer> set = new HashSet<>();
        for (BillSharing billSharing : list2) {
            set.add(billSharing.getSharingUserId());
        }

        Map<Integer, Integer> map = new HashMap<>();

        List<BillDetailsByCount> detailsByCounts = new ArrayList<>();

        for (Integer sharingUserId : set) {
            QueryWrapper<BillName> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", sharingUserId);
            queryWrapper.orderByDesc("create_time");
            queryWrapper.apply("date_format(create_time, '%Y-%m') = '"
                    + startTime + "'");

            List<BillName> page1s = baseMapper.selectList(queryWrapper);

            int n = page1s.size();
            if (n > 2) {
                n = 2;
            }
            for (int i = 0; i < n; i++) {
                Integer billNameId = page1s.get(i).getId();
                String billName = page1s.get(i).getBillName();

                Integer income = billMapper.sumPrice(billNameId, 1);
                Integer expenditure = billMapper.sumPrice(billNameId, 2);
                expenditure = expenditure == null ? 0 : expenditure;
                income = income == null ? 0 : income;

                BillDetailsByCount billDetailsByCount = new BillDetailsByCount();
                billDetailsByCount.setBillName(billName);
                billDetailsByCount.setIncome(income);
                billDetailsByCount.setExpenditure(expenditure);
                detailsByCounts.add(billDetailsByCount);
            }

            for (BillName billName : page1s) {
                Integer billNameId = billName.getId();
                if (billName.getSharing() == 1) {
                    QueryWrapper<BillSharing> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("bill_name_id", billNameId);
                    queryWrapper2.eq("sharing_user_id", userId);
                    BillSharing billSharing = billSharingService.getOne(queryWrapper2);
                    if (billSharing == null) {
                        ExceptionUtils.error(UserEnum.USER_LOGIN_NOT_SHARTING);
                    }
                }
                int count = billMapper.countByBill(billNameId);
                Integer count1 = getBillYearVO.getBillNumCount();
                count1 = count1 == null ? 0 : count1;
                getBillYearVO.setBillNumCount(count + count1);

                for (int i = 1; i <= 10; i++) {
                    Integer sum = billMapper.typePrice(billNameId, status, i);
                    sum = sum == null ? 0 : sum;
                    if (map.containsKey(i)) {
                        map.put(i, map.get(i) + sum);
                    } else {
                        map.put(i, sum);
                    }
                }

            }
        }
        Map<Integer, Integer> map1 = new HashMap<>();
        map.forEach((key, value) -> {
            if (map.get(key) != 0) {
                map1.put(key, value);
            }
        });

        getBillYearVO.setBillMap(map1);

        Integer result = 0;
        for (Integer price : map1.values()) {
            result += price;
        }

        getBillYearVO.setBillCount(result);

        getBillYearVO.setBillDetailsList(detailsByCounts);

        return Result.success(getBillYearVO);
    }
}
