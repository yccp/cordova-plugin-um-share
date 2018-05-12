# 友盟社会化分享 cordova 插件

> 支持ios, android

## 依赖

- [cordova-plugin-um-common](https://github.com/yccp/cordova-plugin-um-common.git)

## 安装

```
cordova plugin add cordova-plugin-um-share --save
```
或
```
ionic cordova plugin add cordova-plugin-um-share
```


## 子插件
只有安装相应的子插件才能显示相应选项

- [cordova-plugin-um-share-wechat](https://github.com/yccp/cordova-plugin-um-share-wechat.git)

- [cordova-plugin-um-share-qq](https://github.com/yccp/cordova-plugin-um-share-qq.git)


## 使用方法
>打开反馈页面
```js
window.UMShare.open({
  image: 'https://xxx.png', // 缩略图 必须为https协议
  url: 'http://xxx.xxx/xxx', // 链接
  title: 'xxx', // 标题
  desc: 'xxx' // 简介
}, success => {
  console.log(success); // success为bool类型
}, e => {
  console.error(e);
});

```