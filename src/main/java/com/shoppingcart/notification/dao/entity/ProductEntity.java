package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @PositiveOrZero
    private double price;

    @PositiveOrZero
    private BigDecimal quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name="category_id", nullable=false, updatable=false)
    private CategoryEntity categoryEntity;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "productEntity")
    private List<GalleryEntity> galleries=new ArrayList<>();

    public void setCategoryById(final String categoryId){
        this.categoryEntity = new CategoryEntity(categoryId);
    }


    public void setGalleriesEntity(List<GalleryEntity> galleries){
        galleries.forEach(g->g.setProductEntity(this));
        this.galleries = galleries;
    }
}
