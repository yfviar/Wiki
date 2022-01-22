package com.yfvia.wiki.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryResp {
    private String  id;

    private String  parent;

    private String name;

    private Integer sort;
}