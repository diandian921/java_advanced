
### 作业一：设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表并在新结构在演示常见的增删改查操作


1. 下载 [ShardingSphere-Proxy](https://www.apache.org/dyn/closer.cgi/shardingsphere/5.0.0-beta/apache-shardingsphere-5.0.0-beta-shardingsphere-proxy-bin.tar.gz) 的最新版本
2. 解压缩后修改 conf/server.yaml和以 config- 前缀开头的文件，如：conf/config-xxx.yaml 文件，进行分片规则配置
3. 引入依赖, 下载 mysql-connector-java-8.0.11.jar，并将其放入ShardingSphere-Proxy的lib 目录。
4. 运行 bin/start.sh 启动

以下为两个配置文件

server.yaml
```yaml
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

######################################################################################################
# 
# If you want to configure governance, authorization and proxy properties, please refer to this file.
# 
######################################################################################################

#governance:
#  name: governance_ds
#  registryCenter:
#    type: ZooKeeper
#    serverLists: localhost:2181
#    props:
#      retryIntervalMilliseconds: 500
#      timeToLiveSeconds: 60
#      maxRetries: 3
#      operationTimeoutMilliseconds: 500
#  overwrite: false

#scaling:
#  blockQueueSize: 10000
#  workerThread: 40

rules:
 - !AUTHORITY
   users:
     - root@%:root
     - sharding@:sharding
   provider:
     type: NATIVE

props:
 max-connections-size-per-query: 1
 executor-size: 16  # Infinite by default.
 proxy-frontend-flush-threshold: 128  # The default value is 128.
   # LOCAL: Proxy will run with LOCAL transaction.
   # XA: Proxy will run with XA transaction.
   # BASE: Proxy will run with B.A.S.E transaction.
 proxy-transaction-type: LOCAL
 xa-transaction-manager-type: Atomikos
 proxy-opentracing-enabled: false
 proxy-hint-enabled: false
 sql-show: true
 check-table-metadata-enabled: false
 lock-wait-timeout-milliseconds: 50000 # The maximum time to wait for a lock


```

config-sharding.yaml
```yaml
schemaName: sharding_db

dataSources:
 ds_0:
   url: jdbc:mysql://127.0.0.1:3306/shopping_0?serverTimezone=UTC&useSSL=false
   username: root
   password: root
   connectionTimeoutMilliseconds: 30000
   idleTimeoutMilliseconds: 60000
   maxLifetimeMilliseconds: 1800000
   maxPoolSize: 50
   minPoolSize: 1
   maintenanceIntervalMilliseconds: 30000
 ds_1:
   url: jdbc:mysql://127.0.0.1:3306/shopping_1?serverTimezone=UTC&useSSL=false
   username: root
   password: root
   connectionTimeoutMilliseconds: 30000
   idleTimeoutMilliseconds: 60000
   maxLifetimeMilliseconds: 1800000
   maxPoolSize: 50
   minPoolSize: 1
   maintenanceIntervalMilliseconds: 30000

rules:
- !SHARDING
 tables:
   order_info:
     actualDataNodes: ds_${0..1}.order_info_${0..15}
     tableStrategy:
       standard:
         shardingColumn: id
         shardingAlgorithmName: order_info_inline
#     keyGenerateStrategy:
#       column: id
#       keyGeneratorName: snowflake
# bindingTables:
#   - t_order,t_order_item
 defaultDatabaseStrategy:
   standard:
     shardingColumn: user_info_id
     shardingAlgorithmName: database_inline
# defaultTableStrategy:
#   none:
 
 shardingAlgorithms:
   database_inline:
     type: INLINE
     props:
       algorithm-expression: ds_${user_info_id % 2}
   order_info_inline:
     type: INLINE
     props:
       algorithm-expression: order_info_${id % 16}
 
#  keyGenerators:
#    snowflake:
#      type: SNOWFLAKE
#      props:
#        worker-id: 123
```

建表语句：
```sql
create table order_info
(
    id                 bigint                              not null comment '主键'
        primary key,
    user_info_id       bigint                              not null comment '用户id'
)comment '订单表';
```

