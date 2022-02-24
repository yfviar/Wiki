package com.yfvia.wiki.controller;

import com.yfvia.wiki.exception.BusinessException;
import com.yfvia.wiki.exception.BusinessExceptionCode;
import com.yfvia.wiki.req.UserQueryReq;
import com.yfvia.wiki.req.UserResetPasswordReq;
import com.yfvia.wiki.req.UserSaveReq;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.UserQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 分页查询用户
     */
    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {


        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(userService.list(req));
        return resp;
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(userService.all());
        return resp;
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {

        if (req.getPassword().equals("5804e4ac3bfa0ffb14202d543b0d7ac6")) {
            throw new BusinessException(BusinessExceptionCode.PASSWORD_EMPTY);
        }

        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes(StandardCharsets.UTF_8)));
        userService.save(req);
        CommonResp<Boolean> resp = new CommonResp<>();
        return resp;
    }

    /**
     * 根据id删除用户
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        Boolean res = userService.delete(id);
        CommonResp resp = new CommonResp();
        if (!res) {
            resp.setMessage("删除失败");
            resp.setSuccess(false);
        } else {
            resp.setMessage("删除成功");
        }

        return resp;
    }

    /**
     * 通过名称查询用户
     */
    @GetMapping("/query")
    public CommonResp query(@Valid UserQueryReq req) {

        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        return resp;
    }

    /**
     * 重置密码
     *
     * @param req
     * @return
     */
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {

        if (req.getPassword().equals("5804e4ac3bfa0ffb14202d543b0d7ac6")) {
            throw new BusinessException(BusinessExceptionCode.PASSWORD_EMPTY);
        }
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }
}
