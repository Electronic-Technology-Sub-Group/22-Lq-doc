# async

C# 并行编程可使用 `async`、`await` 关键字修饰方法
- `async` 只能修饰返回 `void` 和 `Task<>` 类型的方法, 且不能修饰 `main` 方法
- `await` 只能用于返回 `Task<>` 的方法

```c#
await Task.Run(() => {
    for (int i = 0; i < 1000000; i++)
    {
        list.Add("String " + i);
    }
});
```
# Parallel

对线程的抽象，提供对数据和任务的并行性，若需要更多控制并行性，如设置优先级，就需要使用 Thread 类。

Parallel 提供不同并行任务的创建方法并执行

- `Parallel.Invoke`：直接传入 `Action` 对象作为任务

```csharp
Action a1 = () => Console.WriteLine("Task 1");
Action a2 = () => Console.WriteLine("Task 2");
Action a3 = () => Console.WriteLine("Task 3");
Action a4 = () => Console.WriteLine("Task 4");
Action a5 = () => Console.WriteLine("Task 5");
Parallel.Invoke(a1, a2, a3, a4, a5);
```

- `Parallel.For`：使用类似 for 循环的方式创建任务

```csharp
Parallel.For(0, 10, i => {
    Task.Delay(300);
	Console.WriteLine($"{i} - {Thread.CurrentThread.Name}");
});

// 相当于
Action[] actions = new Action[10];
for  (int i = 0; i < 10; i++)
{
    int j = i;
    actions[i] = () =>
    {
        Task.Delay(300);
        Console.WriteLine($"{j} - {Thread.CurrentThread.Name}");
    };
}
Parallel.Invoke(actions);
```

- `Parallel.ForEach`：使用类似 foreach 循环的方式创建任务

```csharp
string[] data = {
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
};
Parallel.ForEach<string>(data, item => {
    Task.Delay(300);
    Console.WriteLine(item);
});

// 相当于
Action[] actions = new Action[data.Length];
int i = 0;
foreach (var item in data)
{
    actions[i++] = () =>
    {
        Task.Delay(300);
        Console.WriteLine(item);
    };
}
Parallel.Invoke(actions);
```

- `<TLocal>`：带有 `<TLocal>` 泛型的方法可拥有返回值，并与其他结果合并

创建的 Task 可以接受一个 `ParallelLoopState` 参数，该参数可调用 `Break()` 或 `Stop()` 结束循环

方法可传入一个 `ParallelOptions` 类型的配置对象用于指定并行机制
- `CancellationToken`：传递取消通知
- `MaxDegreeOfParallelism`：最大并发数
- `TaskScheduler`：内部调度机制
# Task

表示将完成的某个工作单元，使用 Task 或 TaskFactory 创建和执行
- `new TaskFactory().StartNew(Action)`
- `Task.Factory.StartNew(Action)`
- `new Task(Action).Start()`
- `Task.Run(Action)`
所有方法最终调用的实际都是 `new Task(Action).Start()`

可由父任务创建子任务，若要取消子任务与父任务的绑定状态使用 `TaskCreationOption.DetachedFromParent`

Task 的其他方法包括：
- `Task.FromResult`: 创建一个带结果的任务
- `Task.WaitAll` / `Task.WaitAny`: 创建阻塞的任务调用, 即时调用
- `Task.Delay`: 延时
- `Task.Yield`: 释放 CPU 资源, 让其他任务运行; 没有其他任务时, 立即执行此任务

对于多个 Task 对象，可使用组合器将其组合
- `Task.WhenAll()`: 所有异步方法都完成后返回 Task 对象
- `Task.WhenAny()`: 有一个完成就返回 Task## 任务返回值

带有泛型的版本允许返回值，返回一个 `Future<T>` 类

```csharp
void TaskWithResult()
{
    Task<Tuple<int, int>> t1 = new Task<Tuple<int, int>>(TaskMethodResult, Tuple.Create(8, 3), TaskCreationOptions.LongRunning);
    t1.Start();
    Console.WriteLine(t1.Result);
    t1.Wait();
    Console.WriteLine($"result from task: {t1.Result.Item1} {t1.Result.Item2}");
}
```
## 连续任务

