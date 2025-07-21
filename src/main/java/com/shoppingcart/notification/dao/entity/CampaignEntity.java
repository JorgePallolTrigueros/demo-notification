package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "campaign")
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campaign")
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private BigDecimal discount;
    @NotNull
    @Column(name = "days_duration")
    private int daysDuration;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "ended_at")
    private Date endedAt;
    @NotNull
    private boolean enabled;

    // === RELACIONES ===

    @ManyToMany
    @JoinTable(
            name = "user_campaign",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_campaign",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products = new ArrayList<>();

    // === CONSTRUCTORES Y UTILIDADES ===

    public CampaignEntity() {
    }

    public CampaignEntity(String name, String description, BigDecimal discount, int daysDuration, boolean enabled) {
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.daysDuration = daysDuration;
        this.enabled = enabled;
        this.createdAt = new Date();
        this.endedAt =  addDays(this.createdAt,daysDuration);
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }


    @Override
    public String toString() {
        return "CampaignEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", daysDuration=" + daysDuration +
                ", createdAt=" + createdAt +
                ", endedAt=" + endedAt +
                ", enabled=" + enabled +
                ", users=" + (users != null ? users.size():"0") +
                ", products=" + (products != null ? products.size():"0") +
                '}';
    }
}
