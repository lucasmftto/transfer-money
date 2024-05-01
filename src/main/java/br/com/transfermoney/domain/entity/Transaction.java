package br.com.transfermoney.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document
public class Transaction {

    @Id
    private String transactionId;
    @Indexed
    private String clientId;
    private TransactionType transactionType;
    private String description;
    private LocalDate date;
    @Field(targetType = DECIMAL128)
    private BigDecimal value;


    public Transaction() {
    }

    public Transaction(String clientId, TransactionType transactionType, String description, LocalDate date,
                       BigDecimal value) {
        this.clientId = clientId;
        this.transactionType = transactionType;
        this.description = description;
        this.date = date;
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
