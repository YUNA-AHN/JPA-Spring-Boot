package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
