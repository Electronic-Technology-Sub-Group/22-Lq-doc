
通用 Windows 平台（UWP）应用中，只能访问特定的目录，或者让用户选择文件。
# 文件系统

- `FileSystemInfo`：表示任何文件系统对象的基类
- `File`：包含静态方法，不能实例化。只要路径就可以执行一个操作，可省去创建 .Net 对象的开销
- `FileInfo`：包含与 File 类似的公共方法，需要实例化。要对同一个文件进行多个操作使用这些比较有效；提供文件属性的访问，但只有 创建时间和最后一次访问时间是可修改的
- `Directory` 与 `DirectoryInfo`：操作目录，关系类似 `File` 与 `FileInfo`
- Path：包含可用于处理路径名的静态方法
	- Combine：组合路径，放置遗漏单个分隔符或使用太多字符
	- GetXxx：获取路径的各个部分
	- DirectorySeparatorChar：分割文件夹的分隔符，win下：\
- Environment：有关当前环境和平台的信息以及操作它们的方法
	- GetFolderPath：获取系统特殊文件夹的目录路径
	- GetEnvironmentVariable：检索环境变量
	- "HOMEDRIVE"：系统所在磁盘驱动器
	- "HOMEPATH"：当前用户目录，不包含磁盘驱动器
- DriveInfo：提供指定驱动器的信息，可用驱动器列表和任何驱动器的大量细节

```csharp
/*
Drive name: C:\
Format: NTFS
Type: Fixed
Root directory: C:\
Volumn label:
Free space: 42469195776
Available space: 42469195776
Total size: 127281393664

Drive name: D:\
Format: NTFS
Type: Fixed
Root directory: D:\
Volumn label:
Free space: 253473484800
Available space: 253473484800
Total size: 1000203833344
*/
DriveInfo[] drives = DriveInfo.GetDrives();
foreach (DriveInfo drive in drives)
{
    if(drive.IsReady)
    {
        WriteLine($"Drive name: {drive.Name}");
        WriteLine($"Format: {drive.DriveFormat}");
        WriteLine($"Type: {drive.DriveType}");
        WriteLine($"Root directory: {drive.RootDirectory}");
        WriteLine($"Volumn label: {drive.VolumeLabel}");
        WriteLine($"Free space: {drive.TotalFreeSpace}");
        WriteLine($"Available space: {drive.AvailableFreeSpace}");
        WriteLine($"Total size: {drive.TotalSize}");
        WriteLine();
    }
}
```

# 观察文件修改

使用 `FileSystemWatcher` 监视文件的更改，事件在创建、重命名、删除和更改文件时触发

```csharp
var watcher = new FileSystemWatcher(path, filter)
{
    IncludeSubdirectories = true;
}
watcher.Created += (object sender, FileSystemEventArgs e) => {};
watcher.Changed += (object sender, FileSystemEventArgs e) => {};
watcher.Deleted += (object sender, FileSystemEventArgs e) => {};
watcher.Renamed += (object sender, RenamedEventArgs e) => {};
watcher.Error += (object sender, ErrorEventArgs e) => {};
watcher.EnableRaisingEvents = true;
```
# 流

用于传输数据的对象
- 读取流：数据从外部源传入到程序中
- 写入流：数据从程序中传出到外部源
- 随机存取：某些流允许随机存取，允许将读写游标在流的不同位置改变

使用流，可以压缩数据，利用内存映射的文件和管道在不同任务中共享数据
- 网咯：使用网络协议读写网络上的数据，目的是选择数据或从另一个计算机发送数据
- 管道：读写到命名管道上
- 内存：把数据度写到一个内存区域上

用完后要及时释放，或使用 using
## FileStream

用于二进制文件中读写二进制数据
- fileName：要访问的文件
- FileMode：打开文件的模式
- FileAccess：访问文件的方式
- FileShare：共享访问策略

```csharp
new FileStream(fileName, FileMode.Open, FileAccess.Read, FileShare.Read);
File.OpenRead(fileName); // FileAccess.Read, FileShare.Read
```

随机访问流：可以快速访问文件特定位置，通过 `Seek` 跳转到指定位置
  
  ```csharp
using (FileStream stream = File.OpenRead("./samplefile.data"))
{
    byte[] buffer = new byte[RECORDSIZE];
    while (true)
    {
        try
        {
            WriteLine("record number (or 'bye' to End): ");
            string line = ReadLine();
            if (line.ToUpper().CompareTo("BYE") == 0) break;
            int record;
            if(int.TryParse(line, out record))
            {
                stream.Seek((record - 1) * RECORDSIZE, SeekOrigin.Begin);
                stream.Read(buffer, 0, RECORDSIZE);
                string s = Encoding.UTF8.GetString(buffer);
                WriteLine($"record: {s}");
            }
        }
        catch (Exception ex)
        {
            WriteLine(ex.Message);
        }
    }
    WriteLine("finished");
}
```

