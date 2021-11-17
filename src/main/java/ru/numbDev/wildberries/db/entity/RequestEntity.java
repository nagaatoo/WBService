package ru.numbDev.wildberries.db.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "request")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "request_string")
    private String request;

    @Column(name = "info")
    private String info;

    @ManyToMany(mappedBy="requests")
    private Set<ProductEntity> products = new HashSet<>();
}
