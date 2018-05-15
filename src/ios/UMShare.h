#import <Cordova/CDVPlugin.h>
#import <UMShare/UMShare.h>
#import <UShareUI/UShareUI.h>

@interface UMShare : CDVPlugin

- (void)open:(CDVInvokedUrlCommand *)command;
- (void)auth:(CDVInvokedUrlCommand *)command;

@end
