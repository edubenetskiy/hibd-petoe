--
-- База данных: `mysqldb`
--

-- --------------------------------------------------------

--
-- Структура таблицы `coauthors`
--

CREATE DATABASE IF NOT EXISTS mysqldb;

USE mysqldb;

CREATE TABLE `coauthors`
(
    `publication_id` int(32) NOT NULL,
    `person_id`      int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `coauthors` (`publication_id`, `person_id`)
VALUES (1, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `conferences`
--

CREATE TABLE `conferences`
(
    `conference_id`    int(32)                                NOT NULL,
    `conference_name`  varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `conference_place` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `conference_date`  date                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `conferences` (`conference_id`, `conference_name`, `conference_place`, `conference_date`)
VALUES (1, 'Конференция по литературе', 'Санкт-Петербург', '2021-05-30');

-- --------------------------------------------------------

--
-- Структура таблицы `conferences_participants`
--

CREATE TABLE `conferences_participants`
(
    `person_id`     int(32) NOT NULL,
    `conference_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `conferences_participants` (`person_id`, `conference_id`)
VALUES ('1', '1');

-- --------------------------------------------------------

--
-- Структура таблицы `library_card`
--

CREATE TABLE `library_card`
(
    `person_id`    int(32)                                NOT NULL,
    `book_name`    varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `receive_date` date                                   NOT NULL,
    `return_date`  date                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `library_card` (`person_id`, `book_name`, `receive_date`, `return_date`)
VALUES (1, 'Война и мир', '2021-04-01', '2021-04-30');


-- --------------------------------------------------------

--
-- Структура таблицы `persons`
--

CREATE TABLE `persons`
(
    `person_id` int(32)                                NOT NULL,
    `name`      varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `position`  varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `persons` (`person_id`, `name`, `position`)
VALUES (1, 'Иванов Иван Иванович', 'Студент');


-- --------------------------------------------------------

--
-- Структура таблицы `projects`
--

CREATE TABLE `projects`
(
    `project_id`         int(32)                                 NOT NULL,
    `project_name`       varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `project_start_date` date                                    NOT NULL,
    `project_end_date`   date                                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `projects` (`project_id`, `project_name`, `project_start_date`, `project_end_date`)
VALUES (1, 'Исследование книги \"Война и мир\"', '2021-04-01', '2021-05-31');



-- --------------------------------------------------------

--
-- Структура таблицы `project_participants`
--

CREATE TABLE `project_participants`
(
    `person_id`  int(32) NOT NULL,
    `project_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `project_participants` (`person_id`, `project_id`)
VALUES (1, 1);



-- --------------------------------------------------------

--
-- Структура таблицы `publications`
--

CREATE TABLE `publications`
(
    `publication_id`   int(32)                                 NOT NULL,
    `publication_name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `edition_lang`     varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `edition_vol`      int(10)                                 NOT NULL,
    `edition_place`    varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `edition_type`     varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `citation_index`   int(10)                                 NOT NULL,
    `publication_date` date                                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `publications` (`publication_id`, `publication_name`, `edition_lang`, `edition_vol`, `edition_place`,
                            `edition_type`, `citation_index`, `publication_date`)
VALUES (1, 'Статья об исследовании книги \"Война и мир\"', 'Русский', 1, 'Санкт-Петербург', 'Научное', 2, '2021-05-31');



--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `coauthors`
--
ALTER TABLE `coauthors`
    ADD KEY `publication_id` (`publication_id`),
    ADD KEY `person_id` (`person_id`);

--
-- Индексы таблицы `conferences`
--
ALTER TABLE `conferences`
    ADD PRIMARY KEY (`conference_id`);

--
-- Индексы таблицы `conferences_participants`
--
ALTER TABLE `conferences_participants`
    ADD KEY `person_id` (`person_id`),
    ADD KEY `conference_id` (`conference_id`);

--
-- Индексы таблицы `library_card`
--
ALTER TABLE `library_card`
    ADD KEY `person_id` (`person_id`);

--
-- Индексы таблицы `persons`
--
ALTER TABLE `persons`
    ADD PRIMARY KEY (`person_id`);

--
-- Индексы таблицы `projects`
--
ALTER TABLE `projects`
    ADD PRIMARY KEY (`project_id`);

--
-- Индексы таблицы `project_participants`
--
ALTER TABLE `project_participants`
    ADD KEY `person_id` (`person_id`),
    ADD KEY `project_id` (`project_id`) USING BTREE;

--
-- Индексы таблицы `publications`
--
ALTER TABLE `publications`
    ADD PRIMARY KEY (`publication_id`);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `coauthors`
--
ALTER TABLE `coauthors`
    ADD CONSTRAINT `coauthors_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `persons` (`person_id`),
    ADD CONSTRAINT `coauthors_ibfk_2` FOREIGN KEY (`publication_id`) REFERENCES `publications` (`publication_id`);

--
-- Ограничения внешнего ключа таблицы `conferences_participants`
--
ALTER TABLE `conferences_participants`
    ADD CONSTRAINT `conferences_participants_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `persons` (`person_id`),
    ADD CONSTRAINT `conferences_participants_ibfk_2` FOREIGN KEY (`conference_id`) REFERENCES `conferences` (`conference_id`);

--
-- Ограничения внешнего ключа таблицы `library_card`
--
ALTER TABLE `library_card`
    ADD CONSTRAINT `library_card_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `persons` (`person_id`);

--
-- Ограничения внешнего ключа таблицы `project_participants`
--
ALTER TABLE `project_participants`
    ADD CONSTRAINT `project_participants_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `persons` (`person_id`),
    ADD CONSTRAINT `project_participants_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`);
COMMIT;
