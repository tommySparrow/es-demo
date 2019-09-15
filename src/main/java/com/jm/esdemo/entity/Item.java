package com.jm.esdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author jerry
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "item",type = "docs")
public class Item {
    @Id
    @Field(type = FieldType.Long,store = true)
    private Long id;
    @Field(type = FieldType.Text,store = true, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,store = true, analyzer = "ik_max_word")
    private String category;
    @Field(type = FieldType.Text,store = true, analyzer = "ik_max_word")
    private String brand;
    @Field(type = FieldType.Double,store = true)
    private Double price;
    @Field(type = FieldType.Text,store = true)
    private String images;
}
