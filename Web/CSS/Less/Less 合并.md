合并指的是，将一个属性拆成多个同名属性。编译后多个同名属性互相连接

# 逗号连接

在属性之后添加一个 `+` 号，可使出现多次时，不再被覆盖，而是连接成一个属性

```Less
.my_class {  
  box-shadow+: inset 0 0 10px #555;  
  box-shadow+: 0 0 20px black;  
}
```

编译成 CSS 后：

```CSS
.my_class {  
  box-shadow: inset 0 0 10px #555, 0 0 20px black;  
}
```

# 空格连接

在属性之后添加一个 `+_` 号，可使出现多次时，不再被覆盖，而是连接成一个属性

```Less
.my_class {  
  transform+_: scale(2);  
  transform+_: rotate(15deg);  
}
```

编译成 CSS 后：

```CSS
.my_class {  
  transform: scale(2) rotate(15deg);  
}
```