package xyz.tjucomments.tjufood.dto;

import lombok.Data;
import xyz.tjucomments.tjufood.entity.Stall;
import java.time.LocalDateTime;

@Data
public class StallDTO {

    private Long id;
    private String name;
    private Long canteenId;
    private Long typeId;
    private String location;
    private String introduction;
    private String openHours;
    private Double score;
    private Double tasteScore;
    private Double priceScore;
    private Integer comments;
    private Integer openStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联字段
    private String canteenName;
    private String typeName; // <-- 【新增】这个字段用于存放窗口类型名称

    /**
     * 从实体类转换的静态工厂方法
     * @param stall Stall实体对象
     * @return StallDTO对象
     */
    public static StallDTO fromEntity(Stall stall) {
        if (stall == null) {
            return null;
        }
        StallDTO dto = new StallDTO();
        dto.setId(stall.getId());
        dto.setName(stall.getName());
        dto.setCanteenId(stall.getCanteenId());
        dto.setTypeId(stall.getTypeId());
        dto.setLocation(stall.getLocation());
        dto.setIntroduction(stall.getIntroduction());
        dto.setOpenHours(stall.getOpenHours());
        dto.setScore(stall.getScore());
        dto.setTasteScore(stall.getTasteScore());
        dto.setPriceScore(stall.getPriceScore());
        dto.setComments(stall.getComments());
        dto.setOpenStatus(stall.getOpenStatus());
        dto.setCreateTime(stall.getCreateTime());
        dto.setUpdateTime(stall.getUpdateTime());
        // 注意：canteenName 和 typeName 需要在 Controller 层单独设置
        return dto;
    }
}