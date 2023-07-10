package com.moa.moa3.service.test;

import com.moa.moa3.entity.test.Loram;
import com.moa.moa3.repository.test.LoramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoramService {
    private final LoramRepository loramRepository;
    @Transactional
    public void save(Loram loram) {
        loramRepository.save(loram);
    }

    public Loram findById(Long id) {
        return loramRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 없습니다.")
        );
    }

    @Transactional
    public Loram changeName(Long id, String name) {
        Loram loram = loramRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 없습니다.")
        );
        loram.setName(name);
        return loram;
    }

    @Transactional
    public void delete(Long id) {
        Loram loram = loramRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 없습니다.")
        );
        loramRepository.delete(loram);
    }
}
