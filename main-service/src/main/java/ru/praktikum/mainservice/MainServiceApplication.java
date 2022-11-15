package ru.praktikum.mainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

    // TODO Какие таблицы лучше будет проиндексировать?
    // TODO Разобраться с модулем статистики, почему в нем ошибки и как их исправить;

    // TODO Нарисовать архитектуру приложения и каждой из БД;
    // TODO Настроить 2 БД и докер;
    // TODO Сделать ПР и выложить в Яндекс презентацию;

    // TODO Что уже сделано из логики:
	/*
	Public: События
	    - GET /events - Получение событий с возможностью фильтрации
        - GET /events/{id} - Получение подробной информации об опубликованном событии по его идентификатору

    Public: Подборки событий
        + GET /compilations - Получение подборок событий
        + GET /compilations/{compId} - Получение подборки событий по его id

    Public: Категории
        + GET /categories - Получение категорий
        + GET /categories/{catId} - Получение информации о категории по её идентификатору

    Private: События
        + GET /users/{userId}/events - Получение событий, добавленных текущим пользователем
        + PATCH /users/{userId}/events - Изменение события добавленного текущим пользователем
        + POST/users/{userId}/events - Добавление нового события
        + GET /users/{userId}/events/{eventId} - Получение полной информации о событии добавленном текущим пользователем
        + PATCH /users/{userId}/events/{eventId} - Отмена события добавленного текущим пользователем.
        + GET /users/{userId}/events/{eventId}/requests - Получение информации о запросах на участие в событии текущего пользователя
        + PATCH /users/{userId}/events/{eventId}/requests/{reqId}/confirm - Подтверждение чужой заявки на участие в событии текущего пользователя
        + PATCH /users/{userId}/events/{eventId}/requests/{reqId}/reject - Отклонение чужой заявки на участие в событии текущего пользователя

    Private: Запросы на участие
        + GET /users/{userId}/requests - Получение информации о заявках текущего пользователя на участие в чужих событиях
        + POST /users/{userId}/requests - Добавление запроса от текущего пользователя на участие в событии
        + PATCH /users/{userId}/requests/{requestId}/cancel - Отмена своего запроса на участие в событии

    Admin: События
        + GET /admin/events - Поиск событий
        + PUT /admin/events/{eventId} - Редактирование события
        + PATCH /admin/events/{eventId}/publish - Публикация события
        + PATCH /admin/events/{eventId}/reject - Отклонение события

    Admin: Категории
        + PATCH /admin/categories - Изменение категории
        + POST /admin/categories - Добавление новой категории
        + DELETE /admin/categories/{catId} - Удаление категории

    Admin: Пользователи
        + GET /admin/users - Получение информации о пользователях
        + POST /admin/users - Добавление нового пользователя
        + DELETE /admin/users/{userId} - Удаление пользователя

    Admin: Подборки событий
        + POST /admin/compilations - Добавление новой подборки
        + DELETE /admin/compilations/{compId} - Удаление подборки
        + DELETE /admin/compilations/{compId}/events/{eventId} - Удалить событие из подборки
        - PATCH /admin/compilations/{compId}/events/{eventId} - Добавить событие в подборку
        - DELETE /admin/compilations/{compId}/pin - Открепить подборку на главной странице
        - PATCH /admin/compilations/{compId}/pin - Закрепить подборку на главной странице
	 */

}
