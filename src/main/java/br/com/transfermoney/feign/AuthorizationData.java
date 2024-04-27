package br.com.transfermoney.feign;

public record AuthorizationData(String clientPayerId, String clientPayeeId) {
}
