package com.bosi.itms.shiro.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

@TableName(value = "t_info_role")
public class InfoRole implements Serializable {
    private String roleId;

    private String roleName;

    private String deptCode;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}