使用 `Task.ContinueWith()` 创建连续任务，并通过 `TaskContinuationOptions` 指定在上一个任务成功/失败/取消时启动

```csharp
void TaskContinue()
{
    Action task1;
    Action task2;

    Task t1 = new Task(task1);
    Task t2 = t1.ContinueWith(task2);
    Task t3 = t2.ContinueWith(task2);
    Task t4 = t3.ContinueWith(task2);
    t1.Start();
}
```
## 取消任务

使用 `CancellationTokenSource` 对象取消任务，在创建 `Task` 时传入 `Token`

任务取消后将在任务中引发异常

```csharp
void TaskCancel()
{
    var cts = new CancellationTokenSource();
    cts.Token.Register(() => Console.WriteLine("*** token canceled"));
    cts.CancelAfter(500);
    Task t1 = Task.Run(() =>
    {
        try
        {
            // Do Something
        }
        catch (AggregateException e)
        {
            Console.WriteLine($"exception: {e.GetType().Name}, {e.Message}");
            foreach (var innerExceoption in e.InnerExceptions)
            {
                Console.WriteLine($"inner exception: ${e.InnerException.Message}");
            }
        }
        catch (Exception e)
        {
            Console.WriteLine($"exception: {e.GetType().Name}, {e.Message}");
        }
    }, cts.Token);
}
```
# 数据流

Task Parallel Library Data Flow（TPL Data Flow）

- 动作块：TPL Data Flow 的核心，作为提供数据的源或接收数据的目标

```csharp
// 例：将字符串的信息写入控制台
// 定义一个 ActionBlock
var processInput = new ActionBlock<string>(s => Console.WriteLine($"user input: {s}"));
bool exit = false;
while(!exit)
{
    // 读取用户输入
    String input = Console.ReadLine();
    if (string.Compare(input, "exit", ignoreCase: true) == 0)
    {
        exit = true;
    }
    else
    {
        // 将输入的字符写入 ActionBlock
        processInput.Post(input);
    }
}
```

- 数据块：包括源和目标数据块，都实现了 `IDataBlock` 接口
	- 目标块：实现 `ITargetBlock` 接口
	- 数据源块：实现 `ISourceBlock` 接口

```csharp
// 例：一个同时实现了 ITargetBlock 和 ISourceBlock 接口的数据块：BufferBlock
BufferBlock<string> s_buffer = new BufferBlock<string>();
Task t1 = Task.Run(() => Producer(s_buffer));
Task t2 = Task.Run(() => Consumer(s_buffer));
Task.WaitAll(t1, t2);

void Producer(BufferBlock<string> buffer)
{
    bool exit = false;
    while(!exit)
    {
        string input = Console.ReadLine();
        if (string.Compare(input, "exit", ignoreCase: true) == 0)
        {
            exit = true;
        }
        else
        {
            buffer.Post(input);
        }
    }
}

async void Consumer(BufferBlock<string> buffer)
{
    while(true)
    {
        string data = await buffer.ReceiveAsync();
        Console.WriteLine($"user input: {data}");
    }
}
```

- 连接块：使用 TransformBlock 连接多个块，形成管道

```csharp
ITargetBlock<string> SetupPipeline()
{
    var fileNameForPath = new TransformBlock<string, IEnumerable<string>>(p => GetFileNames(p));
    var lines = new TransformBlock<IEnumerable<string>, IEnumerable<string>>(f => LoadLines(f));
    var words = new TransformBlock<IEnumerable<string>, IEnumerable<string>>(l => GetWords(l));
    var display = new ActionBlock<IEnumerable<string>>(words2 =>
    {
        foreach(var word in words2)
        {
            Console.WriteLine(word);
        }
    });
    fileNameForPath.LinkTo(lines);
    lines.LinkTo(words);
    words.LinkTo(display);
    return fileNameForPath;
}

block.SetupPipeline().Post(".");
```