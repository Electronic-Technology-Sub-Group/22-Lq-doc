---
状态: 未完成
未完成部分: 服务器，TCP，UDP
---
C# 中网络包主要有
- `System.Net`：与较高层操作有关，上传下载，Web 请求等
- `System.Net.Socket`：与较低层操作有关，直接使用套接字或 TCP/IP 协议等
# HttpClient

线程安全的 HttpClient 对象，一个对象可处理多个请求，拥有自己的线程池。
- HttpClient 对象间的请求会被隔离，使用 Dispose 释放资源
- 包括 `PostAsync`，`PutAsync`，`DeleteAsync`，`SendAsync` 等方法访问
- 使用 `IsSuccessStatusCode` 或 `EnsureSuccessStatusCode` 判断访问是否成功，`EnsureSuccessStatusCode()` 在 `IsSuccessStatusCode = false` 时抛出异常

```csharp
using (var client = new HttpClient())
{
    var response = await client.GetAsync(NorthwindUrl); 
    if (response.IsSuccessStatusCode)
    {
        WriteLine($"Response Status Code: {response.StatusCode} {response.ReasonPhrase}");
        string responseBodyAsText = await response.Content.ReadAsStringAsync();
        WriteLine($"Received payload of {responseBodyAsText.Length} characters");
        WriteLine();
        WriteLine($"{responseBodyAsText.Substring(0, 20)} ......");
    }
}
```

自定义请求，可自定义 ClientCertificates，Pipelining，CachePolity，ImpersonationLevel 等

```csharp
var client = new HttpClient(new SampleMessageHandler("error"));
HttpResponseMessage response = await client.GetAsync(NorthwindUrl);

public class SampleMessageHandler: HttpClientHandler
{
    private string _message;

    public SampleMessageHandler(string message)
    {
        _message = message;
    }

    protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        WriteLine($"In SampleMessageHandler {_message}");
        if (_message == "error")
        {
            var response = new HttpResponseMessage(System.Net.HttpStatusCode.BadRequest);
            return Task.FromResult(response);
        }
        return base.SendAsync(request, cancellationToken);
    }
}
```
# Uri

- `Uri`: 分析, 组合, 比较 URI
- `UriBuilder`: 将各部分组合成一个 URI

```csharp
void UriSample(string url)
{
    // Uri
    var page = new Uri(url);
    WriteLine($"scheme: {page.Scheme}");
    WriteLine($"host: {page.Host}, type: {page.HostNameType}");
    WriteLine($"port: {page.Port}");
    WriteLine($"path: {page.AbsolutePath}");
    WriteLine($"query: {page.Query}");
    foreach (var segment in page.Segments)
    {
        WriteLine($"segment: {segment}");
    }
    // UriBuilder
    var builder = new UriBuilder();
    builder.Host = "www.cninnovation.com";
    builder.Port = 80;
    builder.Path = "training/MVC";
    Uri uri = builder.Uri;
    WriteLine(uri);
}
```
# IPAddress

IP 地址, 可与字节数组互相转化, 可转化 IPv4 与 IPv6

```csharp
void IPAddressSample(string ipAddressString)
{
    IPAddress address;
    if (!IPAddress.TryParse(ipAddressString, out address))
    {
        WriteLine($"cannot parse {ipAddressString}");
        return;
    }
    byte[] bytes = address.GetAddressBytes();
    for (int i = 0; i < bytes.Length; i++)
    {
        WriteLine($"byte {i}: {bytes[i]:X}");
    }
    
    WriteLine($"family: {address.AddressFamily}, map to ipv6: {address.MapToIPv6()}, map to ipv4: {address.MapToIPv4()}");
    // 特殊地址
    // Loopback: 此 ip 代表主机名 localhost, 可绕过网络硬件
    WriteLine($"IPv4 loopback address: {IPAddress.Loopback}");
    WriteLine($"IPv6 loopback address: {IPAddress.IPv6Loopback}");
    WriteLine($"IPv4 broadcast address: {IPAddress.Broadcast}");
    WriteLine($"IPv4 anycase address: {IPAddress.Any}");
    WriteLine($"IPv6 anycase address: {IPAddress.IPv6Any}");
}
```
# IPHostEntry

封装了与某台特定主机相关的信息
- HostName：返回主机名
- AddressList：返回 IPAddress[]
# WebListener 服务器
# TCP
# UDP
