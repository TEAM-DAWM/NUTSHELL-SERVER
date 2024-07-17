package nutshell.server.discord.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.discord.filter.CachedBodyRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtil {

    // 요청의 URI를 반환
    public static String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    // 요청 헤더를 맵 형태로 반환. 여기서 'user-agent' 헤더는 제외
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    if (!name.equals("user-agent")) {
                        headerMap.put(name, request.getHeader(name));
                    }
                });
        return headerMap;
    }

    // 요청 파라미터를 맵 형태로 반환
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(name -> paramMap.put(name, request.getParameter(name)));

        return paramMap;
    }

    // CachedBodyRequestWrapper 를 사용하여 본문 캐싱 후, 반환한다.
    public static String getBody(HttpServletRequest httpReq) {
        CachedBodyRequestWrapper nativeRequest = WebUtils.getNativeRequest(httpReq, CachedBodyRequestWrapper.class);

        if (nativeRequest != null) {
            return nativeRequest.getBody();
        }
        return "requestBody 정보 없음";
    }

    // 사용자의 IP 주소 반환. 'X-Forwarded-For' 헤더를 먼저 확인하고, 없으면 기본 원격 주소 반환
    public static String getUserIP(HttpServletRequest httpReq) {
        String ip = httpReq.getHeader("X-Forwarded-For");
        if (ip == null)
            ip = httpReq.getRemoteAddr();

        return ip;
    }

    // 사용자의 IP를 기반으로 위치 정보를 반환합니다.
    // ipapi.co API를 사용하여 위치 정보를 가져오며, 반환된 JSON 응답을 파싱하여 맵 형태로 반환합니다.
    public static Map<String, String> getUserLocation(HttpServletRequest request) {
        Map<String, String> locationMap = new HashMap<>();
        String userIP = getUserIP(request);

        String locationFindAPIUrl = "https://ipapi.co/" + userIP + "/json/";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(locationFindAPIUrl, String.class);

        String[] locationEntity = Objects.requireNonNull(response).split(",");

        for (String entity : locationEntity) {
            String[] element = entity.split(":");
            if (element.length == 2) {
                locationMap.put(
                        element[0].replace(" ", "").replace("\n", "").replace("{", "").replace("}", "").replace("\"", ""),
                        element[1].replace(" ", "").replace("\"", ""));
            } else {
                locationMap.put("languages", entity);
            }
        }

        return locationMap;
    }

    // 요청에 포함된 쿠키 배열 반환
    public static Cookie[] getUserCookies(HttpServletRequest httpReq) {
        return httpReq.getCookies();
    }
}
