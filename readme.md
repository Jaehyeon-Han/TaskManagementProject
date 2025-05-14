# API 명세

openapi.yml 파일 참고 요망

---

# ERD

![erd_with_users.png](D:\WebProgramming\BootCamp\ch3\task-management\images\erd_with_users.png)

---

# 요구사항

전반적으로는 주어진 요구사항을 따랐고, 테이블을 분리하면서 해석이 애매한 부분은 제 나름대로 해석하였습니다. 또한 고유 식별자를 통해 검색하는 기능은 전체 서비스 기능 상 사용자가 다른 사람의 id를 알 방법이 없다고 생각하여 이름으로 조회하도록 만들었는데, 주어진 요구사항을 맞게 구현하는 게 더 나았을 거란 생각이 듭니다.

1. 기본 CRUD: 자세한 사항은 아래의 API 동작 예시를 참고해주세요.
   
   * Create: `/tasks`에 `POST` 요청
   
   * Read: `/tasks`에 `GET` 요청, `/tasks/{taskId}`에 `GET` 요청
   
   * Update: `/tasks/{taskId}`에 `PATCH` 요청
   
   * Delete: `/tasks/{taskId}`에 `DELETE` 요청

2. 페이지네이션
   
   `/tasks`에 `GET` 요청 시 기본적으로 페이지네이션된 결과를 반환하였습니다.
   
   코드는 다음과 같이 작성하였습니다.
   
   ```java
   // Controller 계층
   public ResponseEntity<PagedTaskResponse> 
   findAllTasks(@RequestParam(required = false) String author,
               @RequestParam(required = false) LocalDate lastModifiedDate,
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "1") int pageSize) {
      return ResponseEntity.ok(taskService.findAllTasks(author, lastModifiedDate, page, pageSize));
   }
   
   // Service 계층
   int offset = page * size;
   
   List<TaskDto> allTasks = taskRepository.findAllTasks(author, lastModifiedDate, size, offset);
   List<TaskResponseDto> pagedTasks = allTasks.stream().map(TaskResponseDto::fromTaskDto).toList();
   
   long totalElements = taskRepository.countTasks();
   
   int totalPages = (int) Math.ceil((double) totalElements / size);
   
   return new PagedTaskResponse(pagedTasks, page, size, totalElements, totalPages);
   
   // Repository 계층
   StringBuilder sql = new StringBuilder(
          """
          SELECT task_id, task, t.author_id, u.name AS author_name, t.password, t.created_at, t.last_modified_at
          FROM tasks t
          LEFT JOIN users u ON t.author_id = u.user_id
          WHERE 1=1
          """
   );
   
   // 검색 조건에 따른 필터링
   List<Object> params = new ArrayList<>();
   
   if (authorName != null) {
      sql.append(" AND u.name = ?");
      params.add(authorName);
   }
   
   if (lastModifiedDate != null) {
      sql.append(" AND DATE(t.last_modified_at) = ?");
      params.add(lastModifiedDate);
   }
   
   // 오프셋 기반 페이지네이션
   sql.append("""
          ORDER BY t.last_modified_at DESC
          LIMIT ? OFFSET ?
          """);
   params.add(limit);
   params.add(offset);
   
   return jdbcTemplate.query(sql.toString(), params.toArray(), taskDtoRowMapper());
   ```

