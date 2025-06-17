// 文件路径: src/main/java/xyz/tjucomments/tjufood/controller/admin/IncentiveRuleAdminController.java

package xyz.tjucomments.tjufood.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import xyz.tjucomments.tjufood.dto.Result;
import xyz.tjucomments.tjufood.entity.IncentiveRule;
import xyz.tjucomments.tjufood.service.IIncentiveRuleService;

import java.util.List;

@Tag(name = "D04. 运营管理 - 激励规则管理", description = "对用户行为的积分奖励规则进行管理")
@SecurityRequirement(name = "authorization")
@RestController
@RequestMapping("/api/admin/incentive-rules")
public class IncentiveRuleAdminController {

    @Resource
    private IIncentiveRuleService incentiveRuleService;

    @Operation(summary = "新增激励规则")
    @PostMapping
    public Result addIncentiveRule(@RequestBody IncentiveRule incentiveRule) {
        // ID由数据库自增
        boolean isSuccess = incentiveRuleService.save(incentiveRule);
        return isSuccess ? Result.ok(incentiveRule.getId()) : Result.fail("新增激励规则失败！");
    }

    // 激励规则通常不提供删除，而是通过修改状态来禁用，此处为完整性提供删除接口
    @Operation(summary = "删除激励规则", description = "注意：通常不建议删除，而是修改状态为禁用")
    @DeleteMapping("/{id}")
    public Result deleteIncentiveRule(@PathVariable Long id) {
        boolean isSuccess = incentiveRuleService.removeById(id);
        return isSuccess ? Result.ok() : Result.fail("删除激励规则失败！");
    }

    @Operation(summary = "修改激励规则")
    @PutMapping
    public Result updateIncentiveRule(@RequestBody IncentiveRule incentiveRule) {
        if (incentiveRule.getId() == null) {
            return Result.fail("更新失败，ID不能为空");
        }
        boolean isSuccess = incentiveRuleService.updateById(incentiveRule);
        return isSuccess ? Result.ok() : Result.fail("更新激励规则失败！");
    }

    @Operation(summary = "查询所有激励规则列表")
    @GetMapping("/list")
    public Result listAllIncentiveRules() {
        // 激励规则数量通常不多，直接返回列表即可
        List<IncentiveRule> list = incentiveRuleService.list();
        return Result.ok(list);
    }
}