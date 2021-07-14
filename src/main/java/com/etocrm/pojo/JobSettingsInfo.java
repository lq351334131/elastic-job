package com.etocrm.pojo;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class JobSettingsInfo implements Serializable {

    private String jobName;

    private String jobType;

    private String jobClass;

    private String cron;

    private int shardingTotalCount;

    private String shardingItemParameters;

    private String jobParameter;

    private boolean monitorExecution;

    private boolean streamingProcess;

    private int maxTimeDiffSeconds;

    private int monitorPort = -1;

    private boolean failover;

    private boolean misfire;

    private String jobShardingStrategyClass;

    private String description;

    private Map<String, String> jobProperties = new LinkedHashMap<>(JobProperties.JobPropertiesEnum.values().length, 1);

    private String scriptCommandLine;

    private int reconcileIntervalMinutes;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"jobName\":\"")
                .append(jobName).append('\"');
        sb.append(",\"jobType\":\"")
                .append(jobType).append('\"');
        sb.append(",\"jobClass\":\"")
                .append(jobClass).append('\"');
        sb.append(",\"cron\":\"")
                .append(cron).append('\"');
        sb.append(",\"shardingTotalCount\":")
                .append(shardingTotalCount);
        sb.append(",\"shardingItemParameters\":\"")
                .append(shardingItemParameters).append('\"');
        sb.append(",\"jobParameter\":\"")
                .append(jobParameter).append('\"');
        sb.append(",\"monitorExecution\":")
                .append(monitorExecution);
        sb.append(",\"streamingProcess\":")
                .append(streamingProcess);
        sb.append(",\"maxTimeDiffSeconds\":")
                .append(maxTimeDiffSeconds);
        sb.append(",\"monitorPort\":")
                .append(monitorPort);
        sb.append(",\"failover\":")
                .append(failover);
        sb.append(",\"misfire\":")
                .append(misfire);
        sb.append(",\"jobShardingStrategyClass\":\"")
                .append(jobShardingStrategyClass).append('\"');
        sb.append(",\"description\":\"")
                .append(description).append('\"');
        sb.append(",\"jobProperties\":")
                .append(JSON.toJSON(jobProperties));
        sb.append(",\"scriptCommandLine\":\"")
                .append(scriptCommandLine).append('\"');
        sb.append(",\"reconcileIntervalMinutes\":")
                .append(reconcileIntervalMinutes);
        sb.append('}');
        return sb.toString();
    }
}
