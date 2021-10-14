package top.parak.gulimall.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.parak.gulimall.product.service.CategoryService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class GulimallProductApplicationTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void test() {
        log.info("path = {}", Arrays.asList(categoryService.findCatelogPath(225L)));
    }

}
