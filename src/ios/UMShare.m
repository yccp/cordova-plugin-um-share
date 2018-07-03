#import "UMShare.h"

@implementation UMShare
- (void)pluginInitialize
{
}

- (void)handleOpenURL:(NSNotification *)notification
{
    NSURL* url = [notification object];
    NSLog(@"URL host: %@", url.host);
    if([url.host isEqualToString:@"oauth"] || [url.host isEqualToString:@"qzapp"] || [url.host hasPrefix:@"platformId="]) {
        [[UMSocialManager defaultManager] handleOpenURL:url];
    }
}

- (void)open:(CDVInvokedUrlCommand *)command
{
    //创建分享消息对象
    UMSocialMessageObject *messageObject = [UMSocialMessageObject messageObject];
    
    //创建网页内容对象
    NSDictionary* options = [command.arguments objectAtIndex:0];
    NSString* image = options[@"image"];
    NSString* title = options[@"title"];
    NSString* desc = options[@"desc"];
    NSString* url = options[@"url"];
    UMShareWebpageObject *shareObject = [UMShareWebpageObject shareObjectWithTitle:title descr:desc thumImage:image];
    shareObject.webpageUrl = url;
    
    //分享消息对象设置分享内容对象
    messageObject.shareObject = shareObject;
    
    //显示分享面板
    [UMSocialUIManager showShareMenuViewInWindowWithPlatformSelectionBlock:^(UMSocialPlatformType platformType, NSDictionary *userInfo) {
        // 根据获取的platformType确定所选平台进行下一步操作
        
        //调用分享接口
        [[UMSocialManager defaultManager] shareToPlatform:platformType messageObject:messageObject currentViewController:self.viewController completion:^(id data, NSError *error) {
            if (error) {
                NSLog(@"************Share fail with error %@*********",error);
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription] callbackId:command.callbackId];
            } else {
                if ([data isKindOfClass:[UMSocialShareResponse class]]) {
                    NSLog(@"Share response");
                    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
                    
                }
            }
        }];
    }];
}

- (void)auth:(CDVInvokedUrlCommand *)command
{
    // 获取参数
    NSDictionary* arguments = [command argumentAtIndex:0];
    int platform = [[arguments objectForKey:@"platform"] intValue];
    //开始授权
    [[UMSocialManager defaultManager] getUserInfoWithPlatform:platform currentViewController:self.viewController completion:^(id data, NSError *error) {
        if (error) {
            NSLog(@"************Auth fail with error %@*********",error);
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription] callbackId:command.callbackId];
        } else {
            NSLog(@"Auth~~~");
            if ([data isKindOfClass:[UMSocialAuthResponse class]]) {
                NSLog(@"Auth response");
            }
            if ([data isKindOfClass:[UMSocialUserInfoResponse class]]) {
                NSLog(@"User Info response");
                UMSocialUserInfoResponse *resp = data;
                NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
                NSDictionary* dict = @{
                                       @"uid": resp.uid != nil ? resp.uid : @"",
                                       @"openid": resp.openid != nil ? resp.openid : @"",
                                       @"accessToken": resp.accessToken != nil ? resp.accessToken : @"",
                                       @"refreshToken": resp.refreshToken != nil ? resp.refreshToken : @"",
                                       @"expiration": resp.expiration != nil ? [dateFormatter stringFromDate:resp.expiration] : @"",
                                       @"name": resp.name != nil ? resp.name : @"",
                                       @"iconurl": resp.iconurl != nil ? resp.iconurl : @"",
                                       @"gender": resp.gender != nil ? resp.gender : @""
                                       };
                NSLog(@"dict: %@", dict);
                
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dict] callbackId:command.callbackId];
            }
        }
    }];
}

@end
