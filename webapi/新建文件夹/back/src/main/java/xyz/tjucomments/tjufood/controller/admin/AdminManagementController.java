// 文件路径: src/main/java/xyz/tjucomments/tjufood/controller/admin/AdminManagementController.java

package xyz.tjucomments.tjufood.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.tjucomments.tjufood.dto.AdminCreateDTO;
import xyz.tjucomments.tjufood.dto.Result;
import xyz.tjucomments.tjufood.entity.Admin;
import xyz.tjucomments.tjufood.service.IAdminService;

import java.util.Map;

@Tag(name = "E02. 平台设置 - 人员管理", description = "对后台管理员账号进行增删改查")
@SecurityRequirement(name = "authorization")
@RestController
@RequestMapping("/api/admin/admins") // 注意路径与登录/退出接口区分
public class AdminManagementController {

    @Resource
    private IAdminService adminService;

    @Operation(summary = "新增管理员", description = "创建一个新的管理员账号，密码默认为'123456'")
    @PostMapping
    public Result addAdmin(@RequestBody AdminCreateDTO createDTO) {
        return adminService.createAdmin(createDTO);
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@Parameter(description = "管理员唯一标识ID") @PathVariable Long id) {
        // 在实际项目中，需要考虑是否允许删除自己，或删除唯一的超级管理员
        boolean isSuccess = adminService.removeById(id);
        return isSuccess ? Result.ok() : Result.fail("删除管理员失败！");
    }

    @Operation(summary = "修改管理员信息", description = "只能修改姓名、状态等基本信息，不能修改密码")
    @PutMapping
    public Result updateAdmin(@RequestBody Admin admin) {
        if (admin.getId() == null) {
            return Result.fail("更新失败，ID不能为空");
        }
        // 出于安全，置空密码字段，防止通过此接口意外更新密码
        admin.setPassword(null);
        boolean isSuccess = adminService.updateById(admin);
        return isSuccess ? Result.ok() : Result.fail("更新管理员信息失败！");
    }

    @Operation(summary = "分页查询管理员列表")
    @GetMapping("/list")
    public Result listAdmins(
            @Parameter(description = "管理员用户名关键词") @RequestParam(required = false) String username,
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size
    ) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(username)) {
            queryWrapper.like("username", username);
        }
        queryWrapper.orderByDesc("create_time");

        Page<Admin> page = adminService.page(new Page<>(current, size), queryWrapper);
        // 隐藏密码
        page.getRecords().forEach(admin -> admin.setPassword(null));
        return Result.ok(page.getRecords(), page.getTotal());
    }
}