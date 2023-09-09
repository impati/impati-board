package com.example.impatiboard.services.common;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntityFinder {

    private final EntityManager entityManager;

    public <T> T find(final Long id, final Class<T> clazz) {
        T result = entityManager.find(clazz, id);
        if (result == null) {
            throw new BoardApiException(ErrorCode.NOT_FOUND);
        }

        return result;
    }
}
