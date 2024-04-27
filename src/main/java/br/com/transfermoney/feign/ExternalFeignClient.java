package br.com.transfermoney.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "Authorizer", url = "https://run.mocky.io/v3/")
public interface ExternalFeignClient {

    @GetMapping(path = "/5794d450-d2e2-4412-8131-73d0293ac1cc")
    Map<String, String> isAuthorizing(AuthorizationData authorizationData);

    @GetMapping(path = "/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6")
    boolean sendNotification(@RequestBody NotificationResource notificationResource);
}