3. 검증과 예외 처리
   
   DTO에 빈 검증 어노테이션을 붙여서 요청값들에 대한 검증을 진행하였습니다.
   
   ```java
   public class CreateTaskDto {
      @NotBlank
      @Size(max = 200)
      private String task;
      @NotNull
      private Long authorId;
      @NotBlank
      private String authorPassword;
      @NotBlank
      private String taskPassword;
   }
   ```
   
   또한 사용자 비밀번호 불일치, 할일 비밀번호 불일치는 별도의 예외 클래스를 만들었습니다. (e.g. `TaskPasswordMismatchException`)
   
   이렇게 발생한 예외는 `TaskController`와 `UserController` 공통인 부분은 `global/error` 패키지 하에 `GlobalExceptionHandler`를 `@ControllerAdvice`로 사용하여 처리하였고, 오버라이드하고 싶은 경우가 있거나 해당 컨트롤러에만 한정적인 경우는 해당 컨트롤러 안에 `@ExceptionHandler`로 직접 작성하였습니다.
   
   ```java
   @ControllerAdvice
   public class GlobalExceptionHandler {
   
       @ExceptionHandler
       public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
           Map<String, String> errors = new HashMap<>();
   
           ex.getBindingResult().getAllErrors().forEach(error -> {
               String fieldName = ((FieldError) error).getField();
               String errorMessage = error.getDefaultMessage();
               errors.put(fieldName, errorMessage);
           });
   
           return ResponseEntity.badRequest().body(errors);
       }
   
       // ...
   
       @ExceptionHandler
       public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }
   }
   
   public class TaskController {
       @ExceptionHandler
       public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
       }
   
       @ExceptionHandler
       public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }
   
       // ...
   }
   ```

---

# API 동작 예시

## Task 관련 API

### /tasks

1. GET: 쿼리 파라미터를 만족하는 할일을 페이지네이션하여 조회한다. 최종수정일 기준 내림차순으로 정렬된다.
   
   요청 시 쿼리 파라미터
   
   * author(string): 작성자 이름
   
   * lastModifiedDate(string): 최종 수정일 (yyyy-mm-dd)
   
   * page(integer): 요청 페이지 번호, 기본값 0
   
   * pageSize(integer): 한 페이지의 목록 수, 기본값 1
   
   응답
   
   ```json
   // 정상 조회: 200
   {
       "content": [
           {
               "id": 1,
               "task": "task",
               "authorId": 1,
               "authorName": "god",
               "createdAt": "2025-05-14T14:46:07",
               "lastModifiedAt": "2025-05-14T14:46:07"
           }
       ],
       "page": 0,
       "size": 1,
       "totalElements": 1,
       "totalPages": 1
   }
   ```

2. POST: 새로운 할일을 생성한다. `authorId`와 `authorPassword`가 등록된 정보와 일치해야 한다.
   
   요청
   
   ```json
   // 정상 요청
   {
       "task": "create world", // 200자 이내, 필수
       "taskPassword": "iamgod", // 필수
       "authorId": "1", // 필수
       "authorPassword": "genesis" // 필수
   }
   ```
   
   응답
   
   ```json
   // 정상 등록: 201, http://localhost:8080/tasks/1
   {
       "id": 1,
       "task": "create world",
       "authorId": 1,
       "authorName": "god",
       "createdAt": "2025-05-14T14:46:07",
       "lastModifiedAt": "2025-05-14T14:46:07"
   }
   
   // authorId에 해당하는 사용자 없음: 400, Content-Type: text/plain;charset=UTF-8
   User for id 2 does not exist
   
   // 사용자 비밀번호 불일치: 403, Content-Type: text/plain;charset=UTF-8
   user password does not match
   ```

### /tasks/{taskId}

taskId(integer)

1. GET: taskId에 해당하는 할일을 조회한다.
   
   응답
   
   ```json
   // 정상 조회: 200
   {
     "id": 1,
     "task": "create world",
     "authorId": 1,
     "authorName": "god",
     "createdAt": "2025-05-14T06:05:29.804Z",
     "lastModifiedAt": "2025-05-14T06:05:29.804Z"
   }
   
   // taskId에 해당하는 할일 없음: 404, Content-Type: text/plain;charset=UTF-8
   Task for id 1 does not exist
   ```

