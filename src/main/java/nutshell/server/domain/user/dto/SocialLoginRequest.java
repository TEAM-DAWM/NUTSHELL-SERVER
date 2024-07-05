package nutshell.server.domain.user.dto;

import nutshell.server.domain.enums.SocialLoginPlatform;

public record SocialLoginRequest (
        SocialLoginPlatform socialLoginPlatform,
        String code
){
       public static SocialLoginRequest of(SocialLoginPlatform socialLoginPlatform, String code){
           return new SocialLoginRequest(socialLoginPlatform, code);
       }
}
