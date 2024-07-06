package nutshell.server.dto;

import lombok.Builder;
import nutshell.server.domain.enums.SocialLoginPlatform;

@Builder
public record SocialLoginRequest (
        SocialLoginPlatform socialLoginPlatform,
        String code
){
       public static SocialLoginRequest of(SocialLoginPlatform socialLoginPlatform, String code){
           return new SocialLoginRequest(socialLoginPlatform, code);
       }
}
