package com.gigigenie.init;

import com.gigigenie.domain.product.entity.Category;
import com.gigigenie.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Configuration
//@Profile({"test"})
@RequiredArgsConstructor
public class NotProd {

    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner init() {
        return (args) -> {
            log.info("init data...");

            if (categoryRepository.count() > 0) {
                log.info("이미 초기 데이터가 존재합니다.");
                return;
            }

            List<Category> categories = List.of(
                    Category.builder().categoryName("tv").categoryIcon("https://cdn-icons-png.flaticon.com/128/10811/10811514.png").build(),
                    Category.builder().categoryName("refrigerator").categoryIcon("https://cdn-icons-png.flaticon.com/128/2969/2969229.png").build(),
                    Category.builder().categoryName("washing_machine").categoryIcon("https://cdn-icons-png.flaticon.com/128/75/75258.png").build(),
                    Category.builder().categoryName("microwave").categoryIcon("https://cdn-icons-png.flaticon.com/128/508/508620.png").build(),
                    Category.builder().categoryName("air_conditioner").categoryIcon("https://cdn-icons-png.flaticon.com/128/863/863923.png").build(),
                    Category.builder().categoryName("vacuum").categoryIcon("https://cdn-icons-png.flaticon.com/128/4917/4917553.png").build(),
                    Category.builder().categoryName("water_purifier").categoryIcon("https://cdn-icons-png.flaticon.com/128/15512/15512166.png").build(),
                    Category.builder().categoryName("coffee_machine").categoryIcon("https://cdn-icons-png.flaticon.com/128/13888/13888309.png").build(),
                    Category.builder().categoryName("rice_cooker").categoryIcon("https://cdn-icons-png.flaticon.com/128/1670/1670652.png").build(),
                    Category.builder().categoryName("smartphone").categoryIcon("https://cdn-icons-png.flaticon.com/128/15/15874.png").build(),
                    Category.builder().categoryName("tablet").categoryIcon("https://cdn-icons-png.flaticon.com/128/25/25466.png").build(),
                    Category.builder().categoryName("laptop").categoryIcon("https://cdn-icons-png.flaticon.com/128/689/689396.png").build(),
                    Category.builder().categoryName("smartwatch").categoryIcon("https://cdn-icons-png.flaticon.com/128/6421/6421054.png").build(),
                    Category.builder().categoryName("earphone").categoryIcon("https://cdn-icons-png.flaticon.com/128/5906/5906124.png").build(),
                    Category.builder().categoryName("ebook_reader").categoryIcon("https://cdn-icons-png.flaticon.com/128/18418/18418628.png").build()
            );
            categoryRepository.saveAll(categories);
            log.info("Category 초기 데이터 생성 완료");
        };
    }
}
