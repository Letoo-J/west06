package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.AccountRole;
import com.mine.west.service.AccountRoleService;
import com.mine.west.service.AccountService;
import com.mine.west.service.AccountServiceT;
import com.mine.west.service.RolesService;
import com.mine.west.util.AjaxResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AccountService _accountService;
    @Autowired
    private AccountServiceT accountService;
    @Autowired
    private RolesService _rolesService;
    @Autowired
    private AccountRoleService _accountRoleService;


    @ApiOperation(value = "修改用户角色", notes = "修改用户角色")
    @RequestMapping(value = "/role",method = RequestMethod.PUT)
    public AjaxResponse updateAccountRole(HttpSession session,@RequestBody Map<String, String> m){
        /*
        @Param("roleName")String roleName
         */
        String roleName = m.get("roleName");
        Account account = (Account) session.getAttribute("account");
        //查询roleName对应的roleID
        int roleID = _rolesService.selectIDByRoleName(roleName);

        int accountID = account.getAccountID();
        AccountRole ar = new AccountRole();
        ar.setAccountID(accountID);
        ar.setRoleID(roleID);
        //修改ar表的对应用户的roleID
        if(_accountRoleService.updateAccoutRole(ar)>0){
            return AjaxResponse.success("修改成功");
        }
        return AjaxResponse.fail(500,"修改失败");
    }


    @ApiOperation(value = "修改用户封禁状态", notes = "修改用户封禁状态")
    @RequestMapping(value = "/identity",method = RequestMethod.PUT)
    public AjaxResponse updateAccountIdentity(HttpSession session,@RequestBody Map<String, String> m){
        /*
        @Param("identity")String identity
         */
        String identity = m.get("identity");
        Account account = (Account) session.getAttribute("account");

        account.setIdentity(identity);
        try {
            return AjaxResponse.success(accountService.updateAccount(account));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    @RequestMapping(value = "/allAccounts", method = RequestMethod.GET)
    public AjaxResponse selectAllAccount(){
        List<Account> list = _accountService.selectAll();
        log.info("admin后端查询到了所有账户！");
        return AjaxResponse.success(list,"查询到所有账户");
    }

    @ApiOperation(value = "模糊查询用户", notes = "模糊查询用户")
    @RequestMapping(value = "/accounts",method = RequestMethod.GET)
    public AjaxResponse searchUser(@RequestParam(value = "name")String name){
        List<Account> list = _accountService.selectAccountLikeName(name);
        log.info("admin-searchUser后端【模糊】查询到了账户：" + name);
        return AjaxResponse.success(list,"模糊查询到所有账户");
    }

}
