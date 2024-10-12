> [!error] `java.lang.ClassCastException: class [B cannot be cast to class com.example.shopping.model.UserMq ([B is in module java.base of loader 'bootstrap'; com.example.shopping.model.UserMq is in unnamed module of loader 'app')`
> ```
> 2024-10-11T16:30:19.672+08:00 ERROR 7340 --- [userservice] [ntainer #0-0-C-1 ] [                                                 ] o.s.kafka.listener.DefaultErrorHandler   : Backoff FixedBackOff{interval=0, currentAttempts=1, maxAttempts=0} exhausted for user-topic-0@9
> 
> org.springframework.kafka.listener.ListenerExecutionFailedException: Listener method 'public void com.example.shopping.mq.UserMessageListener.onUserMessage(org.apache.kafka.clients.consumer.ConsumerRecord<java.lang.String, com.example.shopping.model.UserMq>)' threw exception
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:2869) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeOnMessage(KafkaMessageListenerContainer.java:2814) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeOnMessage(KafkaMessageListenerContainer.java:2778) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer $ListenerConsumer.lambda$ doInvokeRecordListener$53(KafkaMessageListenerContainer.java:2701) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at io.micrometer.observation.Observation.observe(Observation.java:565) ~[micrometer-observation-1.13.4.jar:1.13.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeRecordListener(KafkaMessageListenerContainer.java:2699) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeWithRecords(KafkaMessageListenerContainer.java:2541) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeRecordListener(KafkaMessageListenerContainer.java:2430) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:2085) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeIfHaveRecords(KafkaMessageListenerContainer.java:1461) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1426) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1296) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run$$$capture(CompletableFuture.java:1804) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run(CompletableFuture.java) ~[na:na]
> 	at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
> 	Suppressed: org.springframework.kafka.listener.ListenerExecutionFailedException: Restored Stack Trace
> 		at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:435) ~[spring-kafka-3.2.4.jar:3.2.4]
> 		at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invoke(MessagingMessageListenerAdapter.java:384) ~[spring-kafka-3.2.4.jar:3.2.4]
> 		at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:85) ~[spring-kafka-3.2.4.jar:3.2.4]
> 		at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:50) ~[spring-kafka-3.2.4.jar:3.2.4]
> 		at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeOnMessage(KafkaMessageListenerContainer.java:2800) ~[spring-kafka-3.2.4.jar:3.2.4]
> Caused by: java.lang.ClassCastException: class [B cannot be cast to class com.example.shopping.model.UserMq ([B is in module java.base of loader 'bootstrap'; com.example.shopping.model.UserMq is in unnamed module of loader 'app')
> 	at com.example.shopping.mq.UserMessageListener.onUserMessage(UserMessageListener.java:20) ~[classes/:na]
> 	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
> 	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
> 	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:169) ~[spring-messaging-6.1.13.jar:6.1.13]
> 	at org.springframework.kafka.listener.adapter.KotlinAwareInvocableHandlerMethod.doInvoke(KotlinAwareInvocableHandlerMethod.java:45) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:119) ~[spring-messaging-6.1.13.jar:6.1.13]
> 	at org.springframework.kafka.listener.adapter.HandlerAdapter.invoke(HandlerAdapter.java:70) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:420) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invoke(MessagingMessageListenerAdapter.java:384) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:85) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:50) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeOnMessage(KafkaMessageListenerContainer.java:2800) ~[spring-kafka-3.2.4.jar:3.2.4]
> 	... 13 common frames omitted
> ```

暂未解决，但可以通过 `ObjectMapping` 手动转换为字符串或字节数组发送
