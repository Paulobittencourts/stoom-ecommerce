package com.br.stoom.commerce.model;


import com.br.stoom.commerce.model.Enum.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String title;

    private String category;

    private String brand;

    private String description;

    private Double price;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availability;
}
