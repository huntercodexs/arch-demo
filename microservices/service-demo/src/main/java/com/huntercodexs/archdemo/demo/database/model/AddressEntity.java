package com.huntercodexs.archdemo.demo.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "address_service_demo")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column
    public String cep;

    @Column
    public String logradouro;

    @Column
    public String complemento;

    @Column
    public String bairro;

    @Column
    public String localidade;

    @Column
    public String uf;

    @Column
    public String ibge;

    @Column
    public String gia;

    @Column
    public String ddd;

    @Column
    public String siafi;

}
