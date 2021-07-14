package com.etocrm.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShardingVo implements Serializable {

    private int item;

    private String serverIp;

    private String instanceId;

    private ShardingStatus status;

    private boolean failover;


    /**
     * 作业分片状态.
     *
     * @author caohao
     */
    public enum ShardingStatus {

        DISABLED,
        RUNNING,
        SHARDING_FLAG,
        PENDING;

        /**
         * 获取分片状态.
         *
         * @param isDisabled 是否被禁用
         * @param isRunning 是否在运行
         * @param isShardingFlag 是否需要分片
         * @return 作业运行时状态
         */
        public static ShardingStatus getShardingStatus(final boolean isDisabled, final boolean isRunning, final boolean isShardingFlag) {
            if (isDisabled) {
                return DISABLED;
            }
            if (isRunning) {
                return RUNNING;
            }
            if (isShardingFlag) {
                return SHARDING_FLAG;
            }
            return PENDING;
        }
    }
}
