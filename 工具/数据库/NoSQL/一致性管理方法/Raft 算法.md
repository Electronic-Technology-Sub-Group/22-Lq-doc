分布式架构中日志一致性管理算法，也是 Zookeeper 的核心算法
- 领导者 Leader：一个，接受客户端请求，将日志复制到其他节点，告知其他节点何时应用日志是安全的
- 候选者 Candidate：多个，在 Leader 任期结束时从中选举新领导者
- 追随者 Follower：多个，响应来自 Leader 和 Candidate 的请求