package ru.h562.smallsite.dao;

/**
 * Завершение работы БД
 * Сжатие БД
 */
public interface DBServiceDao {
    void ShutdownDB();
    void ShutdownCompactDB();
}
