package com.plough.msql.config.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: element_analysis_etl
 * @author: dwang
 * @create: 2019-09-27 11:22
 * @description: SqlStatementModel
 **/

@Data
public class SqlStatementModel {

  private String name;

  private String description;

  private String statement;

  private List<String> depondOn=new ArrayList<>();


}
