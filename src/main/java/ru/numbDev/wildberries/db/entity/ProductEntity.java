package ru.numbDev.wildberries.db.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "product_id", unique=true)
    private Long productId;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    @ToString.Exclude
    private List<MetaEntity> metaData = new ArrayList<>();

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    @ToString.Exclude
    private List<StatisticEntity> statistics = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_request_like",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<RequestEntity> requests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_nomenclature",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nomenclature_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<ProductEntity> nomenclatures = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductEntity that = (ProductEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
