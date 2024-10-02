package com.r2s.project_v1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    private Integer id;
    private String name;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "idProductImage")
    private ProductImage productImage;
}
