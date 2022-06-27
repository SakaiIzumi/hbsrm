package net.bncloud.quotation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMaterCountReVo implements Serializable {

    private Long materialGroupId;

    private String materialGroupName;

    private Integer materialCount;

}
