package com.yfvia.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yfvia.wiki.domain.User;
import com.yfvia.wiki.domain.UserExample;
import com.yfvia.wiki.exception.BusinessException;
import com.yfvia.wiki.exception.BusinessExceptionCode;
import com.yfvia.wiki.mapper.UserMapper;
import com.yfvia.wiki.req.UserQueryReq;
import com.yfvia.wiki.req.UserSaveReq;
import com.yfvia.wiki.resp.UserQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.utils.CopyUtil;
import com.yfvia.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * 查询用户数据
     */
    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }


        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> users = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(users);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());

        List<UserQueryResp> copyList = new ArrayList<>();
        for (User user : users) {
            UserQueryResp copyUser = CopyUtil.copy(user, UserQueryResp.class);
            copyUser.setId(user.getId().toString());
            copyList.add(copyUser);
        }

        pageResp.setList(copyList);

        return pageResp;
    }

    /**
     * 查询所有的用户数据
     */
    public PageResp<UserQueryResp> all() {
        List<User> users = userMapper.selectByExample(new UserExample());
        PageResp<UserQueryResp> resp = new PageResp<>();
        resp.setTotal(0L);

        List<UserQueryResp> copyList = new ArrayList<>();
        for (User user : users) {
            UserQueryResp copyUser = CopyUtil.copy(user, UserQueryResp.class);
            copyUser.setId(user.getId().toString());
            copyList.add(copyUser);
        }

        resp.setList(copyList);
        return resp;
    }

    /**
     * 保存用户数据
     */
    public Boolean save(UserSaveReq req) {
        //新增用户
        if (ObjectUtils.isEmpty(req.getId())) {

            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                User user = CopyUtil.copy(req, User.class);
                user.setId(snowFlake.nextId());
                int res = userMapper.insert(user);
                if (res != 1) return false;
                return true;
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
//            更新用户
            User user = userMapper.selectByPrimaryKey(Long.parseLong(req.getId()));
            if (ObjectUtils.isEmpty(user)) {
                return false;
            }

            User newUser = CopyUtil.copy(req, User.class);
            newUser.setId(Long.valueOf(req.getId()));

//            后端防修改用户名，设置为空，使用selective不更新空字段
            newUser.setLoginName(null);
            newUser.setPassword(null);
            int res = userMapper.updateByPrimaryKeySelective(newUser);
            if (res != 1) return false;
            return true;
        }
    }


    /**
     * 删除用户
     */
    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(id)) return false;

        int res = userMapper.deleteByPrimaryKey(id);
        if (res != 1) return false;

        return true;
    }


    /**
     * 通过用户名查询用户
     *
     * @param LoginName
     * @return
     */
    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }
}
