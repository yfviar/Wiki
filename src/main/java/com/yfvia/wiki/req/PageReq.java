package com.yfvia.wiki.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReq {
    private Integer pageNum;

    private Integer pageSize;
}
