package ru.numbDev.wildberries.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "statistic")
public class StatisticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    // Объект продукта
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ProductEntity product;

    // Цена
    @Column(name = "price")
    private Integer price;

    // Продажи (сейчас wb дает только приблизительные)
    @Column(name = "countOfSales")
    private long countOfSales;

    //
    @Column(name = "price_U")
    private long priceU;

    //
    @Column(name = "sale_price_U")
    private long salePriceU;

    // Рейтинг
    @Column(name = "rating")
    private int rating;

    // Дата
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createdDate;
}
