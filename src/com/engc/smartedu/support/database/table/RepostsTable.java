package com.engc.smartedu.support.database.table;

/**
 * User: Jiang Qi
 * Date: 12-8-10
  */
public class RepostsTable {

    public static final String TABLE_NAME = "reposts_table";
    //support multi user,so primary key can't be message id
    public static final String ID = "_id";
    //support mulit user
    public static final String ACCOUNTID = "accountid";
    //message id
    public static final String MBLOGID = "mblogid";
    public static final String JSONDATA = "json";

}
