package com.antiplagiat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_text")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


}
