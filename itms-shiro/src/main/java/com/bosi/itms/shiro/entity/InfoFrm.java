package com.bosi.itms.shiro.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "t_info_frm")
public class InfoFrm implements Serializable {
    private String frmId;

    private String frmName;

    private String frmPid;

    private String iconPath;

    private String url;

    private BigDecimal showIndex;

    private String isEnabled;

    private String isLevel3;

    private String description;

    private String appCode;

    private String openMode;

    private String remarks;

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId == null ? null : frmId.trim();
    }

    public String getFrmName() {
        return frmName;
    }

    public void setFrmName(String frmName) {
        this.frmName = frmName == null ? null : frmName.trim();
    }

    public String getFrmPid() {
        return frmPid;
    }

    public void setFrmPid(String frmPid) {
        this.frmPid = frmPid == null ? null : frmPid.trim();
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath == null ? null : iconPath.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public BigDecimal getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(BigDecimal showIndex) {
        this.showIndex = showIndex;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled == null ? null : isEnabled.trim();
    }

    public String getIsLevel3() {
        return isLevel3;
    }

    public void setIsLevel3(String isLevel3) {
        this.isLevel3 = isLevel3 == null ? null : isLevel3.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode == null ? null : openMode.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}