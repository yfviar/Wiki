package com.yfvia.wiki.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryResp {
    private String id;

    private String loginName;

    private String name;

    private String password;

}