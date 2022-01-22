package com.yfvia.wiki.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveReq {
    private String id;

    private String parent;
    @NotNull(message = "【名称】不能为空！")
    private String name;
    @NotNull(message = "【排序】不能为空！")
    private Integer sort;

}