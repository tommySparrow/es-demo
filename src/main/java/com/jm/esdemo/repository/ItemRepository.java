package com.jm.esdemo.repository;

import com.jm.esdemo.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Item:为实体类
 * Long:为Item实体类中主键的数据类型
 * @author jerry
 */
@Repository
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
}
