"use strict";
let str = "1"; //根据初始的赋值推导出变量的类型,以后str得类型不能改了
//str = 1; //报错 变量在定义的时候,类型已经确定下来了,不能修改
const num = 1; //常量不能改变指向(不能被修改) 他的值就是他的类型
//num="2"; //报错 原因:常量不能改变指向(不能被修改)
// ts原始类型有哪些? js基础数据类型:string、number、boolean、symbol、null、undefined
// ts原始类型就是js基本数据类型
let str1 = "1";
let bool = false;
let num1 = 10;
num1.toFixed(2);
// str1.toFixed(2); //报错
let und = undefined;
let nul = null;
let sy = Symbol("123");
let vo = undefined;
function a() { }
// function c(): undefined {} //报错
function b() {
    return undefined;
}
//如果想写undefined必须返回undefined
//在ts 中函数没有返回值 函数类型就是void
a();
b();
