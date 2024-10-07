
> [!error] InvalidRequestException:Error parsing request header. Our best guess of the apiKey is: 0
> ERROR Closing socket for 127.0.0.1:9092-127.0.0.1:6841-13 because of error (kafka.network.Processor)
> org.apache.kafka.common.errors.InvalidRequestException: Error parsing request header. Our best guess of the apiKey is: 0
> Caused by: java.lang.RuntimeException: Error reading byte array of 126 byte(s): only 7 byte(s) available
>         at org.apache.kafka.common.protocol.ByteBufferAccessor.readArray(ByteBufferAccessor.java:60)
>         at org.apache.kafka.common.protocol.Readable.readString(Readable.java:43)
>         at org.apache.kafka.common.message.RequestHeaderData.read(RequestHeaderData.java:122)
>         at org.apache.kafka.common.message.RequestHeaderData.<init>(RequestHeaderData.java:84)
>         at org.apache.kafka.common.requests.RequestHeader.parse(RequestHeader.java:95)
>         at kafka.network.Processor.parseRequestHeader(SocketServer.scala:999)
>         at kafka.network.Processor.$anonfun$processCompletedReceives$1(SocketServer.scala:1012)
>         at java.base/java.util.LinkedHashMap$LinkedValues.forEach(LinkedHashMap.java:647)
>         at kafka.network.Processor.processCompletedReceives(SocketServer.scala:1008)
>         at kafka.network.Processor.run(SocketServer.scala:893)
>         at java.base/java.lang.Thread.run(Thread.java:833)


在 `/config/zookeeper.properties` 中注释掉 `maxClientCnxns=0`