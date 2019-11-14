package com.plough.msql.config.service;


import com.plough.msql.config.model.SqlConfigModel;
import com.plough.msql.config.model.SqlJob;
import com.plough.msql.config.model.SqlPlan;
import com.plough.msql.config.model.SqlStatementModel;
import com.plough.msql.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: element_analysis_etl
 * @author: dwang
 * @create: 2019-09-27 15:20
 * @description: SqlPlanCreateService
 **/

@Service
@Slf4j
public class SqlPlanCreateService {


    public SqlPlan createSqlPlan(List<SqlConfigModel> sqlConfigModelList) {
        SqlPlan sqlPlan = new SqlPlan();

        Map<String, SqlJob> processedJopMap = new HashMap<>(16);


        int minParallelism = -1;

        List<SqlStatementModel> orderedStatementList = new ArrayList<>();
        List<SqlStatementModel> tmpStatementList = new ArrayList<>();
        Map<String, Boolean> tmpStatementMap = new HashMap<>(16);


        //pre process start
        for (SqlConfigModel sqlConfigModel : sqlConfigModelList) {
            orderedStatementList.clear();
            tmpStatementList.clear();

            int itemParallelism = sqlConfigModel.getParallelism();
            if (minParallelism == -1 || itemParallelism < minParallelism) {
                minParallelism = itemParallelism;
            }


            List<SqlStatementModel> sqlStatementModelList = sqlConfigModel.getSql();

            for (SqlStatementModel statement : sqlStatementModelList) {
                if (CollectionUtil.isEmpty(statement.getDepondOn())) {
                    SqlJob sqlJob = new SqlJob();
                    sqlJob.addStatement(statement);
                    processedJopMap.put(statement.getName(), sqlJob);
                    tmpStatementMap.put(statement.getName(), Boolean.TRUE);
                } else {

                    tmpStatementList.add(statement);


                }
            }

            // rearrange statement order
            while (tmpStatementList.size() > 0) {

                int preSize = tmpStatementList.size();
                Iterator<SqlStatementModel> tmpIt = tmpStatementList.iterator();

                while (tmpIt.hasNext()) {

                    SqlStatementModel statement = tmpIt.next();

                    List<String> parentNameList = statement.getDepondOn();

                    boolean shouldAdd = true;
                    for (String parentName : parentNameList) {
                        if (Objects.isNull(tmpStatementMap.get(parentName))) {
                            shouldAdd = false;
                        }
                    }
                    if (shouldAdd) {
                        orderedStatementList.add(statement);
                        tmpStatementMap.put(statement.getName(), Boolean.TRUE);
                        tmpIt.remove();
                    }
                }

                int afterSize = tmpStatementList.size();

                if (preSize == afterSize) {
                    throw new RuntimeException("config error exception");
                }
            }


            // rearrange statement order
            Iterator<SqlStatementModel> it = orderedStatementList.iterator();

            while (it.hasNext()) {
                SqlStatementModel statement = it.next();
                List<String> parentNameList = statement.getDepondOn();

                SqlJob baseJob = null;

                for (String parentName : parentNameList) {
                    SqlJob parentJob = processedJopMap.get(parentName);

                    if (Objects.nonNull(parentJob)) {

                        if (Objects.isNull(baseJob)) {
                            baseJob = parentJob;
                        } else if (baseJob != parentJob) {
                            baseJob.connectSqlJob(parentJob);
                            processedJopMap.put(parentName, baseJob);
                        }

                    } else {
                        throw new RuntimeException("config error SqlStatementModel :" + statement.getName());
                    }
                }

                baseJob.addStatement(statement);
                processedJopMap.put(statement.getName(), baseJob);


            }


        }
        Set<SqlJob> sqlJobs = processedJopMap.values().stream().collect(Collectors.toSet());

        for (SqlJob sqlJob : sqlJobs) {

            sqlPlan.addSqlJob(sqlJob);
        }

        if (minParallelism > 0) {
            sqlPlan.setParallelism(minParallelism);
        }


        return sqlPlan;

    }

}