- 分析编码：BOM

```ad-info
BOM: Byte Order Mark, 字节顺序标记, 提供了文件如何编码的信息
```
  
```csharp
Encoding GetEncoding(FileStream stream)
{
    if (!stream.CanSeek)
        throw new ArgumentException("Require a stream that can seek")
    Encoding encoding = Encoding.ASCII
    byte[] bom = new byte[5];
    int nRead = stream.Read(bom, 0, 5);
    if (bom[0] == 0xff && bom[1] == 0xfe && bom[2] == 0 && bom[3] == 0)
    {
        WriteLine("UTF-32");
        stream.Seek(2, SeekOrigin.Begin);
        return Encoding.Unicode;
    }
    else if (bom[0] == 0xfe && bom[1] == 0xff)
    {
        WriteLine("UTF-16, big endian");
        stream.Seek(2, SeekOrigin.Begin);
        return Encoding.BigEndianUnicode;
    }
    else if (bom[0] == 0xef && bom[1] == 0xbb && bom[2] == 0xbf)
    {
        WriteLine("UTF-8");
        stream.Seek(3, SeekOrigin.Begin);
        return Encoding.UTF8;
    }
    stream.Seek(0, SeekOrigin.Begin);
    return encoding;
}
```
## 读取器和写入器

- `StreamReader` / `StreamWriter`：专门用于读写文本格式的流
	- 默认 UTF-8，使用 `EndOfStream` 判断是否已经到结尾
	- 当其释放后，内保存的 Stream 也会同时释放
- `BinaryReader` / `BinaryWriter`：专门用于读写二进制格式的流
## 压缩文件

- DeflateStream：使用抑制算法 RFC 1951 压缩与解压缩

```csharp
// 压缩: DeflateStream -> CompressionMode.Compress
using (FileStream inputStream = File.OpenRead(fileName))
{
    FileStream outputStream = File.OpenWrite(compressedFileName);
    using (var cs = new DeflateStream(outputStream, CompressionMode.Compress))
    {
        inputStream.CopyTo(cs);
    }
}

// 解压缩: DeflateStream -> CompressionMode.Decompress
FileStream inputStream = File.OpenRead(fileName);
using (MemoryStream outputStream = new MemoryStream())
{
    using (var cs = new DeflateStream(inputStream, CompressionMode.Decompress))
    {
        cs.CopyTo(outputStream);
        outputStream.Seek(0, SeekOrigin.Begin);
        using (var reader = new StreamReader(outputStream, 
            Encoding.UTF8, 
            detectEncodingFromByteOrderMarks: true,
            bufferSize: 4096, leaveOpen: true))
        {
            string result = reader.ReadToEnd();
            WriteLine(result);
        }
    }
}
```

- GZipStream：增加了循环冗余检验，Win 不能直接打开 GZipStream 压缩的文件
	- ZipArchive：创建和读取 Zip 文件

```csharp
void CreateZipFile(string directory, string zipFile)
{
    FileStream zipStream = File.OpenWrite(zipFile);
    using(var archive = new ZipArchive(zipStream, ZipArchiveMode.Create))
    {
        IEnumerable<string> files = 
            Directory.EnumerateFiles(directory, "*", SearchOption.TopDirectoryOnly);

        foreach (var file in files)
        {
            ZipArchiveEntry entry = archive.CreateEntry(Path.GetFileName(file));
            using (FileStream inputStream = File.OpenRead(file))
            {
                using (Stream outputStream = entry.Open())
                {
                    inputStream.CopyTo(outputStream);
                }
            }
        }
    }
}
```
# 内存映射

- 允许访问文件，或在不同的进程中共享内存
- 允许使用物理文件或共享的内存（把页面文件作为后备储存器，共享内存可大于可用物理内存）
- 创建内存映射后, 即可创建一个视图, 用于映射完整内存映射文件的一部分, 以此访问, 读写
- 内存映射文件通信时, 必须同步读取器和写入器, 读取器才知道数据何时可用

