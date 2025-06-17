// src/main/java/xyz/tjucomments/tjufood/service/impl/StallReviewServiceImpl.java
package xyz.tjucomments.tjufood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.tjucomments.tjufood.dto.Result;
import xyz.tjucomments.tjufood.dto.UserDTO;
import xyz.tjucomments.tjufood.entity.Stall;
import xyz.tjucomments.tjufood.entity.StallReview;
import xyz.tjucomments.tjufood.interceptor.UserHolder;
import xyz.tjucomments.tjufood.mapper.StallReviewMapper;
import xyz.tjucomments.tjufood.service.IStallReviewService;
import xyz.tjucomments.tjufood.service.IStallService;
import xyz.tjucomments.tjufood.utils.constants.IdPrefixConstants;
import xyz.tjucomments.tjufood.utils.constants.SystemConstants;
import xyz.tjucomments.tjufood.utils.id.RedisIdWorker;

import java.util.List;

@Service
public class StallReviewServiceImpl extends ServiceImpl<StallReviewMapper, StallReview> implements IStallReviewService {

    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private IStallService stallService;

    @Override
    @Transactional
    public Result addReview(StallReview review) {
        UserDTO currentUser = UserHolder.getUser();
        if (currentUser == null) {
            return Result.fail("请先登录！");
        }
        review.setUserId(currentUser.getId());

        // 【修正】规范化使用常量
        review.setId(redisIdWorker.nextId(IdPrefixConstants.REVIEW_ID_PREFIX));

        boolean isSuccess = save(review);
        if (!isSuccess) {
            return Result.fail("评价失败！");
        }
        updateStallScores(review.getStallId());
        return Result.ok(review.getId());
    }

    @Override
    public Result queryReviewsByStallId(Long stallId, Integer current) {
        Page<StallReview> page = page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE),
                new QueryWrapper<StallReview>().eq("stall_id", stallId).orderByDesc("create_time"));
        return Result.ok(page.getRecords(), page.getTotal());
    }

    private void updateStallScores(Long stallId) {
        List<StallReview> reviews = list(new QueryWrapper<StallReview>().eq("stall_id", stallId));
        if (reviews.isEmpty()) {
            return;
        }

        double avgOverall = reviews.stream().mapToInt(StallReview::getOverallScore).average().orElse(0.0);
        double avgTaste = reviews.stream().filter(r -> r.getTasteScore() != null).mapToInt(StallReview::getTasteScore).average().orElse(0.0);
        double avgPrice = reviews.stream().filter(r -> r.getPriceScore() != null).mapToInt(StallReview::getPriceScore).average().orElse(0.0);

        // 【修正】只更新Stall实体中存在的字段
        Stall stall = new Stall();
        stall.setId(stallId);
        stall.setScore(avgOverall);
        stall.setTasteScore(avgTaste);
        stall.setPriceScore(avgPrice);
        stall.setComments(reviews.size());

        stallService.updateById(stall);
    }
}