2. PATCH: task 값을 수정한다. 할일 등록 시 등록한 비밀번호와 일치해야 한다.
   
   요청
   
   ```json
   {
       "taskPassword": "mytask", // 필수
       "task": "destroy world" // 필수
   }
   ```
   
   응답
   
   ```json
   // 정상 수정: 200
   {
       "id": 1,
       "task": destroy world",
       "authorId": 1,
       "authorName": "god",
       "createdAt": "2025-05-14T15:06:45",
       "lastModifiedAt": "2025-05-14T15:12:06"
   }
   
   // task 필드 미제공: 400, Content-Type: text/plain;charset=UTF-8
   업데이트할 필드가 없습니다.
   
   // 할일 비밀번호 불일치: 403, Content-Type: text/plain;charset=UTF-8
   password does not match
   
   // taskId에 해당하는 할일 없음: 404, Content-Type: text/plain;charset=UTF-8
   Task for id 2 does not exist
   ```

3. DELETE: 해당 taskId에 해당하는 할일을 삭제한다. taskPassword가 할일 등록 시 입력한 비밀번호와 일치해야 한다.
   
    요청
   
   ```json
   {
       "taskPassword": "mytask"
   }
   ```
   
    응답
   
   ```json
   // 정상 삭제: 204
   
   // taskPassword 미제공: 400
   {
     "taskPassword": "비어 있을 수 없습니다"
   }
   
   // 할일 비밀번호 불일치: 403, Content-Type: text/plain;charset=UTF-8
   password does not match
   
   // taskId에 해당하는 할일 없음: 404, Content-Type: text/plain;charset=UTF-8
   Task for id 2 does not exist
   ```

## User 관련 API

### /users

1. POST: 새로운 사용자를 등록한다.
   
   요청
   
   ```json
   // 정상 요청
   {
       "name": "Stephanie", // 필수
       "email": "my@gmail.com", // 필수, 이메일 형식
       "password": "password" // 필수
   }
   
   // 잘못된 요청
   {
       "email": "my",
       "password": "password"
   }
   ```
   
   응답
   
   ```json
   // 정상응답: 201, location: http://localhost:8080/users/2
   {
       "userId": 2,
       "name": "Stephanie",
       "email": "my@gmail.com",
       "createdAt": "2025-05-14T13:39:23",
       "lastModifiedAt": "2025-05-14T13:39:23"
   }
   
   // 잘못된 요청에 대한 응답: 400
   {
       "name": "공백일 수 없습니다",
       "email": "올바른 형식의 이메일 주소여야 합니다"
   }
   ```

2. GET: 모든 사용자를 조회한다.
   
   응답
   
   ```json
   [
       {
           "userId": 1,
           "name": "god",
           "email": "god@heaven",
           "createdAt": "2025-05-14T13:19:53",
           "lastModifiedAt": "2025-05-14T13:19:53"
       },
       {
           "userId": 2,
           "name": "Stephanie",
           "email": "my@gmail.com",
           "createdAt": "2025-05-14T13:39:23",
           "lastModifiedAt": "2025-05-14T13:39:23"
       }
   ]
   ```

## /users/{userId}

userId(integer)

1. PATCH: 특정 사용자의 정보를 변경한다.
   
   요청
   
   ```json
   // 정상 요청
   {
       "name": "Steve", // 필수
       "password": "password" // 필수
   }
   
   // 바꿀 필드 미제공
   {
       "password": "password"
   }
   
   // 비밀번호 불일치
   {
       "name": "Steve",
       "password": "wrong_password"
   }
   ```
   
   응답
   
   ```json
   // 정상 응답: 200
   {
     "userId": 2,
     "name": "Steve",
     "email": "my@gmail.com",
     "createdAt": "2025-05-14T13:39:23",
     "lastModifiedAt": "2025-05-14T13:54:42"
   }
   
   // 변경 필드 미제공: 400, Content-Type: text/plain;charset=UTF-8
   업데이트할 필드가 없습니다.
   
   // 비밀번호 불일치: 403, Content-Type: text/plain;charset=UTF-8
   user password does not match
   
   // 해당 리소스 없음: 404, Content-Type: text/plain;charset=UTF-8
   User for id : 2 does not exist
   ```