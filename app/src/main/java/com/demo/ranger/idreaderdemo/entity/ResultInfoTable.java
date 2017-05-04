package com.demo.ranger.idreaderdemo.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by hexinlei on 2017/5/4.
 */
@Table(name = "ResultInfo")
public class ResultInfoTable {

    @Column(name = "id",isId = true,autoGen = true)
    private int id ;

    @Column(name = "checkType")
    private String checkType;//检查结果 目前三种 pass、check、forbid

    @Column(name = "createTime")
    private Date createTime;//创建时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ResultInfoTable{" +
                "id=" + id +
                ", checkType='" + checkType + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
