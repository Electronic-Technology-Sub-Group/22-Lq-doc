声明文件指的是 `.d.ts` 文件，记录了对应 `ts` 或 `js` 所用到的声明和类型，使用 `declare` 关键字声明，有点类似于 Cpp 的头文件，作用更像是给 IDE 和 tsc 看的文档

# 参考写法

## 对象

通过 `declare namespace` 描述全局变量的值

```typescript
let myLib = {
    makeGreeting: (string) => string
    numberOfGreetings: number
}
```

对应 `.d.ts` 为：

```typescript
declare namespace myLib {
    function makeGreeting(s: string): string
    let numberOfGreetings: number
}
```

## 函数重载

只写重载声明，不写实现签名

```typescript
declare function getWidget(id: number): Widget
declare function getWidget(name: string): Widget[]
```

## 接口

使用 `interface` 定义具有属性的类型，*`interface` 不需要 `declare`*

```typescript
interface GreetingSettings {
    greeting: string
    duration?: number
    color?: string
}

declare function greet(settings: GreetingSettings): void
```

## 别名

别名也不需要 `declare`

```typescript
type GreetingLike: string | (() => string) | MyGreeter

declare function greet(g: GreetingLike): void
```

## 组织类型

使用命名空间来组织类型

```typescript
declare namespace GreetingLib {
    interface LogOptions {
        verbose?: boolean
    }
	interface AlertOptions {
	    modal: boolean
	    title?: string
	    color?: string
	}
}
```

也可以用嵌套命名空间，以 `.` 分隔

```typescript
declare namespace GreetingLib.Options {
    interface Log {
        verbose?: boolean
    }
    interface Alert {
        modal: boolean
        title?: string
        color?: string
    }
}
```

## 类

使用 `declare class` 描述

```typescript
declare class Greeter {
    constructor(greeting: string)
    
    greeting: string
    
    showGreeting(): void
}
```

## 全局变量

使用 `declare var`，`declare const`，`declare let` 声明全局变量

```typescript
declare var foo: number
```

## 全局函数

使用 `declare function` 声明函数

```typescript
declare function greet(greeing: string): void
```

# 库结构
#未完成 