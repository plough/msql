package com.plough.msql.config.model;

import lombok.Data;

import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @program: element_analysis_etl
 * @author: dwang
 * @create: 2019-09-27 12:11
 * @description: SqlPlan
 **/

@Data
public class SqlPlan implements Iterable<SqlJob> {

  private int parallelism=1;

  private Deque<SqlJob> sqlJobQueue=new LinkedBlockingDeque<>();

  public void addSqlJob(SqlJob sqlJob){
    sqlJobQueue.add(sqlJob);
  }

  @Override
  public Iterator<SqlJob> iterator() {
    return sqlJobQueue.iterator() ;
  }


}
