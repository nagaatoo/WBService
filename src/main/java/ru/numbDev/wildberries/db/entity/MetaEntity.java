package ru.numbDev.wildberries.db.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "meta_product")
public class MetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    // Объект продукта
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ProductEntity product;

    @Column(name = "version")
    private int version;

    @Column(name = "product_name", length = 2000)
    private String productName;

    @Column(name = "brand", length = 2000)
    private String brand;

    @Column(name = "brand_id")
    private long brandId;

    // Экономим диск для базы - сохраняем только последнюю версию
    @Column(name = "page_json", length = 200000)
    private String pageJson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MetaEntity that = (MetaEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
