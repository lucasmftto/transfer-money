package br.com.transfermoney.feign;

import java.util.Map;

public record NotificationResource(String message, String email, Map<String, Object> fields) {
}
