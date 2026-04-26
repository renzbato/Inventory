package com.example.Inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Float price;
    private Integer quantity;

    private Boolean archive;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel category;

    @LastModifiedBy
    private String updated_by;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @CreatedBy
    private String created_by;

    @CreatedDate
    private LocalDateTime created_at;

}
