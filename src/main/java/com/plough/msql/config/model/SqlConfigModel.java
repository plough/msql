package com.plough.msql.config.model;

import lombok.Data;

import java.util.List;

/**
 * @program: element_analysis_etl
 * @author: dwang
 * @create: 2019-09-27 11:19
 * @description: SqlConfigTask
 **/

@Data
public class SqlConfigModel {

  private int parallelism=1;

  private List<SqlStatementModel> sql;



}
