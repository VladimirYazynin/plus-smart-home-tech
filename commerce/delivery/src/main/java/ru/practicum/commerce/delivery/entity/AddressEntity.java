package ru.practicum.commerce.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue
    private UUID addressId;

    @Column(length = 20)
    private String country;

    @Column(length = 30)
    private String city;

    @Column(nullable = false, length = 50)
    private String street;

    @Column(length = 10)
    private String house;

    @Column(length = 10)
    private String flat;
}