```csharp
private ManualResetEventSlim _mapCreated = new ManualResetEventSlim(false);
private ManualResetEventSlim _dataWrittenEvent = new ManualResetEventSlim(false);
private const string MAPNAME = "SampleMap";

// Write
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    using (MemoryMappedViewAccessor accessor = mappedFile.CreateViewAccessor(0, 10000, MemoryMappedFileAccess.Write))
    {
        for (int i = 0, pos = 0; i < 100; i++, pos += 4)
        {
            accessor.Write(pos, i);
            await Task.Delay(10);
        }
        _dataWrittenEvent.Set();
    }
}

// Write Using Stream
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    MemoryMappedViewStream stream = mappedFile.CreateViewStream(0, 10000, MemoryMappedFileAccess.Write);
    using (var writer = new StreamWriter(stream))
    {
        writer.AutoFlush = true;
        for (int i = 0; i < 100; i++)
        {
            string s = $"some data {i}";
            await writer.WriteLineAsync(s);
        }
        _dataWrittenEvent.Set();
    }
}

// Read
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    using (MemoryMappedViewAccessor accessor = mappedFile.CreateViewAccessor(0, 10000, MemoryMappedFileAccess.Write))
    {
        for (int i = 0, pos = 0; i < 100; i++, pos += 4)
        {
            accessor.Write(pos, i);
            await Task.Delay(10);
        }
        _dataWrittenEvent.Set();
    }
}

// Read Using Stream
_mapCreated.Wait();
using (MemoryMappedFile mappedFile = MemoryMappedFile.OpenExisting(MAPNAME, MemoryMappedFileRights.Read)) 
{
    MemoryMappedViewStream stream = mappedFile.CreateViewStream(0, 10000, MemoryMappedFileAccess.Read);
    using (var reader = new StreamReader(stream))
    {
        _dataWrittenEvent.Wait();
        for (int i = 0; i < 100; i++)
        {
            long pos = stream.Position;
            string s = await reader.ReadLineAsync();
        }
    }
}
```
# 管道

在不同系统之间快速通信；C# 中，管道实现为流
## 命名管道

名称可以用于链接到每一端

```csharp
// 创建命名管道服务器，用于读取
// PipeTransmissionMode: 
//     Byte: 连续的流
//     Message: 可检索每条消息
// PipeOptions: 
//     WriteThrough: 立即写入管道而不缓存
// PipeSecurity: 管道安全性
// HandleInheritability: 句柄可继承性
using (var pipeReader = new NamedPipeServerStream(pipeName, PipeDirection.In))
{
    pipeReader.WaitForConnection();
    const int BUFFERSIZE = 256;
    bool completed = false;
    while (!completed)
    {
        byte[] buffer = new byte[BUFFERSIZE];
        int nRead = pipeReader.Read(buffer, 0, BUFFERSIZE);
        string line = Encoding.UTF8.GetString(buffer, 0, nRead);
        WriteLine(line);
        if (line == "bye") completed = true;
    }
}

// 创建命名管道客户端，用于写入
var pipeWriter = new NamedPipeClientStream(".", pipeName, PipeDirection.Out);
using (var writer = new StreamWriter(pipeWriter))
{
    pipeWriter.Connect();
    bool completed = false;
    while (!completed)
    {
        string input = ReadLine();
        if (input == "bye") completed = true;
        writer.WriteLine(input);
        writer.Flush();
    }
}
```
## 匿名管道

只能用于一个父子进程之间的通信或不同任务之间的通信

```csharp
void Reader()
{
    try
    {
        var pipeReader = new AnonymousPipeServerStream(PipeDirection.In, System.IO.HandleInheritability.None);
        using (var reader = new StreamReader(pipeReader))
        {
            _pipeHandle = pipeReader.GetClientHandleAsString();
            WriteLine($"pipe handler: {_pipeHandle}");
            _pipeHandleSet.Set()
            bool end = false;
            while (!end)
            {
                string line = reader.ReadLine();
                WriteLine(line);
                if (line == "end") end = true;
            }
            WriteLine("finished reading");
        }
    }
    catch (Exception ex)
    {
        WriteLine(ex.Message);
    }
}

void Writer()
{
    WriteLine("anonymous pipe writer");
    _pipeHandleSet.Wait()
    var pipeWriter = new AnonymousPipeClientStream(PipeDirection.Out, _pipeHandle);
    using (var writer = new StreamWriter(pipeWriter))
    {
        writer.AutoFlush = true;
        WriteLine("starting writer");
        for (int i = 0; i < 5; i++)
        {
            writer.WriteLine($"Message {i}");
            Task.Delay(500).Wait();
        }
        writer.WriteLine("end");
    }
}
```
