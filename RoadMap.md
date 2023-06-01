# RoadMap

## v1.0.1
1. [Add] 完善了 DateTime API
2. [Add] 重新设计了一下 Period233, PeriodDad233 (原 PeriodDaddy233), 修改为无状态+深拷贝风格, 避免引用问题
3. [Add] 增加单元测试
4. [Bugfix] JDK LocalDateTime 可能引发的一些 async bug 处理

## v1.0.0
正式发布 v1.0.0
1. [Add] diff 支持 year,month,day,hour,minute,second,ms
2. [Add] 完善了一些 API style 命名 (非核心都改成 getter style)
3. [add] 追加 DateTimeUnit
4. [Add] test unit


## v0.0.8
1. [BugFix] 修复了一些百年后的计算问题
2. [Add] 追加覆盖更全面的单元测试, 设计 plus/minus 时间单位
3. [Add] 追加对比测试
4. [Add] 追加 from Date/LocalDateTime/ZonedDateTime 的构造方式

## v0.0.7
1. [Bugfix] 修复 timeMs, zoneTimeMs 时间戳的算法问题
2. [Add] 完善单元测试, 覆盖到 1970-01-01 的未来 200～500 年
3. [API] DateTimeApi 完善范型


## v0.0.5
1. [Add] DateTime233 完善