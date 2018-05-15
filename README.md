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
只有安装相应的子插件才能显示和使用相应选项

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
}, () => {
  console.log('success');
}, e => {
  console.error(e);
});
```

>授权
```js
window.UMShare.auth({
  platform: 1, // 1为微信，4为QQ，其它暂没有
}, userInfo => {
  console.log(userInfo);
}, e => {
  console.error(e);
});
```