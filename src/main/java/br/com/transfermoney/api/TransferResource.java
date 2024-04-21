package br.com.transfermoney.api;

import java.math.BigDecimal;

public record TransferResource(BigDecimal value, Long payer, Long payee) {
}
