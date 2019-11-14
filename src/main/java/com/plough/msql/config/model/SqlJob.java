package com.plough.msql.config.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: element_analysis_etl
 * @author: dwang
 * @create: 2019-09-27 12:11
 * @description: SqlJob
 **/


@Data
public class SqlJob {

    private List<SqlStatementModel> statementLists=new ArrayList<>();

    public void addStatement(SqlStatementModel sqlStatementModel){

      statementLists.add(sqlStatementModel);
    }

    public void connectSqlJob(SqlJob sqlJob){
      statementLists.addAll(sqlJob.getStatementLists());
    }

}
