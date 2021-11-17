package ru.numbDev.wildberries.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", length = 2000)
    private String productName;

    @Column(name = "brand", length = 2000)
    private String brand;

    @Column(name = "brand_id")
    private long brandId;

    @Column(name = "page_json", length = 200000)
    private String pageJson;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<StatisticEntity> statistics = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_request_like",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"))
    private Set<RequestEntity> requests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_nomenclature",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nomenclature_id", referencedColumnName = "id"))
    private Set<ProductEntity> nomenclatures = new HashSet<>();
}
