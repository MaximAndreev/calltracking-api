### Модуль для работы с [API Calltracking.ru](https://calltracking.ru/)

Модуль состоит из двух частей:
* `ru.avtomir.calltrackingru.CalltrackingRu` - интерфейс по получению данных из API.
* `ru.avtomir.calltrackingru.credential.Credential` - учетная запись для доступа в API.

##### Пример использвания:
1. Создаем `Credential` с помощью утильного класса `ru.avtomir.calltrackingru.util.Credentials`

    Для создания требуется указать путь к JSON-файлу формата:
    ````json
    {
      "login": "login",
      "password": "password",
      "token": "token (can be empty)"
    }
    ````
    При необходимости бибилиотека обновит `token` и сохранит его в указанный файл (!необходимы права на запись).
    
2. Передаем `Credential` в конретную имплементацию `ru.avtomir.calltrackingru.CalltrackingRuImpl`

Если API Calltracking.ru вернет ошибку и за 3 попытки библиотека повторными запросами не сможет ее решить,
 то ошибка будет обернута в `RequestCalltrackingRuException`.
