package com.jm.esdemo;

import com.jm.esdemo.entity.Item;
import com.jm.esdemo.repository.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsDemoApplication.class)
public class EsDemoApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ItemRepository itemRepository;

    /**
     * 创建索引，会根据Item类的@Document注解信息来创建
     */
    @Test
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
        elasticsearchTemplate.putMapping(Item.class);
    }

    /**
     * 删除索引
     */
    @Test
    public void testDeleteIndex() {
        elasticsearchTemplate.deleteIndex(Item.class);
    }

    /**
     * 定义新增方法
     */
    @Test
    public void insert() {

        Item item = new Item(2L, "华为手机", " 手机",
                "华为", 3499.00, "http://image.baidu.com/13123.jpg");
        itemRepository.save(item);
    }

    /**
     * 批量新增
     */
    @Test
    public void insertList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(4L, "坚果手机R1", " 手机", "锤子", 3699.08, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(5L, "华为META10", " 手机", "华为", 4499.09, "http://image.baidu.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    //===================以下是查询

    /**
     * // 对某字段排序查找所有 Sort.by("price").descending() 降序
     * // Sort.by("price").ascending():升序
     */
    @Test
    public void testQueryAll() {
        Iterable<Item> list = this.itemRepository.findAll(Sort.by("price").ascending());

        for (Item item : list) {
            System.out.println(item);
        }
    }

    //===================自定义查询
    @Test
    public void testMatchQuery() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米手机"));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    //======================分页查询
    @Test
    public void searchByPage() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));
        // 分页：
        int page = 1;
        int size = 2;
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);
        // 总页数
        System.out.println("总页数 = " + items.getTotalPages());
        // 当前页
        System.out.println("当前页：" + items.getNumber());
        // 每页大小
        System.out.println("每页大小：" + items.getSize());

        for (Item item : items) {
            System.out.println(item);
        }
    }

    //=====================排序
    @Test
    public void searchAndSort() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));

        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);

        for (Item item : items) {
            System.out.println(item);
        }
    }

    //====================聚合（牛逼！！solr无此功能）
    
}
