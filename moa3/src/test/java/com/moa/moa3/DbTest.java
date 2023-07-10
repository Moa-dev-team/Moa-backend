package com.moa.moa3;

import com.moa.moa3.entity.test.Loram;
import com.moa.moa3.service.test.LoramService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DbTest {
    @Autowired
    LoramService loramService;

    @Test
    void Entity_저장() {
        Loram loram = new Loram("test");
        loramService.save(loram);

        Loram findLoram = loramService.findById(loram.getId());
        Assertions.assertThat(findLoram.getName()).isEqualTo("test");
    }

    @Test
    void Entity_수정() {
        Loram loram = new Loram("old test");
        loramService.save(loram);

        Loram changeLoram = loramService.changeName(loram.getId(), "new test");
        Assertions.assertThat(changeLoram.getName()).isEqualTo("new test");
    }

    @Test
    void Entity_삭제() {
        Loram loram = new Loram("test");
        loramService.save(loram);

        Loram findLoram = loramService.findById(loram.getId());
        Assertions.assertThat(findLoram.getName()).isEqualTo("test");

        loramService.delete(loram.getId());
        Assertions.assertThatThrownBy(() -> loramService.findById(loram.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
