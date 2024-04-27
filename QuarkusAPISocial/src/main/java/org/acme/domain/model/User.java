package org.acme.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User  {

    //PanacheEntityBase é a classe que tem metodos de persistencia,
    // como o persist, find, delete, etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Age is required")
    private Integer age;


}
