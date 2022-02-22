package com.yfvia.wiki.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocSaveReq {
    private String id;

    private String ebookId;

    private String parent;

    private String name;

    private Integer sort;

    private Integer viewCount = 0;

    private Integer voteCount = 0;

}