package com.yfvia.wiki.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocQueryResp {
    private String id;

    private String ebookId;

    private String parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}