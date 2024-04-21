package br.com.transfermoney.domain.entity;

import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@CompoundIndex(def = "{'id': 1, 'email': 1}", unique = true)
public class Client {

    @Id
    private String id;

    private PersonType personType;
    @Indexed(unique = true)
    private String document;
    private String fullName;
    @Email
    private String email;
    private String password;
    private BigDecimal balance;


    public Client() {
    }

    public Client(String id, PersonType personType, String document, String fullName, String email, String password,
                  BigDecimal balance) {
        this.id = id;
        this.personType = personType;
        this.document = document;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
