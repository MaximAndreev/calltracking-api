[![CircleCI](https://circleci.com/gh/MaximAndreev/calltracking-api/tree/master.svg?style=svg)](https://circleci.com/gh/MaximAndreev/calltracking-api/tree/master)
[![codecov](https://codecov.io/gh/MaximAndreev/calltracking-api/branch/master/graph/badge.svg)](https://codecov.io/gh/MaximAndreev/calltracking-api)
[![](https://jitpack.io/v/MaximAndreev/calltracking-api.svg)](https://jitpack.io/#MaximAndreev/calltracking-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6b6fd778a2c14ed7964cdf7b5f13726a)](https://www.codacy.com/app/My_Emails/calltracking-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MaximAndreev/calltracking-api&amp;utm_campaign=Badge_Grade)

### Модуль для работы с [API Calltracking.ru](https://calltracking.ru/) ###

Модуль состоит из двух частей:
*  `ru.avtomir.calltrackingru.CalltrackingRu` - интерфейс по получению данных из API.
*  `ru.avtomir.calltrackingru.credential.Credential` - учетная запись для доступа в API.

##### Пример использвания: #####
1.  Создаем `Credential` с помощью утильного класса `ru.avtomir.calltrackingru.util.Credentials`

    Для создания требуется указать путь к JSON-файлу формата:
    ````json
    {
      "login": "login",
      "password": "password",
      "token": "token (can be empty)"
    }
    ````
    При необходимости бибилиотека обновит `token` и сохранит его в указанный файл (!необходимы права на запись).
    
2.  Передаем `Credential` в конретную имплементацию `ru.avtomir.calltrackingru.CalltrackingRuImpl`

Если API Calltracking.ru вернет ошибку и за 3 попытки библиотека повторными запросами не сможет ее решить,
 то ошибка будет обернута в `RequestCalltrackingRuException`.
