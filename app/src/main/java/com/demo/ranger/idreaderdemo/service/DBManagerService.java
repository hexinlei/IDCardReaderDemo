package com.demo.ranger.idreaderdemo.service;

import com.demo.ranger.idreaderdemo.entity.ResultInfoTable;
import com.demo.ranger.idreaderdemo.util.LogUtil;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hexinlei on 2017/5/4.
 */
public class DBManagerService {

    private final static String DBNAME = "IDCardReaderDB.db";

    private final static int CURTAINVER = 1;

    private DbManager db = null;


    public DBManagerService() {
        initDBManager();
    }

    /**
     * 初始化数据库连接
     */
    private void initDBManager(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(DBNAME)
                .setDbVersion(CURTAINVER)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });

        db = x.getDb(daoConfig);
    }

    /**
     * 插入数据
     * @param resultInfoTable
     */
    public void insert(ResultInfoTable resultInfoTable){
        try{
            if (null == db){
                initDBManager();
            }
            db.save(resultInfoTable);
        }catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("DbException",e);
        }
    }

    /**
     * 按时间删除过期数据
     * @param days
     */
    public void delete(final int days){
        try{
            if (null == db){
                initDBManager();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-days);
            Date date = calendar.getTime();

            db.delete(ResultInfoTable.class, WhereBuilder.b("createTime","<=",date));

        }catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("DbException",e);
        }
    }




}
