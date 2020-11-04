package me.tadeumendonca.springbootapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class City implements Serializable {

    private static final long serialVersionUID = 4891372606019743765L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name")
    @ColumnTransformer(read = "UPPER(name)", write = "UPPER(?)")
    private String name;

    @NotBlank
    @Column(name = "state")
    @ColumnTransformer(read = "UPPER(state)", write = "UPPER(?)")
    private String state;

}
