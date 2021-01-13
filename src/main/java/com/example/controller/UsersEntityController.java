package com.example.controller;

import com.example.entiy.UsersEntity;
import com.example.service.UsersEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/users")
public class UsersEntityController {

    @Autowired(required = false)
    private UsersEntityService usersEntityService;

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.study.spring.entity.UsersEntity>
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UsersEntity> list() {
        return usersEntityService.usersList();
    }

    /**
     * @description: 通过name来查询信息
     * @param: name
     * @return: com.study.spring.entity.UsersEntity
     */
    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public UsersEntity findUserOne(@PathVariable("name") String name){
        return usersEntityService.findUserOne(name);
    }

    /**
     * @description: 向表中插入一条数据
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Map<String,Boolean> save(UsersEntity usersEntity) {
        Map<String,Boolean> map = new HashMap<>();
        try {
            usersEntityService.saveUser(usersEntity);
            map.put("status",true);
        } catch (Exception e) {
            map.put("status",false);
        }
        return map;
    }

    /**
     * @description: 更新表中数据
     * @return: void
     */
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Map<String,Boolean> update(UsersEntity usersEntity) {
        Map<String,Boolean> map = new HashMap<>();
        try {
            usersEntityService.updateUser(usersEntity);
            map.put("status",true);
        } catch (Exception e) {
            map.put("status",false);
        }
        return map;
    }

    @RequestMapping(value = "remove", method = RequestMethod.DELETE)
    public Map<String,Boolean> remove(@RequestParam(value = "userName",required = true) String name) {
        Map<String,Boolean> map = new HashMap<>();
        try {
            usersEntityService.removeUser(name);
            map.put("status",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",false);
        }
        return map;
    }

    /**
     * @description: 向表中插入一条数据
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public int test() {

        return usersEntityService.test();
    }
